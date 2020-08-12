package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.City;
import net.avalith.carDriver.models.dtos.CityDto;
import net.avalith.carDriver.services.CityService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Routes.CITY)
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CityDto> save(@RequestBody @Valid CityDto city){

        return ResponseEntity.status(HttpStatus.CREATED).body(new CityDto(cityService.save(city)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<CityDto>> getAll(){
        List<City> citiesAux = cityService.getAll();

        if(citiesAux.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(citiesAux.stream()
                .map(CityDto::new)
                .collect(Collectors.toList()));
    }

    @PutMapping(value = Routes.CITY_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CityDto> update(@PathVariable(value = "name") String name, @RequestBody @Valid CityDto city){

        return ResponseEntity.ok(new CityDto(cityService.update(name, city)));
    }
}
