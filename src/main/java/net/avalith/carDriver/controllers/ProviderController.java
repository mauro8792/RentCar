package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Provider;
import net.avalith.carDriver.models.dtos.requests.ProviderDtoRequest;
import net.avalith.carDriver.models.dtos.responses.DeleteResponseDto;
import net.avalith.carDriver.models.dtos.responses.ProviderDtoResponse;
import net.avalith.carDriver.services.ProviderService;
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

import static net.avalith.carDriver.utils.Constants.DELETED_PROVIDER;

@RestController
@RequestMapping(value = Routes.PROVIDER)
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ProviderDtoResponse>>getAll(/*@RequestHeader(value = "API_KEY", required = false) String apiKey*/){

        List<Provider> listProviders = providerService.getAll();
        if (listProviders.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(listProviders.stream()
                .map(ProviderDtoResponse::new)
                .collect(Collectors.toList()));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProviderDtoResponse> save(/*@RequestHeader(value = "API_KEY", required = false) String apiKey, */@RequestBody @Valid ProviderDtoRequest provider){

        return ResponseEntity.status(HttpStatus.CREATED).body(new ProviderDtoResponse(providerService.save(provider)));
    }

    @PutMapping(value = Routes.PROVIDER_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ProviderDtoResponse> update(/*@RequestHeader(value = "API_KEY", required = false) String apiKey, */@PathVariable("name") String name, @RequestBody ProviderDtoRequest provider){

        return ResponseEntity.ok(new ProviderDtoResponse(providerService.update(name, provider)));
    }

    @DeleteMapping(value = Routes.PROVIDER_DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteResponseDto> delete(/*@RequestHeader(value = "API_KEY", required = false) String apiKey, */@PathVariable("name") String name){
        providerService.deleteProvider(name);

        return ResponseEntity.ok(new DeleteResponseDto(String.format(DELETED_PROVIDER, name)));
    }

}
