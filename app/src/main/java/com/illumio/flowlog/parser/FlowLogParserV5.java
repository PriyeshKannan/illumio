package com.illumio.flowlog.parser;

import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FlowLogParserV5 implements FlowLogParser {
    protected static final Logger logger = LoggerFactory.getLogger(FlowLogParserV5.class);

    public FlowLogParserV5()
    {

    }

    public FlowRecord parse(String[] flowRecords) throws FlowLogParseException {
        logger.error("Version 5 not supported ..");
        throw new FlowLogParseException("Version 5 not supported");
    }
}
