package net.pixelcop.monstrous.server;

import net.pixelcop.monstrous.http.NullJettyLogger;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        NullJettyLogger.install();
        
        org.mortbay.jetty.Server server = new org.mortbay.jetty.Server(9999);
        server.setHandler(new ServerJettyHandler());
        server.start();
        
    }

}
