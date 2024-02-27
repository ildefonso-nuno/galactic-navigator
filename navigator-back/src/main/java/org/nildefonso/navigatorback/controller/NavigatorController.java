package org.nildefonso.navigatorback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.nildefonso.navigatorback.dto.RouteDTO;
import org.nildefonso.navigatorback.model.Route;
import org.nildefonso.navigatorback.model.StarSystem;
import org.nildefonso.navigatorback.repository.RouteRepository;
import org.nildefonso.navigatorback.repository.StarSystemRepository;
import org.nildefonso.navigatorback.service.NavigatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(
        name = "Galactic Navigator Service",
        description = "CRUD REST APIs"
)
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/navigator")
public class NavigatorController{
    private final NavigatorService navigatorService;

    private final RouteRepository routeRepo;

    private final StarSystemRepository starSystemRepo;

    //Get all Routes RESTAPI
    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeRepo.findAll());
    }

    //Get all StarSystems RESTAPI
    @GetMapping("/starsystems")
    public ResponseEntity<List<StarSystem>> getAllStarSystems() {
        return ResponseEntity.ok(starSystemRepo.findAll());
    }

    //get Route Travel Time RESTAPI
    @Operation(
            summary = "Get Route Travel Time REST API",
            description = "Get route travel time for a list of star systems"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/calculateRouteTravelTime")
    public ResponseEntity<RouteDTO> calculateRouteTravelTime(@Valid @RequestParam List<String> starSystems) {
        return ResponseEntity.ok(navigatorService.calculateRouteTravelTime(starSystems));
    }

    //Find the shortest route RESTAPI
    @Operation(
            summary = "Find the shortest travel time REST API",
            description = "Get the shortest travel time between two star-systems"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/findShortestTravelTime")
    public ResponseEntity<RouteDTO> findShortestTravelTime(@RequestParam String startSystem, @RequestParam String endSystem) {
        return ResponseEntity.ok(navigatorService.findShortestTravelTime(startSystem, endSystem));
    }

    //Find Routes with Max Stops RESTAPI
    @Operation(
            summary = "Find Routes with Max Stops REST API",
            description = "Get a list of Routes between two star-systems with max stops"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/findRoutesWithMaxStops")
    public ResponseEntity<List<RouteDTO>> findRoutesWithMaxStops(@Valid @RequestParam String startSystem, @Valid @RequestParam String endSystem, @Valid @RequestParam int maxStops) {
        return ResponseEntity.ok(navigatorService.findRoutesWithMaxStops(startSystem, endSystem, maxStops));
    }


    //Find Routes with Exact Stops RESTAPI
    @Operation(
            summary = "Find Routes with Exact Stops REST API",
            description = "Get a list of Routes between two star-systems with exact stops"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/findRoutesWithExactStops")
    public ResponseEntity<List<RouteDTO>> findRoutesWithExactStops(@Valid @RequestParam String startSystem, @Valid @RequestParam String endSystem, @Valid @RequestParam int exactStops) {
        return ResponseEntity.ok(navigatorService.findRoutesWithExactStops(startSystem, endSystem, exactStops));
    }

    //Find routes within maximum time RESTAPI
    @Operation(
            summary = "Find routes within maximum time REST API",
            description = "Get all the routes between two star-systems within a maximum time"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 SUCCESS"
    )
    @GetMapping("/findRoutesWithinMaxTime")
    public ResponseEntity<List<RouteDTO>> findRoutesWithinMaxTime(@Valid @RequestParam String startSystem, @Valid @RequestParam String endSystem, @Valid @RequestParam int maxTime) {
        return ResponseEntity.ok(navigatorService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime));
    }
}
