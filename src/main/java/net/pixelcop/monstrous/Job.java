package net.pixelcop.monstrous;

public class Job {

    public static final int T_FOREVER = 1;
    public static final int T_REQ_LIMIT = 2;
    public static final int T_TIME_LIMIT = 3;

    public static final String C_APACHE = "apache";
    public static final String C_JETTY = "jetty";

    private String url;
    private int type;

    private int numRequests;
    private int numThreads;
    private int numSeconds;

    private String client;
    private boolean keepalive;

    public Job() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumRequests() {
        return numRequests;
    }

    public void setNumRequests(int numRequests) {
        this.numRequests = numRequests;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public void setNumSeconds(int numSeconds) {
        this.numSeconds = numSeconds;
    }

    public int getNumSeconds() {
        return numSeconds;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("URL: " + url + "\n");
        sb.append("Type: " + type + "\n");
        sb.append("Threads: " + numThreads + "\n");
        sb.append("Requests: " + numRequests + "\n");
        sb.append("Seconds: " + numSeconds + "\n");
        sb.append("Client: " + client + "\n");
        sb.append("Keepalive: " + keepalive + "\n");

        return sb.toString();
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }

    public boolean isKeepalive() {
        return keepalive;
    }

}
