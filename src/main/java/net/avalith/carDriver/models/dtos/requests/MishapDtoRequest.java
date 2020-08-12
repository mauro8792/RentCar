package net.avalith.carDriver.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MishapDtoRequest {

    @NotBlank(message = "The title is required")
    private String title;

    @NotBlank(message = "The description is required")
    private String description;
}
