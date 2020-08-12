package net.avalith.carDriver.models.dtos.responses;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.Sale;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SaleDtoResponse {

    private Double profit;

    private Long idRide;

    public SaleDtoResponse(Sale sale){
        profit = sale.getProfit();
        idRide = sale.getRide().getId();
    }
}
