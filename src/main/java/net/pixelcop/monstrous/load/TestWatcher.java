package net.pixelcop.monstrous.load;

import net.pixelcop.monstrous.Stats;
import net.pixelcop.monstrous.agent.AgentService;

public class TestWatcher extends Thread {

    private final LoadTester tester;

    public TestWatcher(LoadTester tester) {
        this.tester = tester;
    }

    @Override
    public void run() {

        System.out.println("agent: TestWatcher started");

        long cutoff = tester.getCutoffTime();

        while (!isInterrupted()) {

            if (System.currentTimeMillis() >= cutoff) {

                Stats stats = tester.collectStats();

                System.out.println("agent: going to report stats to server");
                try {
                    AgentService.getInstance().report(stats);
                } catch (Throwable t) {
                    System.out.println("agent: error reproting stats to server: " + t.getMessage());
                    stats.print(0);
                }
                return;

            }

        }

    }

}
