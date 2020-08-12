package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProviderDtoRequest {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The email is required")
    private String email;

    @NotBlank(message = "The business name is required")
    private String businessName;

    @NotBlank(message = "The phone is required")
    private String phone;

    @NotBlank(message = "The password is required")
    private String password;

    @NotNull(message = "The commission is required")
    private Double commission;
}
