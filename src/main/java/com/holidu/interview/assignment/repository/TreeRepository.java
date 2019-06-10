package com.holidu.interview.assignment.repository;

import com.holidu.interview.assignment.domain.Boundaries;
import com.holidu.interview.assignment.domain.CountData;
import com.holidu.interview.assignment.domain.TreeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TreeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeRepository.class);

    private static final String TREES_COUNT_QUERY = "$select=count(tree_id) as count";
    private static final String TREES_SELECT_QUERY = "$select=tree_id as id, spc_common as name, x_sp as x, y_sp as y";
    private static final String ALIVE_TREES_WITH_COORDINATES_FILTER_QUERY = "$where=x_sp between '%f' and '%f' and y_sp between '%f' and '%f' and status = 'Alive'";
    private static final String QUERY_LIMIT_PARAM = "$limit=%d";

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${source.endpoint}")
    private String sourceDataEndpoint;

    //visible for testing
    String getSourceDataEndpoint() {
        return sourceDataEndpoint;
    }

    public List<TreeData> getAliveTreesInSquare(Boundaries boundaries, long count) {
        ResponseEntity<TreeData[]> treesDataResponse = getEntity(
                buildTreesInSquareQueryUrl(
                        boundaries.getX1(), boundaries.getX2(),
                        boundaries.getY1(), boundaries.getY2(), count),
                TreeData[].class);

        return parseTreesData(treesDataResponse);
    }

    public long getAliveTreesCountInSquare(Boundaries boundaries) {
        ResponseEntity<CountData[]> dataCountResponse = getEntity(
                buildTreesInSquareCountQueryUrl(
                        boundaries.getX1(), boundaries.getX2(),
                        boundaries.getY1(), boundaries.getY2()),
                CountData[].class);

        return parseDataCountResponse(dataCountResponse);
    }

    private List<TreeData> parseTreesData(ResponseEntity<TreeData[]> treesDataResponse) {
        return Optional.ofNullable(treesDataResponse.getBody()).map(Arrays::asList).orElse(Collections.emptyList());
    }

    private long parseDataCountResponse(ResponseEntity<CountData[]> dataCountResponse) {
        CountData[] countData = dataCountResponse.getBody();
        if (countData != null && countData.length == 1) {
            return countData[0].getCount();
        } else {
            LOGGER.error("Received incorrect count data: {}", Arrays.toString(countData));
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Received incorrect count data, error has been logged");
        }
    }

    private String buildTreesInSquareQueryUrl(double x1, double x2, double y1, double y2, long limit) {
        return sourceDataEndpoint + "?"
                + TREES_SELECT_QUERY + "&"
                + String.format(ALIVE_TREES_WITH_COORDINATES_FILTER_QUERY, x1, x2, y1, y2) + "&"
                + String.format(QUERY_LIMIT_PARAM, limit);
    }

    private String buildTreesInSquareCountQueryUrl(double x1, double x2, double y1, double y2) {
        return sourceDataEndpoint + "?"
                + TREES_COUNT_QUERY + "&"
                + String.format(ALIVE_TREES_WITH_COORDINATES_FILTER_QUERY, x1, x2, y1, y2);
    }

    private <T> ResponseEntity<T[]> getEntity(String url, Class<T[]> responseType) {
        try {
            return REST_TEMPLATE.getForEntity(url, responseType);
        } catch (RestClientResponseException exc) {
            LOGGER.error("Error calling tree data endpoint. Response body: {}", exc.getResponseBodyAsString(), exc);
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Error calling tree data endpoint. Exception has been logged");
        }
    }
}
