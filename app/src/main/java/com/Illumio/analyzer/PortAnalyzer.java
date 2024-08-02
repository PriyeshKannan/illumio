package com.Illumio.analyzer;

import com.Illumio.IllumioException;
import com.Illumio.data.Data;
import com.Illumio.data.PortPortocolData;
import com.Illumio.flowlog.record.FlowRecord;

public class PortAnalyzer implements Analyzer{
    private final IANAProtocolMapper ianaProtocolMapper;
    private final String protocolReport;

    public PortAnalyzer(IANAProtocolMapper ianaProtocolMapper, String protocolReport) {
        this.ianaProtocolMapper = ianaProtocolMapper;
        this.protocolReport = protocolReport;
    }

    @Override
    public Data analyze(FlowRecord flowRecord) {
        return new PortPortocolData(1,"");
    }

    @Override
    public void capture(Data data) {

    }

    @Override
    public void persist() throws IllumioException {

    }
}
