package net.avalith.carDriver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.UserDtoRequest;
import net.avalith.carDriver.models.dtos.requests.UserDtoUpdateRequest;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "pwd")
    private String pwd;

    @Column(name = "birth_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthDate;

    @Column(name = "dni", unique = true)
    private String dni;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToOne
    @JoinColumn(name = "id_license", referencedColumnName = "id_license")
    private License license;

    public User (UserDtoRequest userDtoRequest){
        name = userDtoRequest.getName();
        lastName = userDtoRequest.getLastName();
        pwd = userDtoRequest.getPwd();
        birthDate = userDtoRequest.getBirthDate();
        dni = userDtoRequest.getDni();
        isActive = Boolean.TRUE;
    }

    public User (UserDtoUpdateRequest userDtoUpdateRequest){
        name = userDtoUpdateRequest.getName();
        lastName = userDtoUpdateRequest.getLastName();
        pwd = userDtoUpdateRequest.getPwd();
        isActive = Boolean.TRUE;
        birthDate = userDtoUpdateRequest.getBirthDate();
    }
}
