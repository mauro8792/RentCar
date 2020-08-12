package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Country;
import net.avalith.carDriver.models.CountryDto;
import net.avalith.carDriver.services.CountryService;
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
@RequestMapping(value = Routes.COUNTRY)
public class CountryController {

    @Autowired
    private CountryService countryService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CountryDto> save(@RequestBody @Valid Country country){
        CountryDto response = new CountryDto(countryService.save(country));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<CountryDto>> getAll(){
        List<Country> countries = countryService.getAll();

        if (countries.isEmpty())
            return ResponseEntity.noContent().build();

        List<CountryDto> response = countries.stream()
                .map(CountryDto::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PutMapping(value = Routes.COUNTRY_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CountryDto> update(@PathVariable(value = "name") String name, @RequestBody @Valid Country country){
        CountryDto response = new CountryDto(countryService.update(name, country));

        return ResponseEntity.ok(response);
    }
}
