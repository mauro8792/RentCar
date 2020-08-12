package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.Mishap;
import net.avalith.carDriver.models.dtos.responses.MishapDtoResponse;
import net.avalith.carDriver.services.MishapService;
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
@RequestMapping(value = Routes.MISHAP)
public class MishapController {

    @Autowired
    private MishapService mishapService;

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<MishapDtoResponse>> getAll(){
        List<Mishap> listMishaps = mishapService.getAll();
        if (listMishaps.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            List<MishapDtoResponse> listMishapResponse = listMishaps.stream()
                    .map(MishapDtoResponse::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(listMishapResponse);
        }
    }
}
