package com.illumio.aggregator;

import com.illumio.IllumioException;
import com.illumio.data.Data;
import com.illumio.data.PortPortocolData;
import com.illumio.flowlog.parser.FlowLogParseException;
import com.illumio.flowlog.parser.FlowLogParserFactory;
import com.illumio.flowlog.record.FlowRecord;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class AggregatorTest {

    private TagPortProtocolMapper getTagPortProtocolMapper() throws Exception{
        Path currentRelativePath = Paths.get("src/test/resources/tag_config_sample.txt");
        String file = currentRelativePath.toAbsolutePath().toString();
        TagPortProtocolMapper tagPortProtocolMapper = new TagPortProtocolMapper();
        tagPortProtocolMapper.initialize(file);
        return tagPortProtocolMapper;
    }

    @Test
    void testTag() throws Exception {
        TagPortProtocolMapper tagPortProtocolMapper = getTagPortProtocolMapper();
        assertEquals(tagPortProtocolMapper.getTag("25", "tcp"), "sv_P1");
        assertEquals(tagPortProtocolMapper.getTag("100", "tcp"), "Untagged");
        assertEquals(tagPortProtocolMapper.getProtocol("25"), "tcp");
        assertEquals(tagPortProtocolMapper.getProtocol("100"), "NotDefined");
    }

    @Test
    void testProtocol() throws Exception {
        TagPortProtocolMapper tagPortProtocolMapper = getTagPortProtocolMapper();
        PortAnalyticsAggregator portAnalyticsAggregator = null;
        portAnalyticsAggregator = new PortAnalyticsAggregator(tagPortProtocolMapper, "sample.txt");
        String flowLogs = "2 123456789010 eni-1235b8ca123456789 172.31.16.139 172.31.16.21 20641 22 6 20 4249 1418530010 1418530070 ACCEPT OK";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        Data data = portAnalyticsAggregator.analyze(record);
        portAnalyticsAggregator.capture(data);
        assertNotNull(data);
        assertEquals(data.identifier(), "22");
    }

    @Test
    void testTagCount() throws Exception {
        TagPortProtocolMapper tagPortProtocolMapper = getTagPortProtocolMapper();
        TagAnalyticsAggregator tagAnalyticsAggregator = null;
        tagAnalyticsAggregator = new TagAnalyticsAggregator(tagPortProtocolMapper, "sample.txt");
        String flowLogs = "2 123456789010 eni-1235b8ca123456789 172.31.16.139 172.31.16.21 20641 25 6 20 4249 1418530010 1418530070 ACCEPT OK";
        FlowRecord record = FlowLogParserFactory.getInstance().parse(flowLogs);
        Data data = tagAnalyticsAggregator.analyze(record);
        tagAnalyticsAggregator.capture(data);
        assertNotNull(data);
        assertEquals(data.identifier(), "sv_P1");
    }

    @Test void testFailure() throws Exception{
        TagPortProtocolMapper tagPortProtocolMapper = new TagPortProtocolMapper();
        IllumioException thrown = assertThrows(
                IllumioException.class,
                () -> tagPortProtocolMapper.initialize("notfile.txt"));
        assertEquals(thrown.getMessage(), "notfile.txt (No such file or directory)");
    }
}