package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Ride;
import net.avalith.carDriver.models.dtos.requests.DestinationPointDtoRequest;
import net.avalith.carDriver.models.dtos.requests.MishapDtoRequest;
import net.avalith.carDriver.models.dtos.requests.RideDtoRequest;
import net.avalith.carDriver.models.dtos.requests.RideDtoUpdateRequest;
import net.avalith.carDriver.models.dtos.responses.RideDtoResponse;
import net.avalith.carDriver.services.RideService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
@RequestMapping(value = Routes.RIDE)
public class RideController {

    @Autowired
    private RideService rideService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RideDtoResponse> save(@RequestBody @Valid RideDtoRequest ride){

        return ResponseEntity.status(HttpStatus.CREATED).body(new RideDtoResponse(rideService.save(ride)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<RideDtoResponse>> getAll(){
        List<Ride>rides = rideService.getAll();

        if (rides.isEmpty())
            return ResponseEntity.noContent().build();

        List<RideDtoResponse>rideResponses = rides.stream()
                .map(RideDtoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(rideResponses);
    }

    @PutMapping(value = Routes.RIDE_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RideDtoResponse> update(@PathVariable(value = "id") Long id,
                                                   @RequestBody @Valid RideDtoUpdateRequest ride){

        return ResponseEntity.ok(new RideDtoResponse(rideService.update(id, ride)));
    }

    @PatchMapping(value = Routes.END_RIDE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RideDtoResponse> endRide(@PathVariable(value = "id") Long id,
                                                   @RequestBody @Valid DestinationPointDtoRequest destinationPoint){

        return ResponseEntity.ok(new RideDtoResponse(rideService.endRide(id, destinationPoint)));
    }

    @PatchMapping(value = Routes.START_RIDE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RideDtoResponse> startRide(@PathVariable(value = "id") Long id){

        return ResponseEntity.ok(new RideDtoResponse(rideService.startRide(id)));
    }

    @PatchMapping(value = Routes.CRASH_RIDE, produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<RideDtoResponse> crashRide(@PathVariable(value = "id") Long id,
                                                     @RequestBody @Valid MishapDtoRequest mishap){

        return ResponseEntity.ok(new RideDtoResponse(rideService.inCrashRide(id, mishap)));
    }
}
