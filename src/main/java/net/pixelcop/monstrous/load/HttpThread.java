package net.pixelcop.monstrous.load;

import java.net.URI;

import net.pixelcop.monstrous.Stats;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ReusableClientConnManager;
import org.apache.http.protocol.HttpContext;

public class HttpThread extends Thread {

    private final DefaultHttpClient client;
    private final HttpUriRequest request;
    private final HttpHost host;

    private final Stats stats;

    private long cutoffTime;

    public HttpThread(HttpUriRequest request) {
        this.client = new DefaultHttpClient(new ReusableClientConnManager(HttpClientUtils.createSimpleRegistry(), true), null);
        client.setReuseStrategy(new NoConnectionReuseStrategy());

        this.cutoffTime = 0;
        this.request = request;
        this.host = determineTarget(request);

        this.stats = new Stats();
    }

    @Override
    public void run() {
        do {
            try {
                HttpResponse res = client.execute(host, request, (HttpContext) null);

                int status = res.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    stats.incrSuccessCount();
                } else {
                    stats.incrErrorCount();
                }

                // System.out.println(res.getStatusLine());
                // System.out.println(res.getFirstHeader("Date"));
            } catch (Throwable t) {
                t.printStackTrace();
                stats.incrErrorCount();
            }

            if (cutoffTime != 0 && System.currentTimeMillis() >= cutoffTime) {
                // hit the time limit
                System.out.println("time limit reached in thread " + getId());
                return;
            }

        } while (!isInterrupted());
    }

    public void printStatus() {

        System.out.println("Success: " + stats.getSuccessCount());
        System.out.println("Error: " + stats.getErrorCount());

    }

    private HttpHost determineTarget(HttpUriRequest request) {
        // A null target may be acceptable if there is a default target.
        // Otherwise, the null target is detected in the director.
        HttpHost target = null;

        URI requestURI = request.getURI();
        if (requestURI.isAbsolute()) {
            target = new HttpHost(
                    requestURI.getHost(),
                    requestURI.getPort(),
                    requestURI.getScheme());
        }
        return target;
    }

    public Stats getStats() {
        return stats;
    }

    public void setCutoffTime(long cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

}
