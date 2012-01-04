package net.pixelcop.monstrous.load;

import java.io.IOException;
import java.net.URI;

import net.pixelcop.monstrous.Job;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.ReusableClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApacheHttpThread extends HttpThread {

    private static final Logger LOG = LoggerFactory.getLogger(ApacheHttpThread.class);

    private final DefaultHttpClient client;
    private final HttpUriRequest request;
    private final HttpHost host;

    private final ClientConnectionManager clientConnManager;

    public ApacheHttpThread(Job job) {

        super();

        if (job.isKeepalive()) {
            this.clientConnManager = new ThreadSafeClientConnManager();
            this.client = new DefaultHttpClient(this.clientConnManager, null);
            this.client.setReuseStrategy(new KeepaliveConnectionReuseStrategy());

        } else {
            // try to disable keepalive & connection reuse
            this.clientConnManager = new ReusableClientConnManager();
            this.client = new DefaultHttpClient(this.clientConnManager, null);
            this.client.setReuseStrategy(new NoConnectionReuseStrategy());
        }

        this.request = new HttpGet((job.getUrl()));;
        this.host = determineTarget(request);
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

    @Override
    public void sendRequest() throws IOException {

        HttpResponse res = client.execute(host, request, (HttpContext) null);

        int status = res.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            stats.incrSuccessCount();
        } else {
            stats.incrErrorCount();
        }

    }

}
