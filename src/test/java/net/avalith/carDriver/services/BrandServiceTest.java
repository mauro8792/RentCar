package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.factoryService.FactoryService;
import net.avalith.carDriver.models.Brand;
import net.avalith.carDriver.models.dtos.requests.BrandDtoRequest;
import net.avalith.carDriver.repositories.BrandRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.avalith.carDriver.utils.Constants.BRAND_ALREADY_EXISTS;
import static net.avalith.carDriver.utils.Constants.NOT_FOUND_BRAND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BrandServiceTest implements FactoryService {

    private BrandService brandService;

    @Mock
    private BrandRepository mockBrandRepository;

    @BeforeEach
    public void config(){
        brandService = new BrandService(mockBrandRepository);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(mockBrandRepository);
    }

    @Test
    public void getAllBrands(){
        List<Brand> brands = new ArrayList<>();
        when(mockBrandRepository.getAllActive()).thenReturn(brands);
        assertNotNull(brandService.getAll());
    }

    @Nested
    class saveTest{

        @Test
        public void save(){
            BrandDtoRequest brandDto = createBrandDto();
            Brand brand = new Brand(brandDto);

            when(mockBrandRepository.findByName(brandDto.getName())).thenReturn(Optional.empty());
            when(mockBrandRepository.save(brand)).thenReturn(brand);

            assertEquals(brand, brandService.save(brandDto));
        }

        @Test
        public void saveAlreadyExistsException(){
            BrandDtoRequest brandDto = createBrandDto();

            when(mockBrandRepository.findByName(brandDto.getName())).thenReturn(Optional.of(new Brand()));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> brandService.save(brandDto));
            Assertions.assertEquals(ex, new AlreadyExistsException(BRAND_ALREADY_EXISTS));
        }
    }
    @Nested
    class updateTest{

        @Test
        public void update(){
            BrandDtoRequest brandDtoRequest = createBrandDto();
            Brand brand = new Brand("toyota");
            Brand auxBrand = new Brand("chevrolet");

            when(mockBrandRepository.findByName("toyota")).thenReturn(Optional.of(brand));
            when(mockBrandRepository.save(brand)).thenReturn(auxBrand);

            assertEquals(auxBrand, brandService.update("toyota", brandDtoRequest));
        }

        @Test
        public void updateNotFoundName(){
            BrandDtoRequest brandDtoRequest = createBrandDto();

            when(mockBrandRepository.findByName("toyota")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> brandService.update("toyota",brandDtoRequest));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_BRAND));

        }
    }
}

