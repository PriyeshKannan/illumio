package com.Illumio.data;

public class PortPortocolData implements Data{
    private final int port;
    private final String protocol;

    public PortPortocolData(int port, String protocol)
    {
        this.port = port;
        this.protocol = protocol;
    }

    public String protocol() {
        return protocol;
    }
    public int port() {
        return port;
    }

    public String identifier() {
        return String.format("%s:%s", port(), protocol());
    }
}
