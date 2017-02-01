package control;

/**
 * Created by Sugirjan on 16/12/2016.
 */
public class Guard extends Thread {
    private NetworkTools networkTools;
    public Guard(NetworkTools networkTools){
        this.networkTools = networkTools;
    }
    public void run(){
        Thread thisThread = Thread.currentThread();
        while (this == thisThread) {
            networkTools.sendMessage(Config.SHOOT);
            networkTools.sendMessage(Config.RIGHT);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
