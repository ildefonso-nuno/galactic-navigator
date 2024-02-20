package org.nildefonso.GalacticNavigator.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StarSystemTests {

    @Test
    public void testNoArgsConstructor() {
        StarSystem starSystem = new StarSystem();
        starSystem.setId("A");
        starSystem.setName("Solar System");

        assertEquals("A", starSystem.getId());
        assertEquals("Solar System", starSystem.getName());
    }

    @Test
    public void testAllArgsConstructor() {
        StarSystem starSystem = new StarSystem("A", "Solar System");

        assertEquals("A", starSystem.getId());
        assertEquals("Solar System", starSystem.getName());
    }
}
