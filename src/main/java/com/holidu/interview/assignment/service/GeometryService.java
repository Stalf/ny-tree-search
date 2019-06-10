package com.holidu.interview.assignment.service;

import com.holidu.interview.assignment.domain.Boundaries;
import org.springframework.stereotype.Service;

/*
 * This service methods could be static in general, but it is much easier to test stuff with mocked methods
 */
@Service
public class GeometryService {

    private static final double METER_TO_FEET_COEFFICIENT = 3.28084;

    public Boundaries calculateSquare(double x, double y, double sideLength) {
        double x1 = x - sideLength;
        double x2 = x + sideLength;
        double y1 = y - sideLength;
        double y2 = y + sideLength;

        return new Boundaries(x1, x2, y1, y2);
    }

    public boolean isInRadiusFromCenter(double x, double y, double xCenter, double yCenter, double radiusInFeet) {
        return getDistanceFromCenter(x, y, xCenter, yCenter) <= radiusInFeet;
    }

    public double convertMetersToFeet(double radiusInMeters) {
        return METER_TO_FEET_COEFFICIENT * radiusInMeters;
    }

    double getDistanceFromCenter(double x, double y, double xCenter, double yCenter) {
        return Math.hypot(Math.abs(x - xCenter), Math.abs(y - yCenter));
    }

}
