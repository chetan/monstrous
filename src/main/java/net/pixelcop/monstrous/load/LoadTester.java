package net.pixelcop.monstrous.load;

import java.util.ArrayList;
import java.util.Date;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Stats;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

public class LoadTester extends Thread {
    
    private Job job;
    private ArrayList<HttpThread> threads;
    
    private long startTime;
    private long cutoffTime;
    
    public LoadTester(Job job) {
        this.job = job;
        this.cutoffTime = 0;
    }
    
    /**
     * Collect stats from all threads
     * 
     * @return
     */
    public Stats collectStats() {
        Stats stats = new Stats();
        for (HttpThread thread : threads) {
            stats.add(thread.getStats());
        }
        return stats;        
    }
    
    private void createThread(HttpUriRequest request) {
        HttpThread t = new HttpThread(request);
        t.setCutoffTime(cutoffTime);
        t.start();
        threads.add(t);
    }
    
    public void shutdown() {
        
        Stats stats = new Stats();
        
        for (HttpThread thread : threads) {
            
            thread.interrupt();
            
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//            }
            
            stats.add(thread.getStats());
        }
        
        System.out.println("Shutdown completed @ " + new Date().toString() + "\n");
        
        stats.print(startTime);
    }

    @Override
    public void run() {

        System.out.println("Starting up with " + job.getNumThreads() + " threads @ " + new Date().toString());

        HttpUriRequest request = new HttpGet((job.getUrl()));
        
        startTime = System.currentTimeMillis();
        
        if (job.getType() == Job.T_TIME_LIMIT) {
            cutoffTime = System.currentTimeMillis() + (job.getNumSeconds() * 1000);
        }

        threads = new ArrayList<HttpThread>();
        for (int i = 0; i < job.getNumThreads(); i++) {
            createThread(request);
        }
        
        TestWatcher watcher = new TestWatcher(this);
        watcher.start();
        
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public ArrayList<HttpThread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<HttpThread> threads) {
        this.threads = threads;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCutoffTime() {
        return cutoffTime;
    }

    public void setCutoffTime(long cutoffTime) {
        this.cutoffTime = cutoffTime;
    }

}
