package net.pixelcop.monstrous.http;

import java.io.IOException;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.util.CharArrayBuffer;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public abstract class AbstractJettyHandler extends AbstractHandler {
    
    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request,
            HttpServletResponse response) throws IOException, ServletException {

        response.setContentType("text/json");
        response.setStatus(HttpServletResponse.SC_OK);
        
        String res = handle(target, request, response);
        response.getOutputStream().println(res);
        
        baseRequest.setHandled(true);        
    }
    
    /**
     * Handle the request and return a String response back to the client
     * 
     * @param urlPath
     * @param request
     * @param response
     * @return
     */
    public abstract String handle(String urlPath, HttpServletRequest request,
            HttpServletResponse response);

    public String readBody(HttpServletRequest request) {
        
        Reader reader = null;
        try {
            reader = ((Request) request).getReader();
        } catch (IOException e) {
            return null;
        }
        
        int i = request.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        
        try {
            try {
                char[] tmp = new char[1024];
                int l;
                while((l = reader.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
            } finally {
                reader.close();
            }
            
        } catch (IOException e) {
        }
        return buffer.toString(); // return whatever we got
    }
    
}
