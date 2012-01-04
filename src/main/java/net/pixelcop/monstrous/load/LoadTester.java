package net.pixelcop.monstrous.load;

import java.util.ArrayList;
import java.util.Date;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Stats;
import net.pixelcop.monstrous.agent.AgentService;

public class LoadTester extends Thread {

    protected Job job;
    protected ArrayList<HttpThread> threads;

    protected long startTime;
    protected long cutoffTime;

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

    protected HttpThread createThread() {
        HttpThread t = HttpThread.create(job);
        t.setCutoffTime(cutoffTime);
        t.start();
        return t;
    }

//    public void shutdown() {
//
//        Stats stats = new Stats();
//
//        for (HttpThread thread : threads) {
//
//            thread.interrupt();
//
////            try {
////                thread.join();
////            } catch (InterruptedException e) {
////            }
//
//            stats.add(thread.getStats());
//        }
//
//        System.out.println("Shutdown completed @ " + new Date().toString() + "\n");
//
//        stats.print(startTime);
//    }

    private void joinThreads() {
        for (HttpThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        System.out.println("Starting up with " + job.getNumThreads() + " threads @ " + new Date().toString());

        startTime = System.currentTimeMillis();
        if (job.getType() == Job.T_TIME_LIMIT) {
            cutoffTime = System.currentTimeMillis() + (job.getNumSeconds() * 1000);
        }

        threads = new ArrayList<HttpThread>();
        for (int i = 0; i < job.getNumThreads(); i++) {
            threads.add(createThread());
        }

        joinThreads();

        Stats stats = collectStats();

        System.out.println("agent: going to report stats to server");
        try {
            AgentService.getInstance().report(stats);
        } catch (Throwable t) {
            System.out.println("agent: error reproting stats to server: " + t.getMessage());
            stats.print(startTime);
        }
        return;


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
