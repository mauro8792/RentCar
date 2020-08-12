package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.RidePointDto;
import net.avalith.carDriver.models.enums.TariffType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RideDtoUpdateRequest {

    @NotNull(message = "The start date is required")
    private Date startDate;

    @NotNull(message = "The end date is required")
    private Date endDate;

    @NotBlank(message = "The vehicle domain is required")
    private String vehicleDomain;

    @NotNull(message = "The tariff type is required")
    private TariffType tariffType;

    @NotNull(message = "The origin point is required")
    private RidePointDto originPoint;

    @NotNull(message = "The destination point is required")
    private RidePointDto destinationPoint;

    @NotBlank(message = "The user dni is required")
    private String userDni;
}
