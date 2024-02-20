package org.nildefonso.navigatorback.service;

import java.util.List;
import java.util.Map;

public interface GraphService {
    void init();
    void loadGraphData();
    Integer calculateRouteTravelTime(List<String> starSystems);
    Map<List<String>, Integer> findShortestTravelTime(String startSystem, String endSystem);
    Map<List<String>, Integer> findRoutesWithMaxStops(String startSystem, String endSystem, int maxStops);

    Map<List<String>, Integer> findRoutesWithExactStops(String startSystem, String endSystem, int exactStops);
    Map<List<String>, Integer> findRoutesWithinMaxTime(String startSystem, String endSystem, int exactStops);
}
