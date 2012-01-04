package net.pixelcop.monstrous.load;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;

public class KeepaliveConnectionReuseStrategy implements ConnectionReuseStrategy {

    @Override
    public boolean keepAlive(HttpResponse response, HttpContext context) {
        return true;
    }

}
