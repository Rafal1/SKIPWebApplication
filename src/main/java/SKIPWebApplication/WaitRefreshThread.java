package SKIPWebApplication;

/**
 * @author Rafal Zawadzki
 */
public class WaitRefreshThread extends Thread {
    private Integer waitTime = 600000; //600 000 - 10min

    @Override
    public void run() {
        waitProc();
    }

    private void waitProc() {
        try {
            Thread.sleep(waitTime);
        } catch (final InterruptedException ignore) {
            return;
        }
        return;
    }
}
