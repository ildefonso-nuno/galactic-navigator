package org.nildefonso.GalacticNavigator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Routes")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    @Id
    private String id;
    private String startSystem;
    private String endSystem;
    private int travelTime;
}