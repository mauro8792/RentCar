package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleModelDtoRequest {

    @NotBlank(message = "The name is required")
    private String name;

    @NotNull
    private Integer cantPlace;

    @NotNull
    private Boolean isAutomatic;

    @NotBlank(message = "The name brand is required")
    private String nameBrand;

}
