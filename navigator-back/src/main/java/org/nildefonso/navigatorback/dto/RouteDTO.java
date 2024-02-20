package org.nildefonso.navigatorback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Schema(description = "Route Model Information")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RouteDTO {
    @Schema(description = "Route")
    @NotEmpty(message = "Route should not be null or empty")
    private List<String> route;

    @Schema(description = "Total Travel Time")
    @Min(value = -1, message = "Total Travel Time should not be less than -1")
    private Integer totalTravelTime;
}