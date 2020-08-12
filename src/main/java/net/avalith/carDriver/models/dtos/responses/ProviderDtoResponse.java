package net.avalith.carDriver.models.dtos.responses;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProviderDtoResponse {

    private String name;

    private String email;

    private String businessName;

    private String phone;

    private Double commission;

    private List<VehicleDtoResponse> vehicles = new ArrayList<>();

    public ProviderDtoResponse(Provider provider){
        this.name = provider.getName();
        this.email = provider.getEmail();
        this.businessName = provider.getBusinessName();
        this.phone = provider.getPhone();
        this.commission = provider.getCommission();
        this.vehicles = provider.getVehicles().stream().map(VehicleDtoResponse::new).collect(Collectors.toList());
    }
}
