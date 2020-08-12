package net.avalith.carDriver.models.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.User;

import java.util.Date;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserDtoResponse {

    private String name;

    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String dni;

    private String licenseNumber;

    private Boolean validated;

    public UserDtoResponse(User user) {
        this.name = user.getName();
        this.lastName = user.getLastName();
        this.birthDate = user.getBirthDate();
        this.dni = user.getDni();
        if(user.getLicense()!=null){
            this.licenseNumber = user.getLicense().getNumber();
            this.validated = user.getLicense().getValidated();
        }
    }
}
