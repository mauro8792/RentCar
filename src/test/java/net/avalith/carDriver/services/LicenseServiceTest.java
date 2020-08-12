package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.License;
import net.avalith.carDriver.models.User;
import net.avalith.carDriver.models.dtos.requests.LicenseDtoRequest;
import net.avalith.carDriver.repositories.LicenseRepository;
import net.avalith.carDriver.repositories.UserRepository;
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

import static net.avalith.carDriver.utils.Constants.LICENSE_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_LICENSE_USER;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LicenseServiceTest {

    LicenseService licenseService;

    @Mock
    LicenseRepository licenseRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    RedisTemplate<String, License> redisTemplate;

    @Mock
    RedisTemplate<String, User> redisTemplateUser;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        licenseService = new LicenseService(licenseRepository, userRepository, redisTemplate, redisTemplateUser);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(licenseRepository, userRepository);
    }


    @Test
    public void get_all_should_return_arraylist_when_is_all_ok(){
        List<License> licenses = new ArrayList<>();
        when(licenseRepository.findAll()).thenReturn(licenses);
        Assertions.assertNotNull(licenseService.getAll());
    }

    @Nested
    class saveTest{

        @Test
        public void save_should_throw_already_exists_exception_when_license_already_exists(){
            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.of(new License()));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> licenseService.save(new LicenseDtoRequest("42454677")));
            Assertions.assertEquals(ex, new AlreadyExistsException(LICENSE_ALREADY_EXISTS));
        }

        @Test
        public void save_should_throw_not_found_exception_when_license_does_not_exist(){
            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.empty());
            when(userRepository.getByDni("42454677")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> licenseService.save(new LicenseDtoRequest("42454677")));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_LICENSE_USER));
        }

        @Test
        public void save_should_return_license_when_is_all_ok(){
            License license = new License("42454677");

            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.empty());
            when(userRepository.getByDni("42454677")).thenReturn(Optional.of(new User()));
            when(licenseRepository.save(new License("42454677"))).thenReturn(license);

            Assertions.assertEquals(license, licenseService.save(new LicenseDtoRequest("42454677")));
        }
    }
/*
    @Nested
    class updateTest{

        @Test
        public void update_should_throw_not_found_exception_when_license_does_not_exist(){
            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> licenseService.update("42454677", new LicenseDtoRequest("42454677")));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_LICENSE));
        }

        @Test
        public void update_should_throw_already_exists_exception_when_license_already_exists(){
            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.of(new License()));
            when(licenseRepository.findByNumber("42454700")).thenReturn(Optional.of(new License()));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> licenseService.update("42454677", new LicenseDtoRequest("42454700")));
            Assertions.assertEquals(ex, new AlreadyExistsException(LICENSE_ALREADY_EXISTS));
        }

        @Test
        public void update_should_throw_not_found_exception_when_user_does_not_exist(){
            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.of(new License()));
            when(licenseRepository.findByNumber("42454700")).thenReturn(Optional.empty());
            when(userRepository.getByDni("42454700")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> licenseService.update("42454677", new LicenseDtoRequest("42454700")));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_LICENSE_USER));
        }

        @Test
        public void update_should_return_license_when_is_all_ok(){
            License license = new License("42454700");

            when(licenseRepository.findByNumber("42454677")).thenReturn(Optional.of(new License()));
            when(licenseRepository.findByNumber("42454700")).thenReturn(Optional.empty());
            when(userRepository.getByDni("42454700")).thenReturn(Optional.of(new User()));
            when(licenseRepository.save(new License("42454700"))).thenReturn(license);

            Assertions.assertEquals(license, licenseService.update("42454677", new LicenseDtoRequest(license)));
        }
    }*/
}
