package com.illumio.aggregator;

import com.illumio.IllumioException;
import com.illumio.data.Data;
import com.illumio.data.TagData;
import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Requirement:  The dstport and protocol combination decide what tag can be applied.
 */
public class TagAnalyticsAggregator implements Aggregator {
    protected static final Logger logger = LoggerFactory.getLogger(TagAnalyticsAggregator.class);
    private TagPortProtocolMapper tagPorProtocolMapper;
    private final String reportFile;
    private Map<String, Integer> datas = new HashMap<>();

    public TagAnalyticsAggregator(TagPortProtocolMapper tagPorProtocolMapper, String tagReport){
        this.reportFile = tagReport;
        this.tagPorProtocolMapper = tagPorProtocolMapper;
    }

    @Override
    public Data analyze(FlowRecord flowRecord) {
        String protocol = tagPorProtocolMapper.getProtocol(flowRecord.getPort());
        return new TagData( tagPorProtocolMapper.getTag(flowRecord.getPort(),  protocol));
    }

    @Override
    public void capture(Data data) {
        datas.merge(data.identifier(), 1, Integer::sum);
    }

    @Override
    public void persist() throws IllumioException {
        try(OutputStream stream = new FileOutputStream(reportFile)){
            BufferedWriter buffWriter  = new BufferedWriter(new OutputStreamWriter(stream));
            buffWriter.write("Tag\tCount");
            buffWriter.newLine();
            for (Map.Entry<String, Integer> entry : datas.entrySet()) {
                buffWriter.write(entry.getKey()+"\t"+entry.getValue());
                buffWriter.newLine();
            }
            buffWriter.flush();
        }catch (IOException exception){
            throw new IllumioException(exception.getMessage());
        }
        logger.info("Tags count saved to report file {} ", reportFile);
    }
}
