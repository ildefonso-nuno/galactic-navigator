package org.nildefonso.GalacticNavigator.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RouteDTOTests {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testNullRoute() {
        RouteDTO dto = new RouteDTO(null, 10);
        Set<ConstraintViolation<RouteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("Route should not be null or empty", violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidTravelTime() {
        RouteDTO dto = new RouteDTO(Collections.singletonList("Sirius"), -5);
        Set<ConstraintViolation<RouteDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals("Total Travel Time should not be less than -1", violations.iterator().next().getMessage());
    }

    @Test
    void testValidDto() {
        RouteDTO dto = new RouteDTO(Collections.singletonList("Sirius"), 10);
        Set<ConstraintViolation<RouteDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testNoArgsConstructor() {
        RouteDTO dto = new RouteDTO();
        dto.setRoute(Collections.singletonList("Sirius"));
        dto.setTotalTravelTime(15);

        assertEquals(Collections.singletonList("Sirius"), dto.getRoute());
        assertEquals(15, dto.getTotalTravelTime());
    }

    @Test
    void testAllArgsConstructor() {
        List<String> route = Collections.singletonList("Sirius");
        int travelTime = 20;

        RouteDTO dto = new RouteDTO(route, travelTime);

        assertEquals(route, dto.getRoute());
        assertEquals(travelTime, dto.getTotalTravelTime());
    }

    @Test
    void testSettersAndGetters() {
        RouteDTO dto = new RouteDTO();
        List<String> route = Collections.singletonList("Sirius");
        int travelTime = 30;

        dto.setRoute(route);
        dto.setTotalTravelTime(travelTime);

        assertEquals(route, dto.getRoute());
        assertEquals(travelTime, dto.getTotalTravelTime());
    }
}