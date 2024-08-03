package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FlowLogParserFactoryTest {

    @Test
    public void parserCheckVersion() throws Exception {
        String flowLogs = "2 123456789010 eni-1235b8ca123456789 172.31.16.139 172.31.16.21 20641 22 6 20 4249 1418530010 1418530070 ACCEPT OK";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        assertEquals("2",record.getVersion());
    }

    @Test
    public void parserCheckVersionUnsupported() {
        String flowLogs = "6 no record";
        FlowLogParseException thrown = assertThrows(
                FlowLogParseException.class,
                () ->  FlowLogParserFactory.getInstance().parse(flowLogs)
        );

        assertEquals(thrown.getMessage(), "Unsupported version 6");
    }

    @Test
    public void parserBadLog() {
        String flowLogs = "no record";
        FlowLogParseException thrown = assertThrows(
                FlowLogParseException.class,
                () ->  FlowLogParserFactory.getInstance().parse(flowLogs)
        );
        assertEquals(thrown.getMessage(), "Invalid Flowlog, version field not found");
    }
}