package org.nildefonso.GalacticNavigator.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nildefonso.GalacticNavigator.dto.RouteDTO;
import org.nildefonso.GalacticNavigator.model.Route;
import org.nildefonso.GalacticNavigator.model.StarSystem;
import org.nildefonso.GalacticNavigator.repository.RouteRepository;
import org.nildefonso.GalacticNavigator.repository.StarSystemRepository;
import org.nildefonso.GalacticNavigator.service.GraphService;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class NavigatorServiceImplTests {
    @Mock
    private StarSystemRepository starSystemRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private GraphService graphService;

    @InjectMocks
    private NavigatorServiceImpl navigatorService;

    @BeforeEach
    void setUp() {
        Optional<StarSystem> solarSystem = Optional.of(new StarSystem());
        when(starSystemRepository.findByName("Solar System")).thenReturn(solarSystem);

        Optional<StarSystem> siriusSystem = Optional.of(new StarSystem());
        when(starSystemRepository.findByName("Sirius")).thenReturn(siriusSystem);
    }

    @Test
    public void whenCalculateRouteTravelTimeWithValidRoute_thenReturnCorrectTravelTime() {
        // Given
        List<String> starSystems = Arrays.asList("Solar System", "Sirius");
        Integer expectedTravelTime = 5;

        // When
        when(graphService.calculateRouteTravelTime(starSystems)).thenReturn(expectedTravelTime);
        RouteDTO result = navigatorService.calculateRouteTravelTime(starSystems);

        // Then
        assertEquals(expectedTravelTime, result.getTotalTravelTime());
        assertEquals(starSystems, result.getRoute());
    }

    @Test
    public void whenCalculateRouteTravelTimeWithEmptyRouteList_thenReturnNoSuchRoute() {
        // Given
        List<String> starSystems = Arrays.asList("Sirius", "Solar System");

        // When
        when(graphService.calculateRouteTravelTime(starSystems)).thenReturn(-1);
        RouteDTO result = navigatorService.calculateRouteTravelTime(starSystems);

        // Then
        assertEquals(-1, result.getTotalTravelTime());
        assertEquals(Collections.singletonList("NO SUCH ROUTE"), result.getRoute());
    }

    @Test
    void whenFindShortestTravelTimeWithValidRoute_thenReturnCorrectRouteDTO() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        List<String> expectedRoute = List.of(startSystem, endSystem);
        Integer expectedTravelTime = 10;
        Map<List<String>, Integer> mockResponse = new HashMap<>();
        mockResponse.put(expectedRoute, expectedTravelTime);

        when(graphService.findShortestTravelTime(startSystem, endSystem)).thenReturn(mockResponse);

        // When
        RouteDTO result = navigatorService.findShortestTravelTime(startSystem, endSystem);

        // Then
        assertEquals(expectedRoute, result.getRoute(), "The route should match the expected path.");
        assertEquals(expectedTravelTime, result.getTotalTravelTime(), "The travel time should match the expected value.");
    }

    @Test
    void whenFindShortestTravelTimeWithNoRoute_thenReturnNoRouteDTO() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";

        // When
        RouteDTO result = navigatorService.findShortestTravelTime(startSystem, endSystem);

        // Then
        assertEquals(Collections.singletonList("NO SUCH ROUTE"), result.getRoute(), "The route should indicate no such route.");
        assertEquals(Integer.valueOf(-1), result.getTotalTravelTime(), "The travel time should indicate no such route.");
    }

    @Test
    void whenFindRoutesWithMaxStopsWithValidCriteria_thenReturnRoutesList() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int maxStops = 2;
        Map<List<String>, Integer> mockResponse = new HashMap<>();
        mockResponse.put(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), 10);

        when(graphService.findRoutesWithMaxStops(startSystem, endSystem, maxStops)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithMaxStops(startSystem, endSystem, maxStops);

        // Then
        assertEquals(1, results.size(), "There should be one route found.");
        assertEquals(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), results.get(0).getRoute(), "The route path should match the expected.");
        assertEquals(10, results.get(0).getTotalTravelTime(), "The total travel time should match the expected.");
    }

    @Test
    void whenFindRoutesWithMaxStopsWithNoMatchingRoutes_thenReturnEmptyList() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int maxStops = 1;
        Map<List<String>, Integer> mockResponse = Collections.emptyMap();

        when(graphService.findRoutesWithMaxStops(startSystem, endSystem, maxStops)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithMaxStops(startSystem, endSystem, maxStops);

        // Then
        assertEquals(1, results.size(), "There should be one route found.");
        assertEquals(Arrays.asList("NO SUCH ROUTE"), results.get(0).getRoute(), "The route should indicate no such route.");
        assertEquals(Integer.valueOf(-1), results.get(0).getTotalTravelTime(), "The travel time should indicate no such route.");
    }

    @Test
    void whenFindRoutesWithExactStopsWithValidCriteria_thenReturnRoutesList() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int exactStops = 2;
        Map<List<String>, Integer> mockResponse = new HashMap<>();
        mockResponse.put(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), 10);

        when(graphService.findRoutesWithExactStops(startSystem, endSystem, exactStops)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithExactStops(startSystem, endSystem, exactStops);

        // Then
        assertEquals(1, results.size(), "There should be one route found that matches the exact stops criteria.");
        assertEquals(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), results.get(0).getRoute(), "The route path should match the expected.");
        assertEquals(10, results.get(0).getTotalTravelTime(), "The total travel time should match the expected.");
    }

    @Test
    void whenFindRoutesWithExactStopsWithNoMatchingRoutes_thenReturnEmptyList() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int exactStops = 1;
        Map<List<String>, Integer> mockResponse = Collections.emptyMap();

        when(graphService.findRoutesWithExactStops(startSystem, endSystem, exactStops)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithExactStops(startSystem, endSystem, exactStops);

        // Then
        assertEquals(1, results.size(), "There should be one route found.");
        assertEquals(Arrays.asList("NO SUCH ROUTE"), results.get(0).getRoute(), "The route should indicate no such route.");
        assertEquals(Integer.valueOf(-1), results.get(0).getTotalTravelTime(), "The travel time should indicate no such route.");
    }

    @Test
    void whenFindRoutesWithinMaxTimeWithValidCriteria_thenReturnRoutesList() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int maxTime = 15;
        Map<List<String>, Integer> mockResponse = new HashMap<>();
        mockResponse.put(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), 10); // Example route within time

        when(graphService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime);

        // Then
        assertEquals(1, results.size(), "There should be at least one route found within the maximum time.");
        assertEquals(Arrays.asList("Solar System", "Alpha Centauri", "Sirius"), results.get(0).getRoute(), "The route path should match the expected.");
        assertEquals(10, results.get(0).getTotalTravelTime(), "The total travel time should be within the maximum time allowed.");
    }

    @Test
    void whenFindRoutesWithinMaxTimeWithNoMatchingRoutes_thenReturnNoRouteDTO() {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Sirius";
        int maxTime = 1;
        Map<List<String>, Integer> mockResponse = Collections.emptyMap();

        when(graphService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime)).thenReturn(mockResponse);

        // When
        List<RouteDTO> results = navigatorService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime);

        // Then
        assertEquals(1, results.size(), "There should be one route found.");
        assertEquals(Arrays.asList("NO SUCH ROUTE"), results.get(0).getRoute(), "The route should indicate no such route.");
        assertEquals(Integer.valueOf(-1), results.get(0).getTotalTravelTime(), "The travel time should indicate no such route.");
    }
}