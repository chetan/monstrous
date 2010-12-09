package net.pixelcop.monstrous.agent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Node;
import net.pixelcop.monstrous.http.AbstractJettyHandler;
import net.pixelcop.monstrous.http.JsonUtils;

public class AgentJettyHandler extends AbstractJettyHandler {

    @Override
    public String handle(String urlPath, HttpServletRequest request, HttpServletResponse response) {

        if (urlPath.equalsIgnoreCase("/job/new")) {
            System.out.println("agent: got job from server");
            Job job = (Job) JsonUtils.fromJsonString(readBody(request), Job.class);
            Node server = new Node(request.getRemoteAddr());
            AgentService.getInstance().start(server, job);
            return "OK";
        }
        
        return "NOT_FOUND";
        
    }

}
