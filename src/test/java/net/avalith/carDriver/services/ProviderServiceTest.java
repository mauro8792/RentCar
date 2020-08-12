package net.avalith.carDriver.services;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.factoryService.FactoryService;
import net.avalith.carDriver.models.Provider;
import net.avalith.carDriver.models.dtos.requests.ProviderDtoRequest;
import net.avalith.carDriver.repositories.ProviderRepository;
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

import static net.avalith.carDriver.utils.Constants.NOT_FOUND_PROVIDER;
import static net.avalith.carDriver.utils.Constants.PROVIDER_ALREADY_EXISTS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProviderServiceTest implements FactoryService {

    private ProviderService providerService;

    @Mock
    private ProviderRepository mockProviderRepository;

    @BeforeEach
    public void config(){
        providerService = new ProviderService(mockProviderRepository);
    }

    @AfterEach
    public void reset(){
        Mockito.reset(mockProviderRepository);
    }

    @Test
    public void getAllProvider(){
        List<Provider> providers = new ArrayList<>();
        when(mockProviderRepository.getAllActive()).thenReturn(providers);
        assertNotNull(providerService.getAll());
    }

    @Nested
    class saveTest{

        @Test
        public void save(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();
            Provider provider = new Provider(providerDtoRequest);

            when(mockProviderRepository.findByName(providerDtoRequest.getName())).thenReturn(Optional.empty());
            when(mockProviderRepository.findNotAvailableByName(providerDtoRequest.getName())).thenReturn(null);
            when(mockProviderRepository.save(new Provider(providerDtoRequest))).thenReturn(provider);

            assertEquals(provider,providerService.save(providerDtoRequest));
        }

        @Test
        public void save_exist_provider(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();
            Provider auxProvider = new Provider(providerDtoRequest);

            when(mockProviderRepository.findByName(providerDtoRequest.getName())).thenReturn(Optional.empty());

            when(mockProviderRepository.findNotAvailableByName(providerDtoRequest.getName())).thenReturn(auxProvider);

            when(mockProviderRepository.save(auxProvider)).thenReturn(auxProvider);

            assertEquals(auxProvider, providerService.save(providerDtoRequest));
        }

        @Test
        public void save_exist_name_provider(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();

            when(mockProviderRepository.findByName(providerDtoRequest.getName())).thenReturn(Optional.of(new Provider()));

            AlreadyExistsException ex = Assertions.assertThrows(AlreadyExistsException.class, () -> providerService.save(providerDtoRequest));
            Assertions.assertEquals(ex, new AlreadyExistsException(PROVIDER_ALREADY_EXISTS));
        }
    }

    @Nested
    class updateTest{
        @Test
        public void update(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();
            Provider provider = new Provider(providerDtoRequest);

            when(mockProviderRepository.findByName("toyota")).thenReturn(Optional.of(createProvider()));
            when(mockProviderRepository.save(new Provider(providerDtoRequest))).thenReturn(provider);

            assertEquals(provider,providerService.update("toyota",providerDtoRequest));
        }

        @Test
        public void update_not_found_provider(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();

            when(mockProviderRepository.findByName("toyota")).thenReturn(Optional.empty());

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> providerService.update("toyota",providerDtoRequest));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_PROVIDER));
        }
    }

    @Nested
    class deleteTest{
        @Test
        public void delete(){
            ProviderDtoRequest providerDtoRequest = createProviderDto();

            when(mockProviderRepository.delete(providerDtoRequest.getName())).thenReturn(1);

            providerService.deleteProvider(providerDtoRequest.getName());

        }

        @Test
        public void delete_not_found_name(){
            when(mockProviderRepository.delete("toyota")).thenReturn(0);

            NotFoundException ex = Assertions.assertThrows(NotFoundException.class, () -> providerService.deleteProvider("toyota"));
            Assertions.assertEquals(ex, new NotFoundException(NOT_FOUND_PROVIDER));

        }

    }



}
