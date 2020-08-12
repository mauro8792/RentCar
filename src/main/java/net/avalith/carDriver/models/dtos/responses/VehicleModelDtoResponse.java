package net.avalith.carDriver.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.VehicleModels;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class VehicleModelDtoResponse {

    private String name;

    private Integer cantPlace;

    private Boolean isAutomatic;

    private String nameBrand;

    public VehicleModelDtoResponse(VehicleModels vehicleModels) {
        this.name = vehicleModels.getName();
        this.cantPlace = vehicleModels.getCantPlace();
        this.isAutomatic = vehicleModels.getIsAutomatic();
        this.nameBrand = vehicleModels.getBrand().getName();
    }
}
