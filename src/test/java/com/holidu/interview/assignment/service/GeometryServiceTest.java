package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.domain.Boundaries;
import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GeometryServiceTest {

    private static final Offset<Double> DOUBLE_PRECISION_OFFSET = Offset.offset(1e-4);
    private GeometryService geometryService = new GeometryService();

    @Test
    public void shouldConvertMetersToFeet() {
        //when
        double feetValue = geometryService.convertMetersToFeet(10);

        //then
        assertThat(feetValue).isEqualTo(32.8084, DOUBLE_PRECISION_OFFSET);
    }

    @Test
    public void shouldCalculateCorrectDistanceFromCenter() {
        //when
        double distance = geometryService.getDistanceFromCenter(10, 5, 0, 0);

        //then
        assertThat(distance).isEqualTo(11.1803, DOUBLE_PRECISION_OFFSET);
    }

    @Test
    public void shouldCalculateCorrectDistanceFromCenterWithNegativeValues() {
        //when
        double distance = geometryService.getDistanceFromCenter(-5, 5, -1, -2);

        //then
        assertThat(distance).isEqualTo(8.0623, DOUBLE_PRECISION_OFFSET);
    }

    @Test
    public void shouldDetectThatPointIsInCircle() {
        //when
        boolean result = geometryService.isInRadiusFromCenter(1, 1, 0, 0, 1.42);

        //then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldDetectThatPointIsNotInCircle() {
        //when
        boolean result = geometryService.isInRadiusFromCenter(-1, -1, 0, 0, 1);

        //then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldCreateBoundaries() {
        //when
        Boundaries boundaries = geometryService.calculateSquare(1, 2, 3);

        assertThat(boundaries).isEqualTo(new Boundaries(-2, 4, -1, 5));
    }


}