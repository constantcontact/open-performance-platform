package com.opp.util;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class FileUtilTest {

    @Before
    public void setup() throws IOException {
        List<String> csvFile = Arrays.asList(
                "one,two,three",
                "1,2,3");
        Files.write(Paths.get("test.csv"), csvFile);
    }
    @Test
    public void readCsv() throws IOException {
        List<List<String>> csvFile = FileUtil.readCsv("test.csv");

        csvFile.forEach(line -> System.out.println(line));

        assertTrue(2 == csvFile.size());
    }

    @After
    public void destroy() throws IOException {
        Path path = Paths.get("test.csv");
        Files.delete(path);
    }
}
