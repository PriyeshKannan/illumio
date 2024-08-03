package com.illumio.aggregator;

import com.illumio.IllumioException;
import com.illumio.data.Data;
import com.illumio.data.PortPortocolData;
import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PortAnalyticsAggregator implements Aggregator {
    protected static final Logger logger = LoggerFactory.getLogger(PortAnalyticsAggregator.class);
    private TagPortProtocolMapper tagPorProtocolMapper;
    private final String protocolReport;
    private final Map<String, Integer> portCounts = new HashMap<>();

    public PortAnalyticsAggregator(TagPortProtocolMapper tagPorProtocolMapper, String protocolReport) {
        this.tagPorProtocolMapper = tagPorProtocolMapper;
        this.protocolReport = protocolReport;
    }

    @Override
    public Data analyze(FlowRecord flowRecord) {
        String protocol = tagPorProtocolMapper.getProtocol(flowRecord.getPort());
        return new PortPortocolData(flowRecord.getPort(),  protocol);
    }

    @Override
    public void capture(Data data) {
        portCounts.merge(data.identifier(), 1, Integer::sum);
    }

    @Override
    public void persist() throws IllumioException {
        try(OutputStream stream = new FileOutputStream(protocolReport)){
            BufferedWriter buffWriter  = new BufferedWriter(new OutputStreamWriter(stream));
            buffWriter.write("Port\tProtocol\tCount");
            buffWriter.newLine();
            for (Map.Entry<String, Integer> entry : portCounts.entrySet()) {
                String protocol = tagPorProtocolMapper.getProtocol(entry.getKey());
                buffWriter.write(String.format("%s\t%s\t%s", entry.getKey(), protocol, entry.getValue()));
                buffWriter.newLine();
            }
            buffWriter.flush();
        }catch (IOException exception){
            throw new IllumioException(exception.getMessage());
        }
        logger.info("Protocol count saved to report file {} ", protocolReport);
    }
}
