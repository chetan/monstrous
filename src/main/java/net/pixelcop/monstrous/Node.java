package net.pixelcop.monstrous;

import java.net.InetAddress;

public class Node {
    
    private InetAddress address;
    
    public Node(InetAddress address) {
        this.address = address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    public InetAddress getAddress() {
        return address;
    }

}
