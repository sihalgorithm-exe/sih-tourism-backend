package com.sih.tourism.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HaversineCalculatorTest {

    @Test
    void distanceBetweenSamePointIsZero() {
        double distance = HaversineCalculator.distanceInMeters(16.5062, 80.6480, 16.5062, 80.6480);
        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void distanceBetweenKnownPointsIsApproximatelyCorrect() {
        // Roughly 1 degree of latitude ~ 111 km
        double distance = HaversineCalculator.distanceInMeters(0.0, 0.0, 1.0, 0.0);
        assertTrue(distance > 110000 && distance < 112000,
                "Expected ~111km, got " + distance + "m");
    }

    @Test
    void distanceIsSymmetric() {
        double d1 = HaversineCalculator.distanceInMeters(16.50, 80.64, 16.52, 80.66);
        double d2 = HaversineCalculator.distanceInMeters(16.52, 80.66, 16.50, 80.64);
        assertEquals(d1, d2, 0.0001);
    }
}
