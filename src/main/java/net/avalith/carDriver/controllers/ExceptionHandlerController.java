package net.avalith.carDriver.controllers;

import net.avalith.carDriver.exceptions.AlreadyExistsException;
import net.avalith.carDriver.exceptions.InvalidLicenseException;
import net.avalith.carDriver.exceptions.InvalidRequestException;
import net.avalith.carDriver.exceptions.NotFoundException;
import net.avalith.carDriver.models.dtos.responses.ErrorResponseDto;
import net.avalith.carDriver.models.dtos.responses.NotValidFieldResponse;
import net.avalith.carDriver.models.dtos.responses.NotValidResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<NotValidResponse> handleNotValidException(MethodArgumentNotValidException exception){

        List<FieldError> errors = exception.getBindingResult().getFieldErrors();
        List<NotValidFieldResponse> responses = errors
                .stream()
                .map(e -> NotValidFieldResponse.builder()
                        .field(e.getField())
                        .message(e.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity
                .badRequest()
                .body(NotValidResponse.builder()
                        .errors(responses)
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException exception){

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyExistsException(AlreadyExistsException exception){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestException(InvalidRequestException exception){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(InvalidLicenseException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidLicenseException(InvalidLicenseException exception){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseDto.builder()
                        .message(exception.getMessage())
                        .build());
    }
}
