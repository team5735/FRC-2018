package frc.team5735.controllers.motionprofiling;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import frc.team5735.constants.RobotConstants;

public class MotionProfile {
    private MotionProfileStatus status = new MotionProfileStatus();

    /** additional cache for holding the active trajectory point */

    double pos = 0, vel = 0, heading = 0;

    /**
     * reference to the talon we plan on manipulating. We will not changeMode()
     * or call set(), just get motion profile status and make decisions based on
     * motion profile.
     */
    private TalonSRX talon;
    private double[][] profile;

    /**
     * State machine to make sure we let enough of the motion profile stream to
     * talon before we fire it.
     */
    private int state = 0;
    /**
     * Any time you have a state machine that waits for external events, its a
     * good idea to add a timeout. Set to -1 to disable. Set to nonzero to count
     * down to '0' which will print an error message. Counting loops is not a
     * very accurate method of tracking timeout, but this is just conservative
     * timeout. Getting time-stamps would certainly work too, this is just
     * simple (no need to worry about timer overflows).
     */
    private int loopTimeout = -1;
    /**
     * If start() gets called, this flag is set and in the control() we will
     * service it.
     */
    private boolean bStart = false;

    /**
     * Since the CANTalon.set() routine is mode specific, deduce what we want
     * the set value to be and let the calling module apply it whenever we
     * decide to switch to MP mode.
     */
    private SetValueMotionProfile setValue = SetValueMotionProfile.Disable;
    /**
     * How many trajectory points do we wait for before firing the motion
     * profile.
     */
    private static final int kMinPointsInTalon = 5;
    /**
     * Just a state timeout to make sure we don't get stuck anywhere. Each loop
     * is about 20ms.
     */
    private static final int kNumLoopsTimeout = 10;

    /**
     * Lets create a periodic task to funnel our trajectory points into our talon.
     * It doesn't need to be very accurate, just needs to keep pace with the motion
     * profiler executer.  Now if you're trajectory points are slow, there is no need
     * to do this, just call _talon.processMotionProfileBuffer() in your teleop loop.
     * Generally speaking you want to call it at least twice as fast as the duration
     * of your trajectory points.  So if they are firing every 20ms, you should call
     * every 10ms.
     */
    public int nextPointIndexToPush = 0;


    class PeriodicRunnable implements Runnable {
        public void run() {  talon.processMotionProfileBuffer();}
    }
    Notifier notifer = new Notifier(new PeriodicRunnable());


    /**
     * C'tor
     *
     * @param talon
     *            reference to Talon object to fetch motion profile status from.
     */
    public MotionProfile(TalonSRX talon, double[][] profile) {
        this.talon = talon;
        this.profile = profile;
        /*
         * since our MP is 10ms per point, set the control frame rate and the
         * notifer to half that
         */
        this.talon.changeMotionControlFramePeriod(5);
        notifer.startPeriodic(0.005);
    }

    /**
     * Called to clear Motion profile buffer and reset state info during
     * disabled and when Talon is not in MP control mode.
     */
    public void reset() {
        /*
         * Let's clear the buffer just in case user decided to disable in the
         * middle of an MP, and now we have the second half of a profile just
         * sitting in memory.
         */
        talon.clearMotionProfileTrajectories();
        /* When we do re-enter motionProfile control mode, stay disabled. */
        setValue = SetValueMotionProfile.Disable;
        /* When we do start running our state machine start at the beginning. */
        state = 0;
        loopTimeout = -1;
        /*
         * If application wanted to start an MP before, ignore and wait for next
         * button press
         */
        bStart = false;
//        System.out.println("Reset");
    }

    /**
     * Called every loop.
     */
    public void control() {
//        System.out.println("loopTimeout: " + loopTimeout);
        /* Get the motion profile status every loop */
        talon.getMotionProfileStatus(status);

        /*
         * track time, this is rudimentary but that's okay, we just want to make
         * sure things never get stuck.
         */
        if (loopTimeout < 0) {
            /* do nothing, timeout is disabled */
        } else {
            /* our timeout is nonzero */
            if (loopTimeout == 0) {
                /*
                 * something is wrong. Talon is not present, unplugged, breaker
                 * tripped
                 */
                Instrumentation.OnNoProgress();
            } else {
                --loopTimeout;
            }
        }

        /* first check if we are in MP mode */
        if (talon.getControlMode() != ControlMode.MotionProfile){
            /*
             * we are not in MP mode. We are probably driving the robot around
             * using gamepads or some other mode.
             */
            state = 0;
            loopTimeout = -1;
//            System.out.println("Get Control Mode: " + talon.getControlMode());
        } else {
            /*
             * we are in MP control mode. That means: starting Mps, checking Mp
             * progress, and possibly interrupting MPs if thats what you want to
             * do.
             */
//            System.out.println(state);
            fillBuffer();

            switch (state) {
                case 0: /* wait for application to tell us to start an MP */

                    if (bStart) {
                        bStart = false;

                        setValue = SetValueMotionProfile.Disable;

                        /*
                         * MP is being sent to CAN bus, wait a small amount of time
                         */
                        state = 1;
                        loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 1: /*
                 * wait for MP to stream to Talon, really just the first few
                 * points
                 */
                    /* do we have a minimum numberof points in Talon */
                    if (status.btmBufferCnt > kMinPointsInTalon) {
                        /* start (once) the motion profile */
                        setValue = SetValueMotionProfile.Enable;
                        /* MP will start once the control frame gets scheduled */
                        state = 2;
                        loopTimeout = kNumLoopsTimeout;
                    }
                    break;
                case 2: /* check the status of the MP */
                    /*
                     * if talon is reporting things are good, keep adding to our
                     * timeout. Really this is so that you can unplug your talon in
                     * the middle of an MP and react to it.
                     */
                    if (status.isUnderrun == false) {
                        loopTimeout = kNumLoopsTimeout;
                    }
                    /*
                     * If we are executing an MP and the MP finished, start loading
                     * another. We will go into hold state so robot servo's
                     * position.
                     */
                    if (status.btmBufferCnt == 0) { //status.activePointValid && status.isLast
                        /*
                         * because we set the last point's isLast to true, we will
                         * get here when the MP is done
                         */
                        setValue = SetValueMotionProfile.Hold;
                        state = 0;
                        loopTimeout = -1;
                        System.out.println("MP done");
                    }
                    break;
            }

//            if ((profile.length - nextPointIndexToPush) < 512 && (profile.length - nextPointIndexToPush) > 0) {
//                nextPointIndexToPush = startFilling(profile, nextPointIndexToPush, profile.length - nextPointIndexToPush) - 1;
//            } else {
//                nextPointIndexToPush = startFilling(profile, nextPointIndexToPush, 512);
//            }

            /* Get the motion profile status every loop */
            talon.getMotionProfileStatus(status);
            heading = talon.getActiveTrajectoryHeading();
            pos = talon.getActiveTrajectoryPosition();
            vel = talon.getActiveTrajectoryVelocity();
            /* printfs and/or logging */
//            Instrumentation.process(status, pos, vel, heading);
        }
    }
    /**
     * Find enum value if supported.
     * @param durationMs
     * @return enum equivalent of durationMs
     */
    private TrajectoryDuration GetTrajectoryDuration(int durationMs)
    {
        /* create return value */
        TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
        /* convert duration to supported type */
        retval = retval.valueOf(durationMs);
        /* check that it is valid */
        if (retval.value != durationMs) {
            DriverStation.reportError("Trajectory Duration not supported - use configMotionProfileTrajectoryPeriod instead", false);
        }
        /* pass to caller */
        return retval;
    }

    private int startFilling(double[][] profile, int startPoint, int totalCnt) {

        /* create an empty point */
        TrajectoryPoint point = new TrajectoryPoint();

        /* did we get an underrun condition since last time we checked ? */
        if (status.hasUnderrun) {
            /* better log it so we know about it */
            Instrumentation.OnUnderrun();
            /*
             * clear the error. This flag does not auto clear, this way
             * we never miss logging it.
             */
            talon.clearMotionProfileHasUnderrun(0);
        }
        /*
         * just in case we are interrupting another MP and there is still buffer
         * points in memory, clear it.
         */
//        talon.clearMotionProfileTrajectories();

        /* set the base trajectory period to zero, use the individual trajectory period below */
        talon.configMotionProfileTrajectoryPeriod(0, (int) profile[0][2]);

        /* This is fast since it's just into our TOP buffer */
        for (int i = startPoint; i < totalCnt + startPoint; i++) {
            double position = profile[i][0];    // IN Ft
            double velocity = profile[i][1];    // IN Ft/S
            /* for each point, fill our structure and pass it to API */

//            point.position = positionRot * Constants.kSensorUnitsPerRotation; //Convert Revolutions to Units
//            point.velocity = velocityRPM * Constants.kSensorUnitsPerRotation / 600.0; //Convert RPM to Units/100ms

            point.position = (position * 12 / (Math.PI * RobotConstants.WHEEL_DIAMETER)) * 4096; //Convert Feet to Units
            point.velocity = (velocity * 12 / (Math.PI * RobotConstants.WHEEL_DIAMETER)) * 4096 / 10; //Convert RPM to Units/100ms

            point.headingDeg = 0; /* future feature - not used in this example*/
            point.profileSlotSelect0 = 0; /* which set of gains would you like to use [0,3]? */
            point.profileSlotSelect1 = 0; /* future feature  - not used in this example - cascaded PID [0,1], leave zero */
            point.timeDur = GetTrajectoryDuration((int) profile[i][2]);
            point.zeroPos = false;
            if (i == 0)
                point.zeroPos = true; /* set this to true on the first point */

            point.isLastPoint = false;
//            if ((i + 1) == totalCnt) {
////                point.isLastPoint = true; /* set this to true on the last point  */
//                System.out.println("LAST POINT");
//            }

            talon.pushMotionProfileTrajectory(point);
//            System.out.println("{" + lPositionRot + ", " + lVelocityRPM + ", 16}");
        }
        return(startPoint + totalCnt);
    }
    /**
     * Called by application to signal Talon to start the buffered MP (when it's
     * able to).
     */
    public void startMotionProfile() {
        bStart = true;
        nextPointIndexToPush = 0;
    }
    
    public void fillBuffer() {
        int spaceAvailable = status.topBufferRem;
        int pointsToPush;
        if (spaceAvailable > profile.length - nextPointIndexToPush) {
            pointsToPush = profile.length - nextPointIndexToPush;
        }else{
            pointsToPush = spaceAvailable;
        }
//        System.out.println(nextPointIndexToPush + " : " + pointsToPush);

        nextPointIndexToPush = startFilling(profile, nextPointIndexToPush, pointsToPush);
    }

    /**
     *
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         motion-profile output, 1 for enable motion-profile, 2 for hold
     *         current motion profile trajectory point.
     */
    public SetValueMotionProfile getSetValue() {
        return setValue;
    }
}
