package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.factoryService.FactoryService;
import net.avalith.carDriver.models.City;
import net.avalith.carDriver.models.Country;
import net.avalith.carDriver.models.dtos.CityDto;
import net.avalith.carDriver.repositories.CityRepository;
import net.avalith.carDriver.repositories.CountryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.avalith.carDriver.utils.Constants.CITY_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_CITY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_COUNTRY;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CityServiceTest implements FactoryService {

    CityService cityService;

    @Mock
    CityRepository cityRepository;

    @Mock
    CountryRepository countryRepository;

    @Mock
    RedisTemplate<String, City> redisTemplate;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        cityService = new CityService(cityRepository, countryRepository, redisTemplate);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(cityRepository, countryRepository);
    }

    @Test
    public void get_all_should_return_arraylist_when_is_all_ok(){
        List<City> cities = new ArrayList<>();
        when(cityRepository.findAll()).thenReturn(cities);
        Assertions.assertNotNull(cityService.getAll());
    }

    @Nested
    class saveTest{

        @Test
        public void save_should_throw_not_found_exception_when_country_does_not_exist(){
            when(countryRepository.findByName("Argentina")).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> cityService.save(createCityDto()));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_COUNTRY));
        }

        @Test
        public void save_should_throw_already_exception_when_city_already_exists(){
            when(countryRepository.findByName("Argentina")).thenReturn(Optional.of(new Country()));

            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(new City()));

            AlreadyExistsException  ex = Assertions.assertThrows(AlreadyExistsException.class, () -> cityService.save(createCityDto()));
            Assertions.assertEquals(ex, new AlreadyExistsException(CITY_ALREADY_EXISTS));
        }

        @Test
        public void save_should_return_city_when_is_all_ok(){

            City city = createCity();

            when(countryRepository.findByName("Argentina")).thenReturn(Optional.of(createCountry()));

            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.empty());

            when(cityRepository.save(createCity())).thenReturn(city);

            Assertions.assertEquals(city, cityService.save(createCityDto()));
        }
    }

    @Nested
    class updateTest{
        @Test
        public void update_should_return_city_when_is_all_ok(){
            City oldCity = createCity();
            CityDto cityDto = createCityDtoAnotherName();
            City newCity = createCityAnotherName();

            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(oldCity));
            when(cityRepository.findByName(cityDto.getName())).thenReturn(Optional.empty());
            when(countryRepository.findByName(cityDto.getCountryName())).thenReturn(Optional.of(createCountry()));
            when(cityRepository.save(newCity)).thenReturn(newCity);

            Assertions.assertEquals(newCity, cityService.update("Mar del Plata", cityDto));
        }

        @Test
        public void update_should_throw_not_found_exception_when_city_does_not_exist(){
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> cityService.update("Mar del Plata", createCityDtoAnotherName()));

            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_CITY));
        }

        @Test
        public void update_should_throw_already_exists_exception_when_city_already_exist(){
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(new City()));
            when(cityRepository.findByName("Buenos Aires")).thenReturn(Optional.of(createCityAnotherName()));

            AlreadyExistsException  ex = Assertions.assertThrows(AlreadyExistsException.class, () -> cityService.update("Mar del Plata", createCityDtoAnotherName()));

            Assertions.assertEquals(ex, new AlreadyExistsException(CITY_ALREADY_EXISTS));
        }

        @Test
        public void update_should_throw_not_found_exception_when_country_does_not_exist(){
            CityDto cityDto = createCityDtoAnotherName();

            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(createCity()));
            when(cityRepository.findByName(cityDto.getName())).thenReturn(Optional.empty());
            when(countryRepository.findByName(cityDto.getCountryName())).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> cityService.update("Mar del Plata", cityDto));

            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_COUNTRY));
        }
    }
}
