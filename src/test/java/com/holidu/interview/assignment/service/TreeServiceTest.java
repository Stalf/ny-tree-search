package com.holidu.interview.assignment.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.holidu.interview.assignment.domain.Boundaries;
import com.holidu.interview.assignment.domain.TreeData;
import com.holidu.interview.assignment.repository.TreeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static com.holidu.interview.assignment.repository.TreeRepositoryTest.MOCKED_TREE_DATA_LIST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TreeServiceTest {

    @Mock
    private TreeRepository treeRepository;
    @Mock
    private GeometryService geometryService;
    private TreeService treeService;

    @Before
    public void setUp() {
        treeService = new TreeService(treeRepository, geometryService);
        when(geometryService.convertMetersToFeet(anyDouble())).thenAnswer(invocation -> invocation.getArgument(0));
        when(geometryService.calculateSquare(anyDouble(), anyDouble(), anyDouble())).thenReturn(new Boundaries(-1, 1, -1, 1));
        when(treeRepository.getAliveTreesCountInSquare(any(Boundaries.class))).thenReturn(5L);
    }

    @Test
    public void shouldCountTreesInRadius() {
        //given
        when(treeRepository.getAliveTreesInSquare(any(Boundaries.class), anyLong())).thenReturn(MOCKED_TREE_DATA_LIST);
        when(geometryService.isInRadiusFromCenter(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(true, false, true, true, false);

        //when
        Map<String, Long> result = treeService.countTreeNamesInRadius(0, 0, 1);

        //then
        assertThat(result).hasSize(2);
        assertThat(result).containsAllEntriesOf(ImmutableMap.of("test1", 1L, "test2", 2L));
    }

    @Test
    public void shouldIgnoreTreeWithoutName() {
        //given
        when(treeRepository.getAliveTreesInSquare(any(Boundaries.class), anyLong())).thenReturn(
                ImmutableList.of(new TreeData(1L, null, 0, 0)));

        //when
        Map<String, Long> result = treeService.countTreeNamesInRadius(0, 0, 1);

        //then
        assertThat(result).hasSize(0);
        verify(geometryService, times(0)).isInRadiusFromCenter(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

}