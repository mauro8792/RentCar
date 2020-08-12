package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Point;
import net.avalith.carDriver.models.dtos.requests.PointDtoRequest;
import net.avalith.carDriver.models.dtos.requests.PointDtoUpdateRequest;
import net.avalith.carDriver.models.dtos.responses.PointDtoResponse;
import net.avalith.carDriver.services.PointService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Routes.POINTS)
public class PointController {

    @Autowired
    private PointService pointService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PointDtoResponse> save(@RequestBody @Valid PointDtoRequest point){

        return ResponseEntity.status(HttpStatus.CREATED).body(new PointDtoResponse(pointService.save(point)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<PointDtoResponse>> getAll(){
        List<Point> pointsAux = pointService.getAll();

        if (pointsAux.isEmpty())
            return ResponseEntity.noContent().build();

        List<PointDtoResponse> points = pointsAux.stream()
                .map(PointDtoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(points);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PointDtoResponse> update(@RequestParam(value = "latitude") String lat,
                                                   @RequestParam(value = "longitude") String lng,
                                                   @RequestBody @Valid PointDtoUpdateRequest point){

        return ResponseEntity.ok(new PointDtoResponse(pointService.update(lat, lng, point)));
    }
}
