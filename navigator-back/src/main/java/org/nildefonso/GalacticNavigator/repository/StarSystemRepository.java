package org.nildefonso.GalacticNavigator.repository;

import org.nildefonso.GalacticNavigator.model.StarSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StarSystemRepository extends MongoRepository<StarSystem, String> {
    Optional<StarSystem> findByName(String name);
}