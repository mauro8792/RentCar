package net.avalith.carDriver.models.dtos.requests;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.enums.Colors;
import net.avalith.carDriver.models.enums.VehicleCategoryEnum;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleDtoRequest {

    @NotBlank(message = "The domain is required")
    private String domain;

    @NotNull(message = "The color is required")
    private Colors color;

    @NotBlank(message = "The name model is required")
    private String nameModel;

    @NotBlank(message = "The name provider is required")
    private String nameProvider;

    @NotNull(message = "The name category is required")
    private VehicleCategoryEnum nameCategory;

}
