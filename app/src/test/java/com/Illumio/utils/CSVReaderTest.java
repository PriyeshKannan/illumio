package com.Illumio.utils;

import com.Illumio.flowlog.parser.FlowLogParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {

    @Test
    void load() throws IOException {
        Path currentRelativePath = Paths.get("src/test/resources/sample.csv");
        String file = currentRelativePath.toAbsolutePath().toString();
        CSVReader csvReader = new CSVReader(3, true);
        String[][] data = csvReader.load(file);
        assertNotNull(data);
        assertEquals(data.length, 2);
        assertEquals(data[0].length, 3);
        assertEquals(data[0][0], "25");
    }

    @Test
    void loadNoSuchFile()  {

        CSVReader csvReader = new CSVReader(3, false);
        IOException thrown = assertThrows(
                IOException.class,
                () -> csvReader.load("somefile"));
        assertEquals(thrown.getMessage(), "somefile (No such file or directory)");

    }
}