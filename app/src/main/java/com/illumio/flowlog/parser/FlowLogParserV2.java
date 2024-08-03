package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FlowLogParserV2 implements FlowLogParser {
    int PORT_INDEX = 6;
    int PROTOCOL_INDEX = 7;
    int STATUS_INDEX = 13;  // Read only of data has OK

    protected static final Logger logger = LoggerFactory.getLogger(FlowLogParser.class);

    public FlowRecord parse(String[] flowRecords) throws FlowLogParseException {
        try {
            if(flowRecords[STATUS_INDEX].equalsIgnoreCase(STATUS_OK)){
                return new FlowRecord("2", flowRecords[PROTOCOL_INDEX], flowRecords[PORT_INDEX]);
            }
            return new FlowRecord("2");
        }
        catch (Exception e){
            logger.error("Failed to process the flowLog, error: {}" , e.getMessage());
            throw new FlowLogParseException("Failed to process flow record");
        }
    }
}
