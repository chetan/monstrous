package net.pixelcop.monstrous.http;

import org.mortbay.log.Logger;

/**
 * Disable logging!
 * 
 * @author chetan
 *
 */
public class NullJettyLogger implements Logger {
    
    private static final NullJettyLogger logger = new NullJettyLogger(); 

    public static void install() {
        System.setProperty("org.mortbay.log.class", "net.pixelcop.monstrous.http.NullJettyLogger");
    }
    
    @Override
    public void debug(String msg, Throwable th) {
    }

    @Override
    public void debug(String msg, Object arg0, Object arg1) {
    }

    @Override
    public Logger getLogger(String name) {
        return logger;
    }

    @Override
    public void info(String msg, Object arg0, Object arg1) {
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
    }

    @Override
    public void warn(String msg, Throwable th) {
    }

    @Override
    public void warn(String msg, Object arg0, Object arg1) {
    }

}
