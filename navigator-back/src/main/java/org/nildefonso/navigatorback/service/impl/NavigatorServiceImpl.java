package org.nildefonso.navigatorback.service.impl;

import lombok.AllArgsConstructor;
import org.nildefonso.navigatorback.dto.RouteDTO;
import org.nildefonso.navigatorback.exception.StarSystemNotValidException;
import org.nildefonso.navigatorback.repository.StarSystemRepository;
import org.nildefonso.navigatorback.service.GraphService;
import org.nildefonso.navigatorback.service.NavigatorService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class NavigatorServiceImpl implements NavigatorService {

    private final GraphService graphService;

    private final StarSystemRepository starSystemRepo;
    @Override
    public RouteDTO calculateRouteTravelTime(List<String> starSystems) {
        if (starSystems.isEmpty() || starSystems.size() < 2) {
            throw new StarSystemNotValidException("Invalid route: Must include at least two star systems.");
        }

        for (String system : starSystems) {
            if (starSystemRepo.findByName(system).isEmpty()) {
                throw new StarSystemNotValidException("Star system not found: " + system);
            }
        }

        Integer travelTime = graphService.calculateRouteTravelTime(starSystems);
        if (travelTime == -1) {
            return new RouteDTO(Collections.singletonList("NO SUCH ROUTE"), -1);
        }

        return new RouteDTO(starSystems, travelTime);
    }

    @Override
    public RouteDTO findShortestTravelTime(String startSystem, String endSystem) {
        if (starSystemRepo.findByName(startSystem).isEmpty()) {
            throw new StarSystemNotValidException("Start system not found: " + startSystem);
        }
        if (starSystemRepo.findByName(endSystem).isEmpty()) {
            throw new StarSystemNotValidException("End system not found: " + endSystem);
        }

        Map<List<String>, Integer> routeWithTravelTime = graphService.findShortestTravelTime(startSystem, endSystem);

        if (!routeWithTravelTime.isEmpty()) {
            Map.Entry<List<String>, Integer> entry = routeWithTravelTime.entrySet().iterator().next();

            return new RouteDTO(entry.getKey(), entry.getValue());
        } else {
            return new RouteDTO(Collections.singletonList("NO SUCH ROUTE"), -1);
        }
    }

    @Override
    public List<RouteDTO> findRoutesWithMaxStops(String startSystem, String endSystem, int maxStops) {
        if (starSystemRepo.findByName(startSystem).isEmpty()) {
            throw new StarSystemNotValidException("Start system not found: " + startSystem);
        }
        if (starSystemRepo.findByName(endSystem).isEmpty()) {
            throw new StarSystemNotValidException("End system not found: " + endSystem);
        }

        Map<List<String>, Integer> routesWithTravelTime = graphService.findRoutesWithMaxStops(startSystem, endSystem, maxStops);

        return mapToRouteDTOS(routesWithTravelTime);
    }

    @Override
    public List<RouteDTO> findRoutesWithExactStops(String startSystem, String endSystem, int exactStops) {
        if (starSystemRepo.findByName(startSystem).isEmpty()) {
            throw new StarSystemNotValidException("Start system not found: " + startSystem);
        }
        if (starSystemRepo.findByName(endSystem).isEmpty()) {
            throw new StarSystemNotValidException("End system not found: " + endSystem);
        }

        Map<List<String>, Integer> routesWithTravelTime = graphService.findRoutesWithExactStops(startSystem, endSystem, exactStops);

        return mapToRouteDTOS(routesWithTravelTime);
    }

    @Override
    public List<RouteDTO> findRoutesWithinMaxTime(String startSystem, String endSystem, int maxTime) {
        if (starSystemRepo.findByName(startSystem).isEmpty()) {
            throw new StarSystemNotValidException("Start system not found: " + startSystem);
        }
        if (starSystemRepo.findByName(endSystem).isEmpty()) {
            throw new StarSystemNotValidException("End system not found: " + endSystem);
        }

        Map<List<String>, Integer> routesWithTravelTime = graphService.findRoutesWithinMaxTime(startSystem, endSystem, maxTime);

        return mapToRouteDTOS(routesWithTravelTime);
    }

    private List<RouteDTO> mapToRouteDTOS(Map<List<String>, Integer> routesWithTravelTime) {
        List<RouteDTO> routeDTOs = new ArrayList<>();

        for (Map.Entry<List<String>, Integer> entry : routesWithTravelTime.entrySet()) {
            RouteDTO dto = new RouteDTO(entry.getKey(), entry.getValue());
            routeDTOs.add(dto);
        }

        if (routeDTOs.isEmpty()) routeDTOs.add(new RouteDTO(Collections.singletonList("NO SUCH ROUTE"), -1));

        return routeDTOs;
    }
}
