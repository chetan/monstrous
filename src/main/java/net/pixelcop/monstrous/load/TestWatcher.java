package net.pixelcop.monstrous.load;

import net.pixelcop.monstrous.agent.AgentService;

public class TestWatcher extends Thread {
    
    private LoadTester tester;
    
    public TestWatcher(LoadTester tester) {
        this.tester = tester;
    }
    
    @Override
    public void run() {
        
        System.out.println("agent: TestWatcher started");
        
        long cutoff = tester.getCutoffTime();
        
        while (!isInterrupted()) {
            
            if (System.currentTimeMillis() >= cutoff) {
                
                System.out.println("agent: going to report stats to server");
                AgentService.getInstance().report(tester.collectStats());
                return;
                
            }
            
        }
        
    }

}
