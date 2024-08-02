package com.Illumio.analyzer;

import com.Illumio.IllumioException;
import com.Illumio.data.Data;
import com.Illumio.data.TagData;
import com.Illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


public class TagAnalyzer implements Analyzer{
    protected static final Logger logger = LoggerFactory.getLogger(TagAnalyzer.class);
    private TagPortMapper tagPortMapper;
    private IANAProtocolMapper ianaProtocolMapper;
    private final String reportFile;
    private Map<String, Integer> datas = new HashMap<>();

    public TagAnalyzer( IANAProtocolMapper ianaProtocolMapper, TagPortMapper tagPortMapper, String tagReport){
        this.reportFile = tagReport;
        this.ianaProtocolMapper = ianaProtocolMapper;
        this.tagPortMapper = tagPortMapper;
    }

    @Override
    public Data analyze(FlowRecord flowRecord) {
        String protocol = ianaProtocolMapper.getProtocolName(flowRecord.getProtocol());
        return new TagData( tagPortMapper.getTag(flowRecord.getPort(),  protocol));
    }

    @Override
    public void capture(Data data) {
        datas.merge(data.identifier(), 1, Integer::sum);
    }

    @Override
    public void persist() throws IllumioException {
        try(OutputStream stream = new FileOutputStream(reportFile)){
            BufferedWriter buffWriter  = new BufferedWriter(new OutputStreamWriter(stream));
            buffWriter.write("tag"+"\t"+"count");
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
