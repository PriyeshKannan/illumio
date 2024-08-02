package com.Illumio.flowlog.parser;

import com.Illumio.flowlog.record.FlowRecord;

public interface FlowLogParser {
    String STATUS_OK = "OK";
    String STATUS_NODATA = "NODATA";
    String STATUS_SKIP_DATA = "SKIPDATA";

    FlowRecord parse(String[] flowRecord) throws FlowLogParseException;
}
