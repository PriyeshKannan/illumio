package com.illumio;

import com.illumio.aggregator.*;
import com.illumio.data.Data;
import com.illumio.flowlog.parser.FlowLogParserFactory;
import com.illumio.flowlog.record.FlowRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FlowLogAnalyzerApplication {
    private final List<Aggregator> analyzers = new ArrayList<>();
    private final FlowLogParserFactory flowLogParserFactory;
    protected static final Logger logger = LoggerFactory.getLogger(FlowLogAnalyzerApplication.class);

    public FlowLogAnalyzerApplication(TagAnalyticsAggregator tagAnalyticsAggregator, PortAnalyticsAggregator portAnalyticsAggregator){
        flowLogParserFactory = FlowLogParserFactory.getInstance();
        analyzers.add(tagAnalyticsAggregator);
        analyzers.add(portAnalyticsAggregator);
    }

    private static String property(String key, String defaultValue)
    {
        return System.getProperty(key, defaultValue);
    }

    public static void main(String[] args) throws Exception {
        // The code is executed from root path
        // say in a docker container,
        Path currentRelativePath = Paths.get("");
        String relativePathRoot = currentRelativePath.toAbsolutePath().toString();
        String root = property("root.dir", relativePathRoot);
        String configDir = property("config.dir", String.format("%s/config", root));
        String logDir = property("log.dir", String.format("%s/log", root));
        String reportDir = property("report.dir", String.format("%s/reports", root));

        String tagConfig = property("tag.config.file", String.format("%s/tag_config.txt", configDir));
        String logFile =  property("flowlog.file", String.format("%s/flow_record.log", logDir));
        String tagReport = property("tag.report.file", String.format("%s/tag.txt", reportDir));
        String protocolReport = property("protocol.report.file",String.format("%s/protocol.txt", reportDir));

        TagPortProtocolMapper tagPorProtocolMapper = new TagPortProtocolMapper();
        tagPorProtocolMapper.initialize(tagConfig);

        TagAnalyticsAggregator tagAnalyticsAggregator = new TagAnalyticsAggregator(tagPorProtocolMapper, tagReport);
        PortAnalyticsAggregator portAnalyticsAggregator = new PortAnalyticsAggregator(tagPorProtocolMapper, protocolReport);
        FlowLogAnalyzerApplication app = new FlowLogAnalyzerApplication(tagAnalyticsAggregator, portAnalyticsAggregator);

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
                // skipping no data and skip data
                if (flowRecord != null && flowRecord.getPort() != null ) {
                    for (Aggregator analyzer : analyzers) {
                        Data data = analyzer.analyze(flowRecord);
                        analyzer.capture(data);
                    }
                }
            }
        }
        catch (IOException e) {
            logger.error("Error occurred processing flow logs {}", e.getMessage(), e);
            throw new IllumioException(e.getMessage());
        }
    }

    private void generateReport()  throws IllumioException {
        for (Aggregator analyzer : analyzers) {
            analyzer.persist();
        }
    }
}
