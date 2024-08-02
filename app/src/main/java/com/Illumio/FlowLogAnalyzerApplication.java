package com.Illumio;

import com.Illumio.analyzer.*;
import com.Illumio.data.Data;
import com.Illumio.flowlog.parser.FlowLogParserFactory;
import com.Illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class FlowLogAnalyzerApplication {
    private final List<Analyzer> analyzers = new ArrayList<>();
    private final FlowLogParserFactory flowLogParserFactory;
    protected static final Logger logger = LoggerFactory.getLogger(FlowLogAnalyzerApplication.class);

    public FlowLogAnalyzerApplication(TagAnalyzer tagAnalyzer, PortAnalyzer portAnalyzer){
        flowLogParserFactory = FlowLogParserFactory.getInstance();
        analyzers.add(tagAnalyzer);
        analyzers.add(portAnalyzer);
    }

    private static String property(String key, String defaultValue)
    {
        return System.getProperty(key, defaultValue);
    }

    public static void main(String[] args) throws Exception {
        // The code is executed from root path
        // say in a docker container,
        Path currentRelativePath = Paths.get("");
        String root = currentRelativePath.toAbsolutePath().toString();

        String tagConfig = property("tag.config.file", String.format("%s/config/tag_config.txt", root));
        String logFile =  property("flowlog.file", String.format("%s/log/flow_record.log", root));
        String tagReport = property("tag.report.file", String.format("%s/reports/tag.txt", root));
        String protocolReport = property("protocol.report.file",String.format("%s/reports/protocol.txt", root));
        String protocolNumber = property("protocol.number.file",
                String.format("%s/config/protocol-numbers.csv", root));

        IANAProtocolMapper ianaProtocolMapper = new IANAProtocolMapper();
        ianaProtocolMapper.initialize(protocolNumber);
        TagPortMapper tagPortMapper = new TagPortMapper();
        tagPortMapper.initialize(tagConfig);

        TagAnalyzer tagAnalyzer = new TagAnalyzer(ianaProtocolMapper, tagPortMapper, tagReport);
        PortAnalyzer portAnalyzer = new PortAnalyzer(ianaProtocolMapper, protocolReport);
        FlowLogAnalyzerApplication app = new FlowLogAnalyzerApplication(tagAnalyzer, portAnalyzer);

        app.process(logFile);
        app.generateReport();

    }

    private void process(String logFile) throws IllumioException {
        Path path = Paths.get(logFile);
        /*
         * Alternative can create a steam and create event handler to process
         * for keeping it sequential single threaded.
         * The log could be split , processed and aggregated/
         */
        try (BufferedReader reader = Files.newBufferedReader(path);) {
            String flowLog;
            while ((flowLog = reader.readLine()) != null) {
                FlowRecord flowRecord  = flowLogParserFactory.parse(flowLog);
                for (Analyzer analyzer : analyzers) {
                    Data data = analyzer.analyze(flowRecord);
                    analyzer.capture(data);
                }
            }
        }
        catch (IOException e) {
            logger.error("Error occured processing flowlogs {}", e.getMessage(), e);
            throw new IllumioException(e.getMessage());
        }
    }

    private void generateReport()  throws IllumioException {
        for (Analyzer analyzer : analyzers) {
            analyzer.persist();
        }
    }
}
