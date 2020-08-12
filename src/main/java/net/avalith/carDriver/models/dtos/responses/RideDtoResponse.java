package net.avalith.carDriver.models.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Ride;
import net.avalith.carDriver.models.dtos.RidePointDto;
import net.avalith.carDriver.models.enums.RideState;
import net.avalith.carDriver.models.enums.TariffType;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RideDtoResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    private RideState state;

    private String vehicleDomain;

    private TariffType tariffType;

    private Double price;

    private RidePointDto originPoint;

    private RidePointDto destinationPoint;

    private String userDni;

    public RideDtoResponse(Ride ride) {
        this.id = ride.getId();
        this.startDate = ride.getStartDate();
        this.endDate = ride.getEndDate();
        this.state = ride.getState();
        this.vehicleDomain = ride.getVehicle().getDomain();
        this.tariffType = ride.getTariffType();
        this.price = ride.getPrice();
        this.originPoint = new RidePointDto(ride.getOriginPoint());
        if(ride.getDestinationPoint()!=null)
            this.destinationPoint = new RidePointDto(ride.getDestinationPoint());
        this.userDni = ride.getUser().getDni();
    }
}
