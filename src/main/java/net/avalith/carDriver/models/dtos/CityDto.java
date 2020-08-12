package net.avalith.carDriver.models.dtos;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.City;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CityDto {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The country name is required")
    private String countryName;

    public CityDto(City city) {
        name = city.getName();
        countryName = city.getCountry().getName();
    }
}
