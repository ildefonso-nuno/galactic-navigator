package org.nildefonso.GalacticNavigator.service.impl;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.nildefonso.GalacticNavigator.model.Route;
import org.nildefonso.GalacticNavigator.model.StarSystem;
import org.nildefonso.GalacticNavigator.repository.RouteRepository;
import org.nildefonso.GalacticNavigator.repository.StarSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@DataMongoTest
public class GraphServiceImplTests {
    @Mock
    private StarSystemRepository starSystemRepository;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private GraphServiceImpl graphService;

    @BeforeEach
    void setUp() {
        // Mock the StarSystemRepository to return the star systems
        List<StarSystem> starSystems = Arrays.asList(
                new StarSystem("A", "Solar System"),
                new StarSystem("B", "Alpha Centauri"),
                new StarSystem("C", "Sirius"),
                new StarSystem("D", "Betelgeuse"),
                new StarSystem("E", "Vega")
        );
        when(starSystemRepository.findAll()).thenReturn(starSystems);

        // Mock the RouteRepository to return the routes
        List<Route> routes = Arrays.asList(
                new Route("1", "Solar System", "Alpha Centauri", 5),
                new Route("2", "Alpha Centauri", "Sirius", 4),
                new Route("3", "Sirius", "Betelgeuse", 8),
                new Route("4", "Betelgeuse", "Sirius", 8),
                new Route("5", "Betelgeuse", "Vega", 6),
                new Route("6", "Solar System", "Betelgeuse", 5),
                new Route("7", "Sirius", "Vega", 2),
                new Route("8", "Vega", "Alpha Centauri", 3),
                new Route("9", "Solar System", "Vega", 7)
        );
        when(routeRepository.findAll()).thenReturn(routes);

        graphService.init();
    }

    @Test
    public void loadGraphDataTest() {
        // Verify the vertices are correctly added to the galaxyMap
        Graph<String, DefaultWeightedEdge> galaxyMap = graphService.getGalaxyMap();

        assertTrue(galaxyMap.containsVertex("Solar System"), "Graph should contain 'Solar System' vertex.");
        assertTrue(galaxyMap.containsVertex("Alpha Centauri"), "Graph should contain 'Alpha Centauri' vertex.");
        assertTrue(galaxyMap.containsVertex("Sirius"), "Graph should contain 'Sirius' vertex.");
        assertTrue(galaxyMap.containsVertex("Betelgeuse"), "Graph should contain 'Betelgeuse' vertex.");
        assertTrue(galaxyMap.containsVertex("Vega"), "Graph should contain 'Vega' vertex.");

        // Verify the edges (routes) are correctly added to the galaxyMap
        assertTrue(galaxyMap.containsEdge("Solar System", "Alpha Centauri"), "Graph should contain an edge from 'Solar System' to 'Alpha Centauri'.");
        assertEquals(5, galaxyMap.getEdgeWeight(galaxyMap.getEdge("Solar System", "Alpha Centauri")), "Edge weight from 'Solar System' to 'Alpha Centauri' should be 5.");
        assertTrue(galaxyMap.containsEdge("Alpha Centauri", "Sirius"), "Graph should contain an edge from 'Alpha Centauri' to 'Sirius'.");
        assertEquals(4, galaxyMap.getEdgeWeight(galaxyMap.getEdge("Alpha Centauri", "Sirius")), "Edge weight from 'Alpha Centauri' to 'Sirius' should be 4.");

        // Verify the total number of vertices and edges matches the expected values
        assertEquals(5, galaxyMap.vertexSet().size(), "Graph should have 5 vertices.");
        assertEquals(9, galaxyMap.edgeSet().size(), "Graph should have 9 edges.");
    }

    @Test
    public void calculateRouteTravelTime_ValidRouteTest() {
        // Given
        List<String> validRoute = Arrays.asList("Solar System", "Alpha Centauri", "Sirius");
        int expectedValidTravelTime = 9;

        // When
        int actualValidTravelTime = graphService.calculateRouteTravelTime(validRoute);

        // Then
        assertEquals(expectedValidTravelTime, actualValidTravelTime, "The travel time for a valid route should match the expected value.");
    }

    @Test
    public void calculateRouteTravelTime_InvalidRouteTest() {
        // Given
        List<String> invalidRoute = Arrays.asList("Solar System", "Sirius");
        int expectedInvalidTravelTime = -1;

        // When
        int actualInvalidTravelTime = graphService.calculateRouteTravelTime(invalidRoute);

        // Then
        assertEquals(expectedInvalidTravelTime, actualInvalidTravelTime, "The travel time for an invalid route should be -1.");
    }

    @Test
    public void findShortestTravelTime_DifferentSystemsTest() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        List<String> expectedRoute = Arrays.asList("Solar System", "Alpha Centauri", "Sirius");
        int expectedTravelTime = 9;

        // When
        Map<List<String>, Integer> result = graphService.findShortestTravelTime(startSystem, endSystem);

        // Then
        assertTrue(result.containsKey(expectedRoute));
        assertEquals(expectedTravelTime, result.get(expectedRoute).intValue());
    }

    @Test
    public void findShortestTravelTime_RoundTripTest() {
        // Given
        String startSystem = "Sirius";
        String endSystem = "Sirius";
        List<String> expectedRoute = Arrays.asList("Sirius", "Vega", "Alpha Centauri", "Sirius");
        int expectedTravelTime = 9; // This value might need adjustment based on the graph setup

        // When
        Map<List<String>, Integer> result = graphService.findShortestTravelTime(startSystem, endSystem);

        // Then
        assertTrue(result.containsKey(expectedRoute));
        assertEquals(expectedTravelTime, result.get(expectedRoute).intValue());
    }

    @Test
    public void findShortestTravelTime_InvalidRouteTest() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Solar System";
        List<String> expectedRoute = Arrays.asList("NO SUCH ROUTE");
        int expectedTravelTime = -1;

        // When
        Map<List<String>, Integer> result = graphService.findShortestTravelTime(startSystem, endSystem);

        // Then
        assertTrue(result.containsKey(expectedRoute));
        assertEquals(expectedTravelTime, result.get(expectedRoute).intValue());
    }

    @Test
    void findRoutesWithMaxStops_ValidRoutesTest() {
        // Given
        String startSystem = "Sirius";
        String endSystem = "Sirius";
        int maxStops = 3;

        List<String> expectedRoute1 = Arrays.asList("Sirius", "Vega", "Alpha Centauri", "Sirius");
        Integer expectedTravelTime1 = 9;
        List<String> expectedRoute2 = Arrays.asList("Sirius", "Betelgeuse", "Sirius");
        Integer expectedTravelTime2 = 16;

        // When
        Map<List<String>, Integer> results = graphService.findRoutesWithMaxStops(startSystem, endSystem, maxStops);

        // Then
        assertNotNull(results, "Results should not be null.");
        assertEquals(2, results.size(), "There should be two routes found.");

        // Check for Route 1
        assertTrue(results.containsKey(expectedRoute1), "Results should contain Route 1.");
        assertEquals(expectedTravelTime1, results.get(expectedRoute1), "Route 1 travel time should match the expected value.");

        // Check for Route 2
        assertTrue(results.containsKey(expectedRoute2), "Results should contain Route 2.");
        assertEquals(expectedTravelTime2, results.get(expectedRoute2), "Route 2 travel time should match the expected value.");
    }

    @Test
    void findRoutesWithMaxStops_InvalidRouteTest() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Solar System";
        int maxStops = 3;

        // When
        Map<List<String>, Integer> results = graphService.findRoutesWithMaxStops(startSystem, endSystem, maxStops);

        // Then
        assertEquals(0, results.size(), "There should be 0 routes found.");
    }

    @Test
    void findRoutesWithExactStops_ValidRoutesTest() {
        // Given
        String startSystem = "Sirius";
        String endSystem = "Sirius";
        int exactStops = 3;

        List<String> expectedRoute = Arrays.asList("Sirius", "Vega", "Alpha Centauri", "Sirius");
        Integer expectedTravelTime = 9;

        // When
        Map<List<String>, Integer> results = graphService.findRoutesWithExactStops(startSystem, endSystem, exactStops);

        // Then
        assertNotNull(results, "Results should not be null.");
        assertEquals(1, results.size(), "There should be two routes found.");

        // Check for Route
        assertTrue(results.containsKey(expectedRoute), "Results should contain Route.");
        assertEquals(expectedTravelTime, results.get(expectedRoute), "Route travel time should match the expected value.");
    }

    @Test
    void findRoutesWithExactStops_InvalidRouteTest() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Solar System";
        int exactStops = 3;

        // When
        Map<List<String>, Integer> results = graphService.findRoutesWithExactStops(startSystem, endSystem, exactStops);

        // Then
        assertEquals(0, results.size(), "There should be 0 routes found.");
    }

    @Test
    void findRoutesWithinMaxTime_ValidRoutesTest() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int maxTime = 15;

        // Expected routes and their travel times
        Map<List<String>, Integer> expectedRoutes = new HashMap<>();
        expectedRoutes.put(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), 9);
        expectedRoutes.put(Arrays.asList("Solar System", "Betelgeuse", "Sirius"), 13);
        expectedRoutes.put(Arrays.asList("Solar System", "Vega", "Alpha Centauri", "Sirius"), 14);

        // When
        Map<List<String>, Integer> results = graphService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime);

        // Then
        assertNotNull(results, "Results should not be null.");
        assertEquals(expectedRoutes.size(), results.size(), "Expected number of routes found within max time.");

        // Check each expected route is present with correct travel time
        expectedRoutes.forEach((expectedPath, expectedTime) -> {
            assertTrue(results.containsKey(expectedPath), "Expected path should be present in results.");
            assertEquals(expectedTime, results.get(expectedPath), "Travel time for path should match expected time.");
        });
    }

}