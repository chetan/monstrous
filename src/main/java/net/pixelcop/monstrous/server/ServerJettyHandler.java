package net.pixelcop.monstrous.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.pixelcop.monstrous.Job;
import net.pixelcop.monstrous.Node;
import net.pixelcop.monstrous.Stats;
import net.pixelcop.monstrous.http.AbstractJettyHandler;
import net.pixelcop.monstrous.http.JsonUtils;

public class ServerJettyHandler extends AbstractJettyHandler {

    @Override
    public String handle(String urlPath, HttpServletRequest request, HttpServletResponse response) {
        
        if (urlPath.equalsIgnoreCase("/client/register")) {
            System.out.println("server: registering new client");
            Server.getInstance().addClient(new Node(request.getRemoteAddr()));
            return "OK";
        }
        
        if (urlPath.equalsIgnoreCase("/client/deregister")) {
            System.out.println("server: deregistering client");
            Server.getInstance().removeClient(request.getRemoteAddr());
            return "OK";
        }
        
        if (urlPath.equalsIgnoreCase("/job/new")) {
            System.out.println("server: starting new job");
            Job job = (Job) JsonUtils.fromJsonString(readBody(request), Job.class);
            Server.getInstance().createJob(job);
            return "OK";
        }
        
        if (urlPath.equalsIgnoreCase("/stats/report")) {
            System.out.println("server: getting stats from client");
            Stats stats = (Stats) JsonUtils.fromJsonString(readBody(request), Stats.class);
            
            Server server = Server.getInstance();
            server.addStats(stats);
            
            if (server.isJobComplete()) {
                System.out.println();
                System.out.println("server: job completed!");
                server.printStats();
                System.out.println();
            }
            
            server.reset();
            
            return "OK";
        }
        
        return "NOT_FOUND";

    }

}
