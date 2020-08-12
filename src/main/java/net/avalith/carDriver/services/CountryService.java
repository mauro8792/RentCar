package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Country;
import net.avalith.carDriver.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.COUNTRY_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.COUNTRY_KEY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_COUNTRY;

@Service
@AllArgsConstructor
public class CountryService {

    @Autowired
    private final CountryRepository countryRepository;

    @Autowired
    private final RedisTemplate<String, Country> redisTemplate;

    public Country save(Country country){
        country.setName(country.getName().toLowerCase());

        if(countryRepository.findByName(country.getName()).isPresent())
            throw new AlreadyExistsException(COUNTRY_ALREADY_EXISTS);

        Country newCountry = countryRepository.save(country);
        redisTemplate.opsForHash().put(COUNTRY_KEY, newCountry.getId(), newCountry);
        return newCountry;
    }

    public List<Country> getAll(){

        ObjectMapper objectMapper = new ObjectMapper();
        List<Country> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(COUNTRY_KEY).isEmpty()){
            countryRepository.findAll()
                    .forEach((Country country) -> redisTemplate.opsForHash().put(COUNTRY_KEY, country.getId(), country));
            redisTemplate.boundHashOps(COUNTRY_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(COUNTRY_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<Country>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Country update(String name, Country country) {
        Country oldCountry = countryRepository.findByName(name.replace("-", " "))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COUNTRY));

        country.setName(country.getName().toLowerCase());

        if(!country.getName().equals(oldCountry.getName()))
            if(countryRepository.findByName(country.getName()).isPresent())
                throw new AlreadyExistsException(COUNTRY_ALREADY_EXISTS);

        country.setId(oldCountry.getId());

        redisTemplate.opsForHash().put(COUNTRY_KEY, country.getId(), country);

        return countryRepository.save(country);
    }
}
