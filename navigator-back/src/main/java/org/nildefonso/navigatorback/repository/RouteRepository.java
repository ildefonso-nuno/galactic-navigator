package org.nildefonso.navigatorback.repository;

import org.nildefonso.navigatorback.model.Route;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RouteRepository extends MongoRepository<Route, String> {
    // You can add custom query methods here if needed
}

