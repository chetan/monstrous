package org.apache.http.impl.conn;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

public class ReusableClientConnManager extends SingleClientConnManager {
    
    private final Log log = LogFactory.getLog(getClass());
    
    private boolean alwaysRecreateConnections;
    
    public ReusableClientConnManager(SchemeRegistry schreg, boolean alwaysRecreateConnections) {
        super(null, schreg);
        this.alwaysRecreateConnections = alwaysRecreateConnections;
    }

    public ReusableClientConnManager(HttpParams params, SchemeRegistry schreg) {
        super(params, schreg);
        alwaysRecreateConnections = false;
    }
    
    @Override
    public synchronized ManagedClientConnection getConnection(HttpRoute route, Object state) {

        if (route == null) {
            throw new IllegalArgumentException("Route may not be null.");
        }
        assertStillUp();

        if (log.isDebugEnabled()) {
            log.debug("Get connection for route " + route);
        }
        
        // Skip this check, just assume it's being used correctly :-)
//        if (managedConn != null)
//            throw new IllegalStateException(MISUSE_MESSAGE);

        // check re-usability of the connection
        boolean recreate = false;
        boolean shutdown = alwaysRecreateConnections;
        
        // Kill the connection if it expired.
        closeExpiredConnections();
        
        if (!alwaysRecreateConnections) {
            if (uniquePoolEntry.connection.isOpen()) {
                RouteTracker tracker = uniquePoolEntry.tracker;
                shutdown = (tracker == null || // can happen if method is aborted
                            !tracker.toRoute().equals(route));
            } else {
                // If the connection is not open, create a new PoolEntry,
                // as the connection may have been marked not reusable,
                // due to aborts -- and the PoolEntry should not be reused
                // either.  There's no harm in recreating an entry if
                // the connection is closed.
                recreate = true;
            }
        }

        if (shutdown) {
            recreate = true;
            try {
                uniquePoolEntry.shutdown();
            } catch (IOException iox) {
                log.debug("Problem shutting down connection.", iox);
            }
        }
        
        if (recreate) {
            uniquePoolEntry = null;
            uniquePoolEntry = new PoolEntry();
            if (managedConn != null)
                managedConn.detach();
        }
        
        managedConn = new ConnAdapter(uniquePoolEntry, route);

        return managedConn;
        
    }

}
