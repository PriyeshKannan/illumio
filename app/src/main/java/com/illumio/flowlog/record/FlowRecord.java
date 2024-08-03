package com.illumio.flowlog.record;

/**
 * This class is primarily a base class
 */
public class FlowRecord {
    private final String version;
    protected final String protocol;
    protected final String port;

    public FlowRecord(String version)
    {
        this(version, null,null); // Data not parse
    }

    public FlowRecord(String version, String protocol, String port) {
        this.version = version;
        this.protocol = protocol;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getPort() {
        return port;
    }

    public String getVersion() {
        return version;
    }
}
