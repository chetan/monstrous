package net.pixelcop.monstrous.cli;

import java.io.UnsupportedEncodingException;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.http.JsonUtils;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        if (args.length == 0) {
            showUsage();
            return;
        }
        
        Job job = new Job();
        job.setType(Job.T_TIME_LIMIT);
        job.setNumThreads(Integer.parseInt(args[0]));
        job.setNumSeconds(Integer.parseInt(args[1]));
        job.setUrl(args[2]);
        // "http://ec2-50-16-15-72.compute-1.amazonaws.com/"

        
        String uri = "http://localhost:9999/job/new";
        HttpPost post = new HttpPost(uri);
        try {
            post.setEntity(new StringEntity(JsonUtils.toJsonString(job)));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        DefaultHttpClient http = new DefaultHttpClient();
        http.execute(post);
        
    }

    private static void showUsage() {
        System.out.println("usage: job <threads> <duration in sec> <url to test>");
    }

}
