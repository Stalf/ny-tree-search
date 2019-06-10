package com.holidu.interview.assignment.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.holidu.interview.assignment.TestUtils.getFile;
import static org.assertj.core.api.Assertions.assertThat;

public class DeserialisationTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    public void shouldDeserialiseTreeData() throws IOException, URISyntaxException {
        //given
        String fileData = getFile("testTree.json");
        TreeData expectedObject = new TreeData(180683L, "red maple", 1027431.148, 202756.7687);

        //when
        ObjectReader objectReader = OBJECT_MAPPER.readerFor(TreeData.class);
        Object result = objectReader.readValue(fileData);

        //then
        assertThat(result).isInstanceOf(TreeData.class);
        assertThat((TreeData)result).isEqualTo(expectedObject);
    }

    @Test
    public void shouldDeserialiseCountData() throws IOException, URISyntaxException {
        //given
        String fileData = getFile("testTreeCount.json");
        CountData expectedObject = new CountData(5L);

        //when
        ObjectReader objectReader = OBJECT_MAPPER.readerFor(CountData.class);
        Object result = objectReader.readValue(fileData);

        //then
        assertThat(result).isInstanceOf(CountData.class);
        assertThat((CountData)result).isEqualTo(expectedObject);
    }

}