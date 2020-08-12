package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Sale;
import net.avalith.carDriver.models.dtos.responses.SaleDtoResponse;
import net.avalith.carDriver.services.SaleService;
import net.avalith.carDriver.utils.Routes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Routes.SALES)
public class SaleController {

    @Autowired
    private SaleService saleService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<SaleDtoResponse>> getAll(){
        List<Sale> salesAux = saleService.getAll();

        if(salesAux.isEmpty())
            return ResponseEntity.noContent().build();

        return ResponseEntity.ok(salesAux.stream()
                .map(SaleDtoResponse::new)
                .collect(Collectors.toList()));
    }

}
