package net.avalith.carDriver.controllers;

import net.avalith.carDriver.models.User;
import net.avalith.carDriver.models.dtos.UserDtoNoLicense;
import net.avalith.carDriver.models.dtos.requests.UserDtoRequest;
import net.avalith.carDriver.models.dtos.requests.UserDtoUpdateRequest;
import net.avalith.carDriver.models.dtos.responses.DeleteResponseDto;
import net.avalith.carDriver.models.dtos.responses.UserDtoResponse;
import net.avalith.carDriver.services.UserService;
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

import static net.avalith.carDriver.utils.Constants.DELETED_USER;

@RestController
@RequestMapping(value = Routes.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserDtoNoLicense> save(@RequestBody @Valid UserDtoRequest user){

        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDtoNoLicense(userService.save(user)));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserDtoResponse>> getAll(){
        List<User> users = userService.getAll();

        if(users.isEmpty())
            return ResponseEntity.noContent().build();

        List<UserDtoResponse>userResponses = users.stream()
                .map(UserDtoResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }

    @DeleteMapping(value = Routes.USER_DELETE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteResponseDto> delete(@PathVariable(value = "dni") String dni){
        userService.delete(dni);

        return ResponseEntity.ok(new DeleteResponseDto(String.format(DELETED_USER, dni)));
    }

    @PutMapping(value = Routes.USER_UPDATE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserDtoResponse> update(@PathVariable(value = "dni") String dni,
                                                  @RequestBody @Valid UserDtoUpdateRequest user){

        return ResponseEntity.ok(new UserDtoResponse(userService.update(dni, user)));
    }
}
