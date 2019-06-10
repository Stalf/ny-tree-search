package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.domain.Boundaries;
import com.holidu.interview.assignment.domain.TreeData;
import com.holidu.interview.assignment.repository.TreeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TreeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TreeService.class);

    private final TreeRepository treeRepository;
    private final GeometryService geometryService;

    @Autowired
    public TreeService(TreeRepository treeRepository, GeometryService geometryService) {
        this.treeRepository = treeRepository;
        this.geometryService = geometryService;
    }

    public Map<String, Long> countTreeNamesInRadius(double x, double y, double radiusInMeters) {

        double radiusInFeet = geometryService.convertMetersToFeet(radiusInMeters);
        Boundaries boundaries = geometryService.calculateSquare(x, y, radiusInFeet);

        long aliveTreesCountInSquare = treeRepository.getAliveTreesCountInSquare(boundaries);
        LOGGER.debug("{} trees matched query", aliveTreesCountInSquare);

        List<TreeData> treeDataList = treeRepository.getAliveTreesInSquare(boundaries, aliveTreesCountInSquare);
        LOGGER.debug("Fetched {} tree records", treeDataList.size());

        return treeDataList.stream()
                .filter(treeData -> treeData.getName() != null)
                .filter(treeData -> geometryService.isInRadiusFromCenter(treeData.getX(), treeData.getY(), x, y, radiusInFeet))
                .collect(Collectors.groupingBy(TreeData::getName, Collectors.counting()));
    }

}
