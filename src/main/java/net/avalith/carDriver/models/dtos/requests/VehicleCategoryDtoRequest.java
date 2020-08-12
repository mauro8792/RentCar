package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.enums.VehicleCategoryEnum;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleCategoryDtoRequest {

    @NotNull(message = "The name category type is required")
    private VehicleCategoryEnum name;

    @NotNull(message = "The price for hour is required")
    private Double priceHour;

    @NotNull(message = "The price for day is required")
    private Double priceDay;

    @NotNull(message = "The price for week is required")
    private Double priceWeek;
}
