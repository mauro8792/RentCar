package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.VehicleCategory;
import net.avalith.carDriver.models.dtos.requests.VehicleCategoryDtoRequest;
import net.avalith.carDriver.models.dtos.responses.DeleteResponseDto;
import net.avalith.carDriver.models.dtos.responses.VehicleCategoryDtoResponse;
import net.avalith.carDriver.models.enums.VehicleCategoryEnum;
import net.avalith.carDriver.services.VehicleCategoryService;
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

import static net.avalith.carDriver.utils.Constants.DELETED_VEHICLE_CATEGORY;

@RequestMapping(value = Routes.VEHICLE_CATEGORY)
@RestController
public class VehicleCategoryController {

    @Autowired
    private VehicleCategoryService vehicleCategoryService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<VehicleCategoryDtoResponse>> getAll() {
        List<VehicleCategory> listVehicleCategoryVehicles = vehicleCategoryService.getAll();
        if (listVehicleCategoryVehicles.isEmpty()) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listVehicleCategoryVehicles.stream()
            .map(VehicleCategoryDtoResponse::new)
            .collect(Collectors.toList()));

    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<VehicleCategoryDtoResponse> save(@RequestBody @Valid VehicleCategoryDtoRequest categoryVehicle) {

        return ResponseEntity.status(HttpStatus.CREATED).body(new VehicleCategoryDtoResponse(vehicleCategoryService.save(categoryVehicle)));
    }

    @PutMapping(value = Routes.VEHICLE_CATEGORY_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<VehicleCategoryDtoResponse> update(@PathVariable VehicleCategoryEnum name, @RequestBody @Valid VehicleCategoryDtoRequest vehicleCategoryDtoRequest){

        return ResponseEntity.ok( new VehicleCategoryDtoResponse(vehicleCategoryService.update(vehicleCategoryDtoRequest, name)));
    }

    @DeleteMapping(value = Routes.VEHICLE_CATEGORY_DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteResponseDto>delete(@PathVariable String name){

        vehicleCategoryService.delete(name.replace("-"," "));

        return ResponseEntity.ok(new DeleteResponseDto(String.format(DELETED_VEHICLE_CATEGORY,name)));
    }

}