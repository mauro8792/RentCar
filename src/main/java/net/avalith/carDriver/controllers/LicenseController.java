package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.License;
import net.avalith.carDriver.models.dtos.requests.LicenseDtoRequest;
import net.avalith.carDriver.models.dtos.requests.LicenseDtoRequestUpdate;
import net.avalith.carDriver.models.dtos.responses.LicenseDtoResponse;
import net.avalith.carDriver.services.LicenseService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping(value = Routes.LICENSE)
public class LicenseController {

    @Autowired
    private LicenseService licenseService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LicenseDtoResponse> save(@RequestBody @Valid LicenseDtoRequest license){

        return ResponseEntity.status(HttpStatus.CREATED).body(new LicenseDtoResponse(licenseService.save(license)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<LicenseDtoResponse>> getAll(){
        List<License> licenses = licenseService.getAll();

        if (licenses.isEmpty())
            return ResponseEntity.noContent().build();

        List<LicenseDtoResponse> licenseResponses = licenses.stream()
                .map(LicenseDtoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(licenseResponses);
    }

    @PutMapping(value = Routes.LICENSE_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LicenseDtoResponse> updateExpirationDate(@PathVariable(value = "number") String number, @RequestBody @Valid LicenseDtoRequestUpdate license){

        return ResponseEntity.ok(new LicenseDtoResponse(licenseService.updateExpirationDate(number, license)));
    }
}
