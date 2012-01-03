package net.pixelcop.monstrous.server;


public class Main extends Thread {
    
    public static void main(String[] args) throws Exception {
        new Main(args);        
    }
    
    public Main(String[] args) throws Exception {
        
        Runtime.getRuntime().addShutdownHook(this);
        
        org.eclipse.jetty.server.Server server = new org.eclipse.jetty.server.Server(9999);
        server.setHandler(new ServerJettyHandler());
        server.start();
    }
    
    @Override
    public void run() {
        System.out.println("Shutting down..");
        Server server = Server.getInstance();
        if (server.getStats() != null) {
            System.out.println();
            System.out.println("Results of last running job:");
            server.printStats();
        }
    }

}
