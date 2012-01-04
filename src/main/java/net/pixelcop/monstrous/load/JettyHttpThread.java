package net.pixelcop.monstrous.load;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.pixelcop.monstrous.Job;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyHttpThread extends HttpThread {

    private final HttpClient client = new HttpClient();

    private URI uri;

    public JettyHttpThread(Job job) {
        super();
        try {
            uri = new URI(job.getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
        client.setMaxConnectionsPerAddress(200); // max 200 concurrent connections to every address
        client.setThreadPool(new QueuedThreadPool(200)); // max 250 threads
        client.setTimeout(30000); // 30 seconds timeout; if no server reply, the request expires
        try {
            client.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void sendRequest() throws IOException {
        HttpExchange exchange = new HttpExchange();
        exchange.setURI(uri);

        try {
            client.send(exchange);
            stats.incrSuccessCount();
        } catch (IOException e) {
            e.printStackTrace();
            stats.incrErrorCount();
        }
    }
}
