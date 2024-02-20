package org.nildefonso.GalacticNavigator.service;

import org.nildefonso.GalacticNavigator.dto.RouteDTO;

import java.util.List;

public interface NavigatorService {

    RouteDTO calculateRouteTravelTime(List<String> starSystems);
    RouteDTO findShortestTravelTime(String startSystem, String endSystem);

    List<RouteDTO> findRoutesWithMaxStops(String startSystem, String endSystem, int maxStops);

    List<RouteDTO> findRoutesWithExactStops(String startSystem, String endSystem, int exactStops);

    List<RouteDTO> findRoutesWithinMaxTime(String startSystem, String endSystem, int maxTime);
}
