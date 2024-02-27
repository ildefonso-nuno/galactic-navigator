package org.nildefonso.navigatorback.service.impl;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.nildefonso.navigatorback.model.Route;
import org.nildefonso.navigatorback.model.StarSystem;
import org.nildefonso.navigatorback.repository.RouteRepository;
import org.nildefonso.navigatorback.repository.StarSystemRepository;
import org.nildefonso.navigatorback.service.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GraphServiceImpl implements GraphService {
    private final StarSystemRepository starSystemRepository;
    private final RouteRepository routeRepository;
    private Graph<String, DefaultWeightedEdge> galaxyMap;

    @Autowired
    public GraphServiceImpl(StarSystemRepository starSystemRepository, RouteRepository routeRepository) {
        this.starSystemRepository = starSystemRepository;
        this.routeRepository = routeRepository;
    }

    @PostConstruct
    public void init() {
        galaxyMap = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        loadGraphData();
    }

    @Override
    public void loadGraphData() {
        // Fetch star systems and routes from MongoDB
        List<StarSystem> systems = starSystemRepository.findAll();
        systems.forEach(system -> galaxyMap.addVertex(system.getName()));

        List<Route> routes = routeRepository.findAll();
        routes.forEach(route -> {
            DefaultWeightedEdge edge = galaxyMap.addEdge(route.getStartSystem(), route.getEndSystem());
            if (edge != null) { // Ensure the edge is not null in case of invalid data
                galaxyMap.setEdgeWeight(edge, route.getTravelTime());
            }
        });
    }

    public Graph<String, DefaultWeightedEdge> getGalaxyMap() {
        return this.galaxyMap;
    }

    private int getTravelTime(String startSystem, String endSystem) {
        // Attempt to find an edge connecting the two systems in the galaxy map
        DefaultWeightedEdge edge = galaxyMap.getEdge(startSystem, endSystem);

        return edge != null ? (int) galaxyMap.getEdgeWeight(edge) : -1;
    }

    @Override
    public Integer calculateRouteTravelTime(List<String> starSystems) {
        if (starSystems.size() < 2) {
            return -1;
        }

        int totalTravelTime = 0;
        for (int i = 0; i < starSystems.size() - 1; i++) {
            int segmentTravelTime = getTravelTime(starSystems.get(i), starSystems.get(i + 1));
            if (segmentTravelTime == -1) {
                return -1;
            }
            totalTravelTime += segmentTravelTime;
        }

        return totalTravelTime;
    }

    private Map<List<String>, Integer> findShortestRoundTrip(String startSystem) {
        Map<List<String>, Integer> routeWithTravelTime = new HashMap<>();

        Set<DefaultWeightedEdge> edges = galaxyMap.outgoingEdgesOf(startSystem);
        int shortestTravelTime = Integer.MAX_VALUE;
        List<String> shortestRoute = new ArrayList<>();

        for (DefaultWeightedEdge edge : edges) {
            String adjacentNode = galaxyMap.getEdgeTarget(edge);
            int startToEdgeTravelTime = getTravelTime(startSystem, adjacentNode);

            GraphPath<String, DefaultWeightedEdge> edgeToEndShortestPath = DijkstraShortestPath.findPathBetween(galaxyMap, adjacentNode, startSystem);

            if (edgeToEndShortestPath != null) {
                int edgeToEndShortestTravelTime = (int) edgeToEndShortestPath.getWeight();
                int totalTravelTime = startToEdgeTravelTime + edgeToEndShortestTravelTime;
                if (totalTravelTime < shortestTravelTime ) {
                    shortestTravelTime = totalTravelTime;
                    // Clear previous entries
                    shortestRoute.clear();
                    routeWithTravelTime.clear();
                    shortestRoute.add(startSystem);
                    shortestRoute.addAll(edgeToEndShortestPath.getVertexList());
                    routeWithTravelTime.put(shortestRoute, totalTravelTime);
                }
            }
        }

        if (routeWithTravelTime.isEmpty()) routeWithTravelTime.put(Collections.singletonList("NO SUCH ROUTE"), -1);

        return routeWithTravelTime;
    }

    @Override
    public Map<List<String>, Integer> findShortestTravelTime(String startSystem, String endSystem) {
        Map<List<String>, Integer> routeWithTravelTime = new HashMap<>();

        if (startSystem.equals(endSystem)) {
            routeWithTravelTime = findShortestRoundTrip(startSystem);
        } else {
            GraphPath<String, DefaultWeightedEdge> path = DijkstraShortestPath.findPathBetween(galaxyMap, startSystem, endSystem);

            if (path != null) {
                List<String> route = path.getVertexList();
                int travelTime = (int) path.getWeight();
                routeWithTravelTime.put(route, travelTime);
            }
        }

        return routeWithTravelTime;
    }

    private List<GraphPath<String, DefaultWeightedEdge>> getAllDirectedPaths(String startSystem, String endSystem, int stops) {
        AllDirectedPaths<String, DefaultWeightedEdge> allDirectedPaths = new AllDirectedPaths<>(galaxyMap);
        return allDirectedPaths.getAllPaths(startSystem, endSystem, false, stops);
    }

    @Override
    public Map<List<String>, Integer> findRoutesWithMaxStops(String startSystem, String endSystem, int maxStops) {
        return getAllDirectedPaths(startSystem, endSystem, maxStops)
                .stream()
                .filter(path -> path.getWeight() != 0) //to exclude not travelling case
                .collect(Collectors.toMap(GraphPath::getVertexList, path -> (int) path.getWeight()));
    }

    @Override
    public Map<List<String>, Integer> findRoutesWithExactStops(String startSystem, String endSystem, int exactStops) {
        return getAllDirectedPaths(startSystem, endSystem, exactStops)
                .stream()
                .filter(path -> path.getLength() == exactStops) // Assuming path.getLength() gives the number of stops.
                .collect(Collectors.toMap(GraphPath::getVertexList, path -> (int) path.getWeight()));
    }

    public Map<List<String>, Integer> findRoutesWithinMaxTime(String startSystem, String endSystem, int maxTime) {
        Map<List<String>, Integer> routeWithTravelTime = new HashMap<>();

        List<List<String>> paths = new ArrayList<>();
        explorePathsToWeightLimit(galaxyMap, startSystem, endSystem, new ArrayList<>(), paths, 0, maxTime);

        for (List<String> path : paths) {
            if(path.size()!=1){
                int travelTime = calculateRouteTravelTime(path);
                routeWithTravelTime.put(path, travelTime);
            }
        }

        return routeWithTravelTime;
    }

    private void explorePathsToWeightLimit(Graph<String, DefaultWeightedEdge> graph, String currentVertex, String targetVertex, List<String> currentPath, List<List<String>> paths, double currentWeight, double weightLimit) {
        currentPath.add(currentVertex);

        if (currentVertex.equals(targetVertex)) {
            paths.add(new ArrayList<>(currentPath));
        }

        if (currentWeight < weightLimit) {
            for (DefaultWeightedEdge edge : graph.outgoingEdgesOf(currentVertex)) {
                String target = graph.getEdgeTarget(edge);
                double edgeWeight = graph.getEdgeWeight(edge);

                if (currentWeight + edgeWeight <= weightLimit) {
                    explorePathsToWeightLimit(graph, target, targetVertex, currentPath, paths, currentWeight + edgeWeight, weightLimit);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1); // Backtrack
    }

}
