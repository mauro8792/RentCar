package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Country;
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

import static net.avalith.carDriver.utils.Constants.COUNTRY_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_COUNTRY;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CountryServiceTest {

    CountryService countryService;

    @Mock
    CountryRepository countryRepository;

    @Mock
    RedisTemplate<String, Country> redisTemplate;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        countryService = new CountryService(countryRepository, redisTemplate);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(countryRepository);
    }

    @Nested
    class saveTest{

        @Test
        public void save_should_throw_already_exists_exception_when_country_already_exists(){
            when(countryRepository.findByName("Argentina")).thenReturn(Optional.of(new Country("Argentina")));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> countryService.save(new Country("Argentina")));
            Assertions.assertEquals(ex, new AlreadyExistsException(COUNTRY_ALREADY_EXISTS));
        }

        @Test
        public void save_should_return_country_when_is_all_ok(){
            Country country = new Country("Argentina");

            when(countryRepository.findByName("Argentina")).thenReturn(Optional.empty());
            when(countryRepository.save(new Country("Argentina"))).thenReturn(country);

            Assertions.assertEquals(country, countryService.save(new Country("Argentina")));
        }
    }

    @Test
    public void get_all_should_return_arraylist_when_is_all_ok(){
        List<Country>countries = new ArrayList<>();
        when(countryRepository.findAll()).thenReturn(countries);

        Assertions.assertNotNull(countryService.getAll());
    }

    @Nested
    class updateTest{

        @Test
        public void update_should_throw_not_found_exception_when_country_does_not_exist(){
            Country country = new Country("Argentina");

            when(countryRepository.findByName("Uruguay")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> countryService.update("Uruguay", country));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_COUNTRY));
        }

        @Test
        public void update_should_throw_already_exists_exception_when_country_already_exists(){
            Country country = new Country("Argentina");

            when(countryRepository.findByName("Uruguay")).thenReturn(Optional.of(new Country()));
            when(countryRepository.findByName("Argentina")).thenReturn(Optional.of(country));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> countryService.update("Uruguay", country));
            Assertions.assertEquals(ex, new AlreadyExistsException(COUNTRY_ALREADY_EXISTS));
        }

        @Test
        public void update_should_return_country_when_is_all_ok(){
            Country country = new Country("Argentina");

            when(countryRepository.findByName("Uruguay")).thenReturn(Optional.of(new Country()));
            when(countryRepository.findByName("Argentina")).thenReturn(Optional.empty());
            when(countryRepository.save(country)).thenReturn(country);

            Assertions.assertEquals(country, countryService.update("Uruguay", country));
        }
    }
}
