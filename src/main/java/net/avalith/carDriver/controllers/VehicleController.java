package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Vehicle;
import net.avalith.carDriver.models.dtos.requests.VehicleDtoRequest;
import net.avalith.carDriver.models.dtos.responses.DeleteResponseDto;
import net.avalith.carDriver.models.dtos.responses.VehicleDtoResponse;
import net.avalith.carDriver.services.VehicleService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import static net.avalith.carDriver.utils.Constants.DELETED_VEHICLE;

@RestController
@RequestMapping(value = Routes.VEHICLE)
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<VehicleDtoResponse>> getAll(){
        List<Vehicle> listVehicles = vehicleService.getAll();
        if (listVehicles.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listVehicles.stream()
                .map(VehicleDtoResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<VehicleDtoResponse> save(@RequestBody @Valid VehicleDtoRequest vehicle){

        return ResponseEntity.status(HttpStatus.CREATED).body(new VehicleDtoResponse(vehicleService.save(vehicle)));
    }

    @PutMapping(value = Routes.VEHICLE_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<VehicleDtoResponse> update (@RequestBody @Valid VehicleDtoRequest vehicleDtoRequest, @PathVariable String domain){

        return ResponseEntity.ok(new VehicleDtoResponse(vehicleService.update(vehicleDtoRequest,domain)));
    }

    @DeleteMapping(value = Routes.VEHICLE_DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteResponseDto>delete(@PathVariable String domain){
        vehicleService.delete(domain);

        return ResponseEntity.ok(new DeleteResponseDto(String.format(DELETED_VEHICLE,domain)));
    }


}
