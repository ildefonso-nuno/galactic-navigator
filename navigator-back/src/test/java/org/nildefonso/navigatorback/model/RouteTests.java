package org.nildefonso.navigatorback.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTests {
    @Test
    public void testNoArgsConstructor() {
        Route route = new Route();
        route.setId("1");
        route.setStartSystem("Solar System");
        route.setEndSystem("Sirius");
        route.setTravelTime(5);

        assertEquals("1", route.getId());
        assertEquals("Solar System", route.getStartSystem());
        assertEquals("Sirius", route.getEndSystem());
        assertEquals(5, route.getTravelTime());
    }

    @Test
    public void testAllArgsConstructor() {
        Route route = new Route("1", "Solar System", "Sirius", 5);

        assertEquals("1", route.getId());
        assertEquals("Solar System", route.getStartSystem());
        assertEquals("Sirius", route.getEndSystem());
        assertEquals(5, route.getTravelTime());
    }
}
