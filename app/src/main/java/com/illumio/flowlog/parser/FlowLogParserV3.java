package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FlowLogParserV3 implements FlowLogParser {
    protected static final Logger logger = LoggerFactory.getLogger(FlowLogParserV3.class);

    public FlowLogParserV3()
    {

    }

    public FlowRecord parse(String[] flowRecords) throws FlowLogParseException {
        logger.error("Version 3 not supported ..");
        throw new FlowLogParseException("Version 3 not supported");
    }
}
