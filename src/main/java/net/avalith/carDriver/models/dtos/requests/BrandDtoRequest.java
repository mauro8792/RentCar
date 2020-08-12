package net.avalith.carDriver.models.dtos.requests;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BrandDtoRequest {

    @NotBlank(message = "The name is required")
    private String name;

    private Boolean isActive;

    public BrandDtoRequest(String name) {
        this.name = name;
        this.isActive = Boolean.TRUE;
    }
}
