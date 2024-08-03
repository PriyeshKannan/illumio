package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlowLogParserV2Test {

    @Test
    void parseOK() throws Exception {
        String flowLogs = "2 123456789010 eni-1235b8ca123456789 172.31.16.139 172.31.16.21 20641 22 6 20 4249 1418530010 1418530070 ACCEPT OK";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        assertEquals(record.getPort(), "22");
        assertEquals(record.getProtocol(), "6");  // 6 is TCP as per IANA Port Number definition
    }

    @Test
    void parseNoData()  throws Exception {
        String flowLogs = "2 123456789010 eni-1235b8ca123456789 - - - - - - - 1431280876 1431280934 - NODATA";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        assertNull(record.getPort());
    }

    @Test
    void parseSkipData()  throws Exception {
        String flowLogs = "2 123456789010 eni-11111111aaaaaaaaa - - - - - - - 1431280876 1431280934 - SKIPDATA";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        assertNull(record.getPort());
    }

    @Test
    void parseBadData() {
        String flowLogs = "2 123456789010 eni-11111111aaaaaaaaa - - - - - - - 1431280876 1431280934";
        FlowLogParseException thrown = assertThrows(
                FlowLogParseException.class,
                () -> FlowLogParserFactory.getInstance().parse(flowLogs));
        assertTrue(thrown.getMessage().contains("Failed to process flow record"));
    }
}