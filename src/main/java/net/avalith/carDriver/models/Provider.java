package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.ProviderDtoRequest;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "providers")
public class Provider implements Serializable {

    @Id
    @Column(name = "id_provider")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String businessName;

    @Column(unique = true)
    private String phone;

    @Column(name = "commission")
    private Double commission;

    @Column(name = "password")
    private String password;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private  Timestamp updatedAt;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Provider(String name) {
        this.name = name;
    }

    public Provider(ProviderDtoRequest providerDtoRequest) {
        this.name = providerDtoRequest.getName();
        this.businessName= providerDtoRequest.getBusinessName();
        this.email = providerDtoRequest.getEmail();
        this.password = providerDtoRequest.getPassword();
        this.phone = providerDtoRequest.getPhone();
        this.commission = providerDtoRequest.getCommission();
        this.isActive = Boolean.TRUE;
    }
}
