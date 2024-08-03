package com.illumio.flowlog.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowLogParserV5Test {
    @Test
    void parseNotImplemented() {
        StringBuilder flowLogs = new StringBuilder("5 52.95.128.179 10.0.0.71 80 34210 6 1616729292 1616729349");
        flowLogs.append("IPv4 14 15044 123456789012 vpc-abcdefab012345678 subnet-aaaaaaaa012345678 ");
        flowLogs.append("i-0c50d5961bcb2d47b eni-1235b8ca123456789 ap-southeast-2 apse2-az3 - - ");
        flowLogs.append("ACCEPT 19 52.95.128.179 10.0.0.71 S3 - - ingress OK");
        FlowLogParseException thrown = assertThrows(
                FlowLogParseException.class,
                () -> FlowLogParserFactory.getInstance().parse(flowLogs.toString()));
        assertEquals(thrown.getMessage(), "Version 5 not supported");
    }
}