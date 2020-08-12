package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Provider;
import net.avalith.carDriver.models.Vehicle;
import net.avalith.carDriver.models.VehicleCategory;
import net.avalith.carDriver.models.VehicleModels;
import net.avalith.carDriver.models.dtos.requests.VehicleDtoRequest;
import net.avalith.carDriver.repositories.ProviderRepository;
import net.avalith.carDriver.repositories.VehicleCategoryRepository;
import net.avalith.carDriver.repositories.VehicleModelRepository;
import net.avalith.carDriver.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_PROVIDER;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_VEHICLE;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_VEHICLE_CATEGORY;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_VEHICLE_MODEL;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleModelRepository vehicleModelRepository;

    @Autowired
    private ProviderRepository providerRepository;

    @Autowired
    public VehicleCategoryRepository vehicleCategoryRepository;

    public List<Vehicle> getAll(){
        return vehicleRepository.findAll();
    }

    public Vehicle save(VehicleDtoRequest vehicleDtoRequest){

        Provider providerSearch = providerRepository.findByName(vehicleDtoRequest.getNameProvider())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROVIDER));
        VehicleModels modelSearch = vehicleModelRepository.findByName(vehicleDtoRequest.getNameModel())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE_MODEL));
        VehicleCategory categorySearch = vehicleCategoryRepository.findByName(vehicleDtoRequest.getNameCategory())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE_CATEGORY));

        return vehicleRepository.save(new Vehicle(vehicleDtoRequest, providerSearch, modelSearch, categorySearch));
    }

    public Vehicle update (VehicleDtoRequest vehicleDtoRequest, String domain){
        Vehicle aux = vehicleRepository.findByDomain(domain)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE));
        Provider providerSearch = providerRepository.findByName(vehicleDtoRequest.getNameProvider())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROVIDER));
        VehicleModels modelSearch = vehicleModelRepository.findByName(vehicleDtoRequest.getNameModel())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE_MODEL));
        VehicleCategory categorySearch = vehicleCategoryRepository.findByName(vehicleDtoRequest.getNameCategory())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_VEHICLE_CATEGORY));

        return vehicleRepository.save(aux.VehicleFromDtoRequest(aux,vehicleDtoRequest,providerSearch,modelSearch,categorySearch));
    }

    public void delete(String domain){
        if(vehicleRepository.delete(domain) < 1)
            throw new NotFoundException(NOT_FOUND_VEHICLE);
    }

}
