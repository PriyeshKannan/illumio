package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;

public interface FlowLogParser {
    String STATUS_OK = "OK";
    String STATUS_NODATA = "NODATA";
    String STATUS_SKIP_DATA = "SKIPDATA";

    FlowRecord parse(String[] flowRecord) throws FlowLogParseException;
}
