package net.pixelcop.monstrous.server;

public class Main {
    
    public static void main(String[] args) throws Exception {
        
        HttpServer server = new HttpServer();
        server.start();
        server.join();
        
    }

}
