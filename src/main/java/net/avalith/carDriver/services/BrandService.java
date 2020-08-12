package net.avalith.carDriver.services;

import lombok.AllArgsConstructor;
import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.Brand;
import net.avalith.carDriver.models.dtos.requests.BrandDtoRequest;
import net.avalith.carDriver.repositories.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static net.avalith.carDriver.utils.Constants.BRAND_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_BRAND;

@Service
@AllArgsConstructor
public class BrandService {

    @Autowired
    private final BrandRepository brandRepository;

    public Brand save(BrandDtoRequest brand){
        if(brandRepository.findByName(brand.getName()).isPresent())
            throw new AlreadyExistsException(BRAND_ALREADY_EXISTS);

        return brandRepository.save(new Brand(brand));
    }

    public List<Brand> getAll(){
        return brandRepository.getAllActive();
    }

    public Brand update(String name, BrandDtoRequest brand){
        Brand brand1 = brandRepository.findByName(name.replace("-", " "))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_BRAND));
        brand1.setName(brand.getName());
        return  brandRepository.save(brand1);
    }
}
