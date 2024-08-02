package com.Illumio.flowlog.parser;

import com.Illumio.flowlog.record.FlowRecord;

import java.util.HashMap;
import java.util.Map;

public class FlowLogParserFactory {
    private final static int VERSION_INDEX = 0;

    Map<Integer, FlowLogParser> flowlogParserMap = new HashMap<>();

    private static class ParserFactorySingleton
    {
        private static final FlowLogParserFactory INSTANCE = new FlowLogParserFactory();
    }

    public static FlowLogParserFactory getInstance()
    {
        return ParserFactorySingleton.INSTANCE;
    }

    private FlowLogParserFactory()
    {
        flowlogParserMap.put(2, new FlowLogParserV2());
        flowlogParserMap.put(3, new FlowLogParserV3());
        flowlogParserMap.put(5, new FlowLogParserV5());
    }

    private FlowLogParser parser(int version) throws FlowLogParseException
    {
        FlowLogParser parser = flowlogParserMap.get(version);
        if (parser == null){
            throw new FlowLogParseException("Unsupported version " + version);
        }
        return parser;
    }

    public FlowRecord parse(String flowLog) throws FlowLogParseException{
        String[] flowRecords = flowLog.split(" ");
        int version = -1;
        try {
             version = Integer.parseInt(flowRecords[VERSION_INDEX]);
        }catch(Exception e){
            throw new FlowLogParseException("Invalid Flowlog, version field not found");
        }
        return parser(version).parse(flowRecords);

    }
}
