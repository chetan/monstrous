package net.pixelcop.monstrous.agent;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Node;
import net.pixelcop.monstrous.Stats;
import net.pixelcop.monstrous.http.JsonUtils;
import net.pixelcop.monstrous.load.LoadTester;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


public class AgentService {

    private static final AgentService instance = new AgentService();

    private Node server;

    public AgentService() {
        server = null;
    }

    public static AgentService getInstance() {
        return instance;
    }

    /**
     * Register with the server
     *
     * @param hostname
     */
    public void register(String hostname) throws IOException {
        DefaultHttpClient http = new DefaultHttpClient();
        http.execute(new HttpGet("http://" + hostname + ":9999/client/register"));
    }

    public void deregister(String hostname) throws IOException {
        DefaultHttpClient http = new DefaultHttpClient();
        http.execute(new HttpGet("http://" + hostname + ":9999/client/deregister"));
    }

    public void start(Node server, Job job) {

        if (this.server != null) {
            // TODO already have a job? error?
        }

        System.out.println("Got new job:");
        System.out.println(job.toString());
        System.out.println();

        this.server = server;
        LoadTester tester = new LoadTester(job);
        try {
            // start up test in a new thread so we can return quickly
            tester.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * Report stats back to the server
     *
     * @param collectStats
     */
    public void report(Stats stats) {

        String uri = "http://" + server.getAddress() + ":9999/stats/report";
        HttpPost post = new HttpPost(uri);
        try {
            post.setEntity(new StringEntity(JsonUtils.toJsonString(stats)));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DefaultHttpClient http = new DefaultHttpClient();
        try {
            http.execute(post);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        server = null; // TODO ready for new job?

    }

}
