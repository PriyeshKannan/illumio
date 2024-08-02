package com.Illumio.flowlog.record;

/**
 * This class is primarily a base class
 */
public class FlowRecord {
    private final int version;
    protected final int protocol;
    protected final int port;

    public FlowRecord(int version)
    {
        this(version, -1,-1); // Data not parse
    }

    public FlowRecord(int version, int protocol, int port) {
        this.version = version;
        this.protocol = protocol;
        this.port = port;
    }

    public int getProtocol() {
        return protocol;
    }

    public int getPort() {
        return port;
    }

    public int getVersion() {
        return version;
    }
}
