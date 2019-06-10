package com.holidu.interview.assignment.controller;

import com.google.common.collect.ImmutableMap;
import com.holidu.interview.assignment.service.TreeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TreesController.class)
public class TreesControllerTest {

    private static final ImmutableMap<String, Long> MOCKED_TREE_NAMES_AGGREGATION = ImmutableMap.of("test1", 3L, "test2", 6L);
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TreeService treeService;

    @Test
    public void shouldReturnAggregatedTreesResponce() throws Exception {
        //given
        when(treeService.countTreeNamesInRadius(anyDouble(), anyDouble(), anyDouble())).thenReturn(
                MOCKED_TREE_NAMES_AGGREGATION
        );

        //when
        ResultActions result = mvc.perform(get("/trees")
                .param("x", "1")
                .param("y", "2")
                .param("radius", "3")
        );

        //then
        result.andExpect(status().is2xxSuccessful())
                .andExpect(content().json("{test1=3, test2=6}"));
    }

}