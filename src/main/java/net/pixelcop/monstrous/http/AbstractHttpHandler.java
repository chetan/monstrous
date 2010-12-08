/**
 * 
 */
package net.pixelcop.monstrous.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.impl.nio.DefaultNHttpServerConnection;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

public abstract class AbstractHttpHandler implements HttpRequestHandler  {
    
    public AbstractHttpHandler() {
    }

    public abstract void handle(
            final HttpRequest request,
            final HttpResponse response,
            final HttpContext context) throws HttpException, IOException;
    
    public InetAddress getRemoteInetAddress(HttpContext context) {
        return ((DefaultNHttpServerConnection) context.getAttribute("http.connection"))
                .getRemoteAddress();
    }
    
    public void retJsonString(HttpResponse response, Object object) {
        ret(response, JsonUtils.toJsonString(object));
    }
    
    public void ret(HttpResponse response, String str) {
        try {
            response.setEntity(new NStringEntity(str));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}