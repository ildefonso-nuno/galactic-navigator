package org.nildefonso.GalacticNavigator.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.nildefonso.GalacticNavigator.dto.RouteDTO;
import org.nildefonso.GalacticNavigator.model.Route;
import org.nildefonso.GalacticNavigator.model.StarSystem;
import org.nildefonso.GalacticNavigator.repository.RouteRepository;
import org.nildefonso.GalacticNavigator.repository.StarSystemRepository;
import org.nildefonso.GalacticNavigator.service.NavigatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NavigatorControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private NavigatorService navigatorService;
    @MockBean
    private RouteRepository routeRepo;
    @MockBean
    private StarSystemRepository starSystemRepo;

    @Test
    void getAllRoutes_ShouldReturnNotNull() throws Exception {
        // Given
        Route route1 = new Route();
        Route route2 = new Route();
        List<Route> allRoutes = Arrays.asList(route1, route2);
        given(routeRepo.findAll()).willReturn(allRoutes);

        // When & Then
        mockMvc.perform(get("/api/navigator/routes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(notNullValue())))
                .andExpect(jsonPath("$[1]", is(notNullValue())));
    }

    @Test
    void getAllStarSystems_ShouldReturnNotNull() throws Exception {
        // Given
        StarSystem starSystem1 = new StarSystem(); // Assuming Route has proper constructor or setters to set test data
        StarSystem starSystem2 = new StarSystem();
        List<StarSystem> allStarSystems = Arrays.asList(starSystem1, starSystem2);
        given(starSystemRepo.findAll()).willReturn(allStarSystems);

        // When & Then
        mockMvc.perform(get("/api/navigator/starsystems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is(notNullValue())))
                .andExpect(jsonPath("$[1]", is(notNullValue())));
    }

    @Test
    void calculateRouteTravelTime_ShouldReturnRouteDTO() throws Exception {
        // Given
        List<String> starSystems = Arrays.asList("Solar System", "Alpha Centauri");
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRoute(starSystems);
        routeDTO.setTotalTravelTime(5);

        Mockito.when(navigatorService.calculateRouteTravelTime(starSystems)).thenReturn(routeDTO);

        // When & Then
        mockMvc.perform(get("/api/navigator/calculateRouteTravelTime")
                        .param("starSystems", "Solar System", "Alpha Centauri"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.route", is(starSystems)))
                .andExpect(jsonPath("$.totalTravelTime", is(5)));
    }

    @Test
    void findShortestTravelTime_ShouldReturnRouteDTO() throws Exception {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Alpha Centauri";
        List<String> starSystems = Arrays.asList("Solar System","Sirius","Alpha Centauri");
        RouteDTO routeDTO = new RouteDTO();
        routeDTO.setRoute(starSystems);
        routeDTO.setTotalTravelTime(5);

        Mockito.when(navigatorService.findShortestTravelTime(startSystem, endSystem)).thenReturn(routeDTO);

        // When & Then
        mockMvc.perform(get("/api/navigator/findShortestTravelTime")
                        .param("startSystem", "Solar System")
                        .param("endSystem", "Alpha Centauri"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.route", is(starSystems)))
                .andExpect(jsonPath("$.totalTravelTime", is(5)));
    }

    @Test
    void findRoutesWithMaxStops_ShouldReturnRouteDTO() throws Exception {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Alpha Centauri";
        int maxStops = 5;
        List<String> starSystems = Arrays.asList("Solar System", "Sirius", "Alpha Centauri");
        RouteDTO routeDTO = new RouteDTO(starSystems, 5);
        List<RouteDTO> expectedRoutes = List.of(routeDTO);

        Mockito.when(navigatorService.findRoutesWithMaxStops(startSystem, endSystem, maxStops)).thenReturn(expectedRoutes);

        // When & Then
        mockMvc.perform(get("/api/navigator/findRoutesWithMaxStops")
                        .param("startSystem", startSystem)
                        .param("endSystem", endSystem)
                        .param("maxStops", String.valueOf(maxStops)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].route", is(starSystems)))
                .andExpect(jsonPath("$[0].totalTravelTime", is(5)));
    }

    @Test
    void findRoutesWithExactStops_ShouldReturnRouteDTO() throws Exception {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Alpha Centauri";
        int exactStops = 5;
        List<String> starSystems = Arrays.asList("Solar System", "Sirius", "Alpha Centauri");
        RouteDTO routeDTO = new RouteDTO(starSystems, 5);
        List<RouteDTO> expectedRoutes = List.of(routeDTO);

        Mockito.when(navigatorService.findRoutesWithExactStops(startSystem, endSystem, exactStops)).thenReturn(expectedRoutes);

        // When & Then
        mockMvc.perform(get("/api/navigator/findRoutesWithExactStops")
                        .param("startSystem", startSystem)
                        .param("endSystem", endSystem)
                        .param("exactStops", String.valueOf(exactStops)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].route", is(starSystems)))
                .andExpect(jsonPath("$[0].totalTravelTime", is(5)));
    }

    @Test
    void findRoutesWithinMaxTime_ShouldReturnRouteDTO() throws Exception {
        // Given
        String startSystem = "Solar System";
        String endSystem = "Alpha Centauri";
        int maxTime = 10;
        List<String> starSystems = Arrays.asList("Solar System", "Sirius", "Alpha Centauri");
        RouteDTO routeDTO = new RouteDTO(starSystems, 10);
        List<RouteDTO> expectedRoutes = List.of(routeDTO);

        Mockito.when(navigatorService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime)).thenReturn(expectedRoutes);

        // When & Then
        mockMvc.perform(get("/api/navigator/findRoutesWithinMaxTime")
                        .param("startSystem", startSystem)
                        .param("endSystem", endSystem)
                        .param("maxTime", String.valueOf(maxTime)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].route", is(starSystems)))
                .andExpect(jsonPath("$[0].totalTravelTime", is(10)));
    }
}