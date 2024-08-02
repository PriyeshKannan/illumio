package com.Illumio.analyzer;

import com.Illumio.IllumioException;
import com.Illumio.data.Data;
import com.Illumio.utils.CSVReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TagPortMapper {
    private final Map<String, String> tagPortMap = new HashMap<>();

    public TagPortMapper() {

    }

    public void initialize(String tagCSVConfig) throws IllumioException {
        try
        {
            CSVReader csvReader = new CSVReader(3, true);
            String[][] tagsData = csvReader.load(tagCSVConfig);
            for(String[] data: tagsData)
            {
                tagPortMap.put(data[0]+":"+data[1], data[2]);
            }
        }
        catch (IOException e) {
            throw new IllumioException(e.getMessage());
        }
    }

    public String getTag(int port,  String protocol) {
        return tagPortMap.getOrDefault(port+":"+protocol, "Untagged");
    }
}
