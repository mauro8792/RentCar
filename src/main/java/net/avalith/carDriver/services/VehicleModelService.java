package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Brand;
import net.avalith.carDriver.models.VehicleModels;
import net.avalith.carDriver.models.dtos.requests.VehicleModelDtoRequest;
import net.avalith.carDriver.repositories.BrandRepository;
import net.avalith.carDriver.repositories.VehicleModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_BRAND;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_VEHICLE;
import static net.avalith.carDriver.utils.Constants.VEHICLE_MODEL_ALREADY_EXISTS;

@Service
public class VehicleModelService {

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private BrandRepository brandRepository;

    public VehicleModels save(VehicleModelDtoRequest models){
        Brand brandSearch = brandRepository.findByName(models.getNameBrand())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BRAND));

        if(vehicleModelRepository.findByName(models.getName()).isPresent())
            throw new AlreadyExistsException(VEHICLE_MODEL_ALREADY_EXISTS);

        return vehicleModelRepository.save(new VehicleModels(models,brandSearch));
    }
    public List<VehicleModels> getAll(){

        return vehicleModelRepository.getAllActive();
    }

    public VehicleModels update (VehicleModelDtoRequest model, String name){
        VehicleModels auxM = vehicleModelRepository.findByName(name.replace("-"," "))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE));
        Brand brandSearch = brandRepository.findByName(model.getNameBrand())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BRAND));

        return vehicleModelRepository.save(auxM.VehicleFromDto(auxM,model,brandSearch));
    }
}
