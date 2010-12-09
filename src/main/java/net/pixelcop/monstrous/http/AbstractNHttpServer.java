/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package net.pixelcop.monstrous.http;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;

import org.apache.http.HttpException;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.protocol.BufferingHttpServiceHandler;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.ListeningIOReactor;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerResolver;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

/**
 * Basic, yet fully functional and spec compliant, HTTP/1.1 server based on the non-blocking 
 * I/O model.
 * <p>
 * Please note the purpose of this application is demonstrate the usage of HttpCore APIs.
 * It is NOT intended to demonstrate the most efficient way of building an HTTP server. 
 * 
 *
 */
public abstract class AbstractNHttpServer extends Thread {
    
    private int port;
    
    public AbstractNHttpServer() {
        this(8080);
    }
    
    public AbstractNHttpServer(int port) {
        this.port = port;
    }
    
    @Override
    public void run() {
        
        HttpParams params = new SyncBasicHttpParams();
        params
            .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
            .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
            .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
            .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
            .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1");

        HttpProcessor httpproc = new ImmutableHttpProcessor(new HttpResponseInterceptor[] {
                new ResponseDate(),
                new ResponseServer(),
                new ResponseContent(),
                new ResponseConnControl()
        });
        
        BufferingHttpServiceHandler handler = new BufferingHttpServiceHandler(
                httpproc,
                new DefaultHttpResponseFactory(),
                new DefaultConnectionReuseStrategy(),
                params);

        // Set up request handlers
        handler.setHandlerResolver(createHandlers());

        // Provide an event logger
//        handler.setEventListener(new EventLogger());

        IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(handler, params);
        ListeningIOReactor ioReactor = null;
        
        try {
            ioReactor = new DefaultListeningIOReactor(4, params);
        } catch (IOReactorException e) {
            System.err.println("Failed to start server");
            e.printStackTrace();
            System.exit(1);
        }
        
        try {
            ioReactor.listen(new InetSocketAddress(port));
            ioReactor.execute(ioEventDispatch);
        } catch (InterruptedIOException ex) {
            System.err.println("Interrupted");
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
        System.out.println("Shutdown");
        
    }


    public abstract HttpRequestHandlerResolver createHandlers();


    static class EventLogger implements EventListener {

        public void connectionOpen(final NHttpConnection conn) {
            System.out.println("Connection open: " + conn);
        }

        public void connectionTimeout(final NHttpConnection conn) {
            System.out.println("Connection timed out: " + conn);
        }

        public void connectionClosed(final NHttpConnection conn) {
            System.out.println("Connection closed: " + conn);
        }

        public void fatalIOException(final IOException ex, final NHttpConnection conn) {
            System.err.println("I/O error: " + ex.getMessage());
        }

        public void fatalProtocolException(final HttpException ex, final NHttpConnection conn) {
            System.err.println("HTTP error: " + ex.getMessage());
        }

    }

}
