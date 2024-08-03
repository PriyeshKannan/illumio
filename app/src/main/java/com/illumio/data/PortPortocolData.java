package com.illumio.data;

public class PortPortocolData implements Data{
    private final String port;
    private final String protocol;

    public PortPortocolData(String port, String protocol)
    {
        this.port = port;
        this.protocol = protocol;
    }

    public String identifier() {
        return String.valueOf(port);
    }
}
