package net.pixelcop.monstrous.load;

import java.io.IOException;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Stats;

public abstract class HttpThread extends Thread {

    protected final Stats stats;

    protected long cutoffTime;

    public HttpThread() {
        this.cutoffTime = 0;
        this.stats = new Stats();
    }

    public abstract void sendRequest() throws IOException;

    @Override
    public void run() {
        do {
            try {
                sendRequest();

                // System.out.println(res.getStatusLine());
                // System.out.println(res.getFirstHeader("Date"));
            } catch (Throwable t) {
                //LOG.error("error", t);
                stats.incrErrorCount();
            }

            if (cutoffTime != 0 && System.currentTimeMillis() >= cutoffTime) {
                // hit the time limit
                //System.out.println("time limit reached in thread " + getId());
                return;
            }

        } while (!isInterrupted());
    }

    public void printStatus() {
        System.out.println("Success: " + stats.getSuccessCount());
        System.out.println("Error: " + stats.getErrorCount());
    }

    public Stats getStats() {
        return stats;
    }

    public void setCutoffTime(long cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

    public static HttpThread create(Job job) {

        if (job.getClient().equalsIgnoreCase(Job.C_APACHE)) {
            return new ApacheHttpThread(job);
        }

        if (job.getClient().equalsIgnoreCase(Job.C_JETTY)) {
            return new JettyHttpThread(job);
        }

        return null;
    }

}
