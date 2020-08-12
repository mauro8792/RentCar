package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.City;
import net.avalith.carDriver.models.Country;
import net.avalith.carDriver.models.dtos.CityDto;
import net.avalith.carDriver.repositories.CityRepository;
import net.avalith.carDriver.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.CITY_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.CITY_KEY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_CITY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_COUNTRY;

@Service
@AllArgsConstructor
public class CityService {

    @Autowired
    private final CityRepository cityRepository;

    @Autowired
    private final CountryRepository countryRepository;

    @Autowired
    private final RedisTemplate<String, City> redisTemplate;

    public City save(CityDto city){

        city.setCountryName(city.getCountryName().toLowerCase());
        city.setName(city.getName().toLowerCase());

        Country country = countryRepository.findByName(city.getCountryName())
            .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY));

        if(cityRepository.findByName(city.getName()).isPresent())
            throw new AlreadyExistsException(CITY_ALREADY_EXISTS);

        City newCity = cityRepository.save(new City(city, country));
        redisTemplate.opsForHash().put(CITY_KEY, newCity.getId(), newCity);

        return newCity;
    }

    public List<City> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<City> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(CITY_KEY).isEmpty()){
            cityRepository.findAll()
                    .forEach((City city) -> redisTemplate.opsForHash().put(CITY_KEY, city.getId(), city));
            redisTemplate.boundHashOps(CITY_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(CITY_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<City>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public City update(String name, CityDto city) {
        city.setCountryName(city.getCountryName().toLowerCase());
        city.setName(city.getName().toLowerCase());

        City oldCity = cityRepository.findByName(name.replace("-", " "))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CITY));

        if(!city.getName().equals(oldCity.getName()))
            if(cityRepository.findByName(city.getName()).isPresent())
                throw new AlreadyExistsException(CITY_ALREADY_EXISTS);

        Country country = countryRepository.findByName(city.getCountryName())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY));

        City updatedCity = new City(city, country);
        updatedCity.setId(oldCity.getId());

        redisTemplate.opsForHash().put(CITY_KEY, updatedCity.getId(), updatedCity);

        return cityRepository.save(updatedCity);
    }
}
