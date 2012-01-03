package net.pixelcop.monstrous.agent;

import java.io.IOException;

import org.eclipse.jetty.server.Server;

public class Main extends Thread {
    
    private String serverHost;
    
    public static void main(String[] args) throws Exception {
        new Main(args);
    }
    
    public Main(String[] args) throws Exception {

        Runtime.getRuntime().addShutdownHook(this);
        
        serverHost = "127.0.0.1";
        
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("--help") || args[0].equalsIgnoreCase("-h")) {
                System.out.println("usage: agent [<server host>]");
                return;
            }
            serverHost = args[0];
        }
        
        AgentService.getInstance().register(serverHost);
        
        Server server = new Server(9998);
        server.setHandler(new AgentJettyHandler());
        server.start();
    }
    
    @Override
    public void run() {
        
        try {
            AgentService.getInstance().deregister(serverHost);
        } catch (IOException e) {
        }
        
    }

}
