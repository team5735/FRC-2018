package frc.team5735.controllers.auto;

import frc.team5735.controllers.Controller;

public abstract class AutoController implements Controller {
    protected boolean isFinished;
    private long startDelayTime;

    public AutoController() {
        isFinished = false;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean delay(long milliseconds) {
        if (startDelayTime == -1) {
            startDelayTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - startDelayTime >= milliseconds) {
            startDelayTime = -1;
            return true;
        }
        return false;
    }

}
