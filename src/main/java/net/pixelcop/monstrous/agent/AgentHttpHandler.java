package net.pixelcop.monstrous.agent;

import java.io.IOException;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Node;
import net.pixelcop.monstrous.http.AbstractHttpHandler;
import net.pixelcop.monstrous.http.JsonUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class AgentHttpHandler extends AbstractHttpHandler {
    
    @Override
    public void handle(HttpRequest request, HttpResponse response, HttpContext context)
            throws HttpException, IOException {
        
        String uri = request.getRequestLine().getUri();
                
        if (uri.equalsIgnoreCase("/job/new")) {
            System.out.println("agent: got job from server");
            Job job = (Job) JsonUtils.fromJsonString(readBody(request), Job.class);
            Node server = new Node(getRemoteInetAddress(context));
            AgentService.getInstance().start(server, job);
            ret(response, "OK");
            return;
        }
        
    }

    /**
     * Read the request body and return it as a string
     * 
     * @param request
     * @return
     */
    private String readBody(HttpRequest request) {
        
        HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
        try {
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
    }

}
