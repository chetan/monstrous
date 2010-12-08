package net.pixelcop.monstrous.agent;

import net.pixelcop.monstrous.http.AbstractNHttpServer;

import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpRequestHandlerResolver;

public class AgentHttpServer extends AbstractNHttpServer {
    
    public AgentHttpServer() {
        super(9998);
    }

    @Override
    public HttpRequestHandlerResolver createHandlers() {
        
        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("*", new AgentHttpHandler());
        return registry;
        
    }

}
