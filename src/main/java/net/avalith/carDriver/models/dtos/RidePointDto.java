package net.avalith.carDriver.models.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Point;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RidePointDto {

    @NotBlank(message = "The latitude coordinate is required")
    private String lat;

    @NotBlank(message = "The longitude coordinate is required")
    private String lng;

    public RidePointDto(Point point) {
        this.lat = point.getLat();
        this.lng = point.getLng();
    }
}
