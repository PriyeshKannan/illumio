package com.illumio.aggregator;

import com.illumio.IllumioException;
import com.illumio.utils.CSVReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TagPortProtocolMapper {
    private final Map<String, String> tagPortMap = new HashMap<>();
    private final Map<String, String> portProtocolMap = new HashMap<>();

    public TagPortProtocolMapper() {

    }

    public void initialize(String tagCSVConfig) throws IllumioException {
        try
        {
            CSVReader csvReader = new CSVReader(3, true);
            String[][] tagsData = csvReader.load(tagCSVConfig);
            for(String[] data: tagsData)
            {
                tagPortMap.put(data[0]+":"+data[1], data[2]);
                portProtocolMap.put(data[0], data[1]);
            }
        }
        catch (IOException e) {
            throw new IllumioException(e.getMessage());
        }
    }

    public String getTag(String port,  String protocol) {
        return tagPortMap.getOrDefault(port+":"+protocol, "Untagged");
    }

    public String getProtocol(String port) {
        return portProtocolMap.getOrDefault(port, "NotDefined" );
    }
}
