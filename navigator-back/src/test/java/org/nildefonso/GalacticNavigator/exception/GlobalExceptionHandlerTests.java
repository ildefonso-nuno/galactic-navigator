package org.nildefonso.GalacticNavigator.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nildefonso.GalacticNavigator.controller.NavigatorController; // Assume this is your controller
import org.nildefonso.GalacticNavigator.repository.RouteRepository;
import org.nildefonso.GalacticNavigator.repository.StarSystemRepository;
import org.nildefonso.GalacticNavigator.service.NavigatorService;
import org.nildefonso.GalacticNavigator.service.impl.GraphServiceImpl;
import org.nildefonso.GalacticNavigator.service.impl.NavigatorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(NavigatorController.class)
public class GlobalExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteRepository routeRepository;

    @MockBean
    private StarSystemRepository starSystemRepository;

    @MockBean
    private NavigatorServiceImpl navigatorService;

    @Test
    public void whenStarSystemNotValid_thenBadRequest() throws Exception {
        String startSystem = "invalidStartSystem";
        String endSystem = "invalidEndSystem";

        // Mock the behavior of your service/repository if necessary. For example:
        when(navigatorService.findShortestTravelTime(anyString(), anyString())).thenThrow(new StarSystemNotValidException("message"));

        mockMvc.perform(get("/api/navigator/findShortestTravelTime")
                        .param("startSystem", startSystem)
                        .param("endSystem", endSystem)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
