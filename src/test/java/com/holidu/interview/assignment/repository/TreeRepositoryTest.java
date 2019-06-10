package com.holidu.interview.assignment.repository;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.ContentTypeHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.google.common.collect.ImmutableList;
import com.holidu.interview.assignment.TestUtils;
import com.holidu.interview.assignment.domain.Boundaries;
import com.holidu.interview.assignment.domain.TreeData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TreeRepositoryTest {

    public static final String TEST_1 = "test1";
    public static final String TEST_2 = "test2";
    public static final ImmutableList<TreeData> MOCKED_TREE_DATA_LIST = ImmutableList.of(
            new TreeData(1L, TEST_1, 1, 2),
            new TreeData(2L, TEST_1, 2, 1),
            new TreeData(3L, TEST_2, 0, 0),
            new TreeData(4L, TEST_2, 2, 2),
            new TreeData(5L, TEST_2, -1, -1)
    );
    public static final String SELECT_COUNT_REGEXP = "/data\\?\\$select=count.*";
    public static final String SELECT_DATA_REGEXP = "/data\\?\\$select=tree_id.*";

    @Autowired
    private TreeRepository treeRepository;

    private WireMockServer wireMockServer;

    @Before
    public void setUp() {
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void shouldSetRepositoryUrlProperty() {
        //when
        String sourceDataEndpoint = treeRepository.getSourceDataEndpoint();

        //then
        assertThat(sourceDataEndpoint).isEqualTo("http://localhost:8080/data");
    }

    @Test
    public void shouldFetchTreeCount() throws IOException, URISyntaxException {
        //given
        String mockResponse = TestUtils.getFile("treeCountResponse.json");
        wireMockServer.stubFor(get(urlMatching(SELECT_COUNT_REGEXP))
                .willReturn(aResponse().withBody(mockResponse)
                        .withHeader(ContentTypeHeader.KEY, "application/json")
                )
        );

        //when
        long treesCountInSquare = treeRepository.getAliveTreesCountInSquare(new Boundaries(1, 2, 3, 4));

        //then
        assertThat(treesCountInSquare).isEqualTo(5);
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowExceptionOnEmptyTreeCountResponse() {
        //given
        wireMockServer.stubFor(get(urlMatching(SELECT_COUNT_REGEXP))
                .willReturn(aResponse().withBody("")
                        .withHeader(ContentTypeHeader.KEY, "application/json")
                )
        );

        //when
        treeRepository.getAliveTreesCountInSquare(new Boundaries(1, 2, 3, 4));

        //then exception expected
        assert false;
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowExceptionOnNonSuccessResponse() {
        //given
        wireMockServer.stubFor(get(urlMatching(SELECT_COUNT_REGEXP))
                .willReturn(aResponse().withStatus(500))
        );

        //when
        treeRepository.getAliveTreesCountInSquare(new Boundaries(1, 2, 3, 4));

        //then exception expected
        assert false;
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowExceptionOnConnectionError() {
        //given no mocking

        //when
        treeRepository.getAliveTreesCountInSquare(new Boundaries(1, 2, 3, 4));

        //then exception expected
        assert false;
    }

    @Test
    public void shouldFetchTreeData() throws IOException, URISyntaxException {
        //given
        String mockResponse = TestUtils.getFile("treeListResponse.json");
        wireMockServer.stubFor(get(urlMatching(SELECT_DATA_REGEXP))
                .willReturn(aResponse().withBody(mockResponse)
                        .withHeader(ContentTypeHeader.KEY, "application/json")
                )
        );

        //when
        List<TreeData> treeDataList = treeRepository.getAliveTreesInSquare(new Boundaries(1, 2, 3, 4), 5);

        //then
        assertThat(treeDataList).hasSize(5);
        assertThat(treeDataList).containsAll(MOCKED_TREE_DATA_LIST);
    }
}