package com.holidu.interview.assignment;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestUtils {

    public static String getFile(String filename) throws IOException, URISyntaxException {
        Path path = Paths.get(TestUtils.class.getClassLoader().getResource(filename).toURI());
        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(data::append);
        lines.close();
        return data.toString();
    }

}
