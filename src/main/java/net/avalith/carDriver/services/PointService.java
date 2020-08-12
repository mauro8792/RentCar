package net.avalith.carDriver.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.City;
import net.avalith.carDriver.models.Point;
import net.avalith.carDriver.models.dtos.requests.PointDtoRequest;
import net.avalith.carDriver.models.dtos.requests.PointDtoUpdateRequest;
import net.avalith.carDriver.repositories.CityRepository;
import net.avalith.carDriver.repositories.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_CITY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_POINT;
import static net.avalith.carDriver.utils.Constants.POINT_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.POINT_KEY;

@Service
@AllArgsConstructor
public class PointService {

    @Autowired
    private final PointRepository pointRepository;

    @Autowired
    private final CityRepository cityRepository;

    @Autowired
    private final RedisTemplate<String, Point> redisTemplate;

    public Point save(PointDtoRequest point){
        point.setCityName(point.getCityName().toLowerCase());

        City city = cityRepository.findByName(point.getCityName())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CITY));

        if(pointRepository
                .getByLatAndLng(point.getLat(), point.getLng())
                .isPresent())
            throw new AlreadyExistsException(POINT_ALREADY_EXISTS);

        Point newPoint = pointRepository.save(new Point(point, city));
        redisTemplate.opsForHash().put(POINT_KEY, newPoint.getId(), newPoint);

        return newPoint;
    }

    public List<Point> getAll(){
        ObjectMapper objectMapper = new ObjectMapper();
        List<Point> list = new ArrayList<>();
        String json = "";

        if(redisTemplate.opsForHash().keys(POINT_KEY).isEmpty()){
            pointRepository.findAll()
                    .forEach((Point point) -> redisTemplate.opsForHash().put(POINT_KEY, point.getId(), point));
            redisTemplate.boundHashOps(POINT_KEY).expire(24L, TimeUnit.HOURS);
        }

        try {
            json = objectMapper.writeValueAsString(redisTemplate.opsForHash().values(POINT_KEY));
            list = objectMapper.readValue(json, new TypeReference<List<Point>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Point update(String lat, String lng, PointDtoUpdateRequest point) {
        Point oldPoint = pointRepository.getByLatAndLng(lat, lng)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_POINT));

        if(!point.getLat().equals(oldPoint.getLat()) && !point.getLng().equals(oldPoint.getLng()))
            if(pointRepository.getByLatAndLng(point.getLat(), point.getLng()).isPresent())
                throw new AlreadyExistsException(POINT_ALREADY_EXISTS);

        point.setCityName(point.getCityName().toLowerCase());

        City city = cityRepository.findByName(point.getCityName())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CITY));

        Point pointUpdate = new Point(point, city);
        pointUpdate.setId(oldPoint.getId());
        pointUpdate.setIsActive(oldPoint.getIsActive());

        redisTemplate.opsForHash().put(POINT_KEY, pointUpdate.getId(), pointUpdate);

        return pointRepository.save(pointUpdate);
    }
}
