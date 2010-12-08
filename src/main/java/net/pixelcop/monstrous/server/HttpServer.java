package net.pixelcop.monstrous.server;

import net.pixelcop.monstrous.http.AbstractNHttpServer;

import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpRequestHandlerResolver;

public class HttpServer extends AbstractNHttpServer {
    
    public HttpServer() {
        super(9999);
    }

    @Override
    public HttpRequestHandlerResolver createHandlers() {
        
        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("*", new ServerHttpHandler());
        return registry;
        
    }

}
