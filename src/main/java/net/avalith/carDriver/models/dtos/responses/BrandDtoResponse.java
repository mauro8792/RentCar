package net.avalith.carDriver.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Brand;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDtoResponse {

    private String name;

    private List<VehicleModelDtoResponse> vehicle_models;

    public BrandDtoResponse(Brand brand) {
        this.name = brand.getName();
        this.vehicle_models = brand.getVehicleModels().stream().map(VehicleModelDtoResponse::new).collect(Collectors.toList());
    }
}
