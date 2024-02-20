package org.nildefonso.GalacticNavigator.repository;

import org.nildefonso.GalacticNavigator.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RouteRepository extends MongoRepository<Route, String> {
    // You can add custom query methods here if needed
}

