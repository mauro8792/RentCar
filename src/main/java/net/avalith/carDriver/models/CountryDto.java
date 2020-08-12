package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDto {

    private String name;

    public CountryDto(Country country){
        this.name = country.getName();
    }

}
