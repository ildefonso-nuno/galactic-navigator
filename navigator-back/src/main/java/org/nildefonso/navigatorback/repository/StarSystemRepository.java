package org.nildefonso.navigatorback.repository;

import org.nildefonso.navigatorback.model.StarSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StarSystemRepository extends MongoRepository<StarSystem, String> {
    Optional<StarSystem> findByName(String name);
}