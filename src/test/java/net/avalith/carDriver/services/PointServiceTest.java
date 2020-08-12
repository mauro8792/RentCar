package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.factoryService.FactoryService;
import net.avalith.carDriver.models.City;
import net.avalith.carDriver.models.Point;
import net.avalith.carDriver.models.dtos.requests.PointDtoRequest;
import net.avalith.carDriver.repositories.CityRepository;
import net.avalith.carDriver.repositories.PointRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_CITY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_POINT;
import static net.avalith.carDriver.utils.Constants.POINT_ALREADY_EXISTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PointServiceTest implements FactoryService {

    PointService pointService;

    @Mock
    PointRepository pointRepository;

    @Mock
    CityRepository cityRepository;

    @Mock
    RedisTemplate<String, Point> redisTemplate;

    @BeforeEach
    public void setUp(){
        initMocks(this);
        this.pointService = new PointService(pointRepository, cityRepository, redisTemplate);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(pointRepository, cityRepository);
    }

    @Test
    public void get_all_should_return_arraylist_when_is_all_ok(){
        List<Point> points = new ArrayList<>();
        when(pointRepository.findAll()).thenReturn(points);

        Assertions.assertNotNull(pointService.getAll());
    }

    @Nested
    class saveTest{

        @Test
        public void save_should_throw_not_found_exception_when_city_does_not_exist(){
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> pointService.save(createPointDtoRequest()));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_CITY));
        }

        @Test
        public void save_should_throw_already_exists_exception_when_point_already_exist(){
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(new City()));
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.of(new Point()));

            AlreadyExistsException  ex = Assertions.assertThrows(AlreadyExistsException.class, () -> pointService.save(createPointDtoRequest()));
            Assertions.assertEquals(ex, new AlreadyExistsException(POINT_ALREADY_EXISTS));
        }

        @Test
        public void save_should_return_point_when_is_all_ok(){
            PointDtoRequest pointDto = createPointDtoRequest();
            Point point = createPoint();

            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(new City()));
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.empty());
            when(pointRepository.save(any(Point.class))).thenReturn(point);

            Assertions.assertEquals(point, pointService.save(pointDto));
        }
    }

    @Nested
    class updateTest{

        @Test
        public void update_should_throw_not_found_exception_when_point_does_not_exist(){
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> pointService.update("30", "20", createPointDtoUpdateRequest()));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_POINT));
        }

        @Test
        public void update_should_throw_already_exists_exception_when_point_already_exist(){
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.of(new Point()));
            when(pointRepository.getByLatAndLng("20", "30")).thenReturn(Optional.of(new Point()));

            AlreadyExistsException  ex = Assertions.assertThrows(AlreadyExistsException.class, () -> pointService.update("30", "20", createPointDtoUpdateRequest()));
            Assertions.assertEquals(ex, new AlreadyExistsException(POINT_ALREADY_EXISTS));
        }

        @Test
        public void update_should_throw_not_found_exception_when_city_does_not_exist(){
            when(pointRepository.getByLatAndLng("20", "30")).thenReturn(Optional.of(new Point()));
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.empty());
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.empty());

            NotFoundException  ex = Assertions.assertThrows(NotFoundException.class, () -> pointService.update("20", "30", createPointDtoUpdateRequest()));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_CITY));
        }

        @Test
        public void update_should_return_point_when_is_all_ok(){
            when(pointRepository.getByLatAndLng("20", "30")).thenReturn(Optional.of(new Point()));
            when(pointRepository.getByLatAndLng("30", "20")).thenReturn(Optional.empty());
            when(cityRepository.findByName("Mar del Plata")).thenReturn(Optional.of(new City()));
            when(pointRepository.save(any(Point.class))).thenReturn(createPoint());

            Assertions.assertEquals(createPoint(), pointService.update("20", "30", createPointDtoUpdateRequest()));
        }
    }
}
