package com.Illumio.analyzer;

import com.Illumio.IllumioException;
import com.Illumio.utils.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IANAProtocolMapper {
    protected static final Logger logger = LoggerFactory.getLogger(IANAProtocolMapper.class);

    private Map<Integer, String> protocolMap = new HashMap<>();

    public IANAProtocolMapper() {
    }

    public String getProtocolName(int protocol) {
        return protocolMap.get(protocol);
    }

    public void initialize(String protocolNumberFile) throws IllumioException {
        logger.info("Initializing IANAProtocolMapper using input file {}", protocolNumberFile);
        try
        {
            CSVReader csvReader = new CSVReader(6, true);
            String[][] data = csvReader.load(protocolNumberFile);
            for (String[] row : data) {
                if(!row[0].contains("-")) { // record "146-252" Unassigned
                    protocolMap.put(Integer.parseInt(row[0]), row[1].toLowerCase());
                }
            }
        }
        catch (IOException e) {
            throw new IllumioException(e.getMessage());
        }
        logger.info("Initialized IANAProtocolMapper using input file {}", protocolNumberFile);
    }
}
