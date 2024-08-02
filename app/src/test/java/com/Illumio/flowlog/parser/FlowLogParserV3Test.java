package com.Illumio.flowlog.parser;

import com.Illumio.flowlog.record.FlowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowLogParserV3Test {

    @Test
    void parseNotImplemented() {
        StringBuilder flowLogs = new StringBuilder("3 eni-22222222222222222 123456789010 vpc-abcdefab012345678 ");
        flowLogs.append("subnet-22222222bbbbbbbbb - 10.40.2.236 10.40.2.31 80 39812 6 19 ");
        flowLogs.append("IPv4 10.40.2.236 10.20.33.164 ACCEPT OK");

        FlowLogParseException thrown = assertThrows(
                FlowLogParseException.class,
                () -> FlowLogParserFactory.getInstance().parse(flowLogs.toString()));
        assertEquals(thrown.getMessage(), "Version 3 not supported");
    }
}