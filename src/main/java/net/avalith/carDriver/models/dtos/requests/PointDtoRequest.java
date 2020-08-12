package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Point;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PointDtoRequest {

    @NotNull(message = "Is origin is required")
    private Boolean isOrigin;

    @NotBlank(message = "The latitude coordinate is required")
    private String lat;

    @NotBlank(message = "The longitude coordinate is required")
    private String lng;

    @NotNull(message = "The capacity is required")
    private Integer capacity;

    @NotNull(message = "The stock is required")
    private Integer stock;

    @NotBlank(message = "The city name is required")
    private String cityName;

    public PointDtoRequest(Point point) {
        isOrigin = point.getIsOrigin();
        lat = point.getLat();
        lng = point.getLng();
        capacity = point.getCapacity();
        stock = point.getStock();
        cityName = point.getCity().getName();
    }
}

