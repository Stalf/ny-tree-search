package com.holidu.interview.assignment.controller;

import com.holidu.interview.assignment.service.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TreesController {

    private final TreeService treeService;

    @Autowired
    public TreesController(TreeService treeService) {
        this.treeService = treeService;
    }

    @GetMapping(
            name = "treesAggregationEndpoint",
            value = "/trees"
    )
    public Map<String, Long> getTreesInRadius(double x, double y, double radius) {
        return treeService.countTreeNamesInRadius(x, y, radius);
    }
}
