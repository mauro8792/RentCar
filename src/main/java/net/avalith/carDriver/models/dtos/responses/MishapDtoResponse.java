package net.avalith.carDriver.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Mishap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MishapDtoResponse {

    private String title;

    private String description;

    private Long id_ride;

    public MishapDtoResponse(Mishap mishap) {
        this.title = mishap.getTitle();
        this.description = mishap.getDescription();
        this.id_ride = mishap.getRide().getId();
    }
}
