package net.avalith.carDriver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.VehicleModelDtoRequest;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;


@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_models")
public class VehicleModels implements Serializable {

    @Id
    @Column(name = "id_vehicle_model")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "cant_place")
    private Integer cantPlace;

    @Column(name = "is_automatic")
    private Boolean isAutomatic;

    @ManyToOne
    @JoinColumn(name = "id_brand", referencedColumnName = "id_brand")
    @JsonIgnore
    private Brand brand;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private  Timestamp updatedAt;

    @Column(name = "is_active")
    private  Boolean isActive;

    public VehicleModels(VehicleModelDtoRequest vehicleModelDtoRequest, Brand brand){
        this.name = vehicleModelDtoRequest.getName();
        this.cantPlace = vehicleModelDtoRequest.getCantPlace();
        this.isAutomatic = vehicleModelDtoRequest.getIsAutomatic();
        this.brand = brand;
        this.isActive = Boolean.TRUE;
    }
    public VehicleModels VehicleFromDto (VehicleModels vehicleModels, VehicleModelDtoRequest vehicleModelDtoRequest, Brand brand){
        vehicleModels.setName(vehicleModelDtoRequest.getName());
        vehicleModels.setCantPlace(vehicleModelDtoRequest.getCantPlace());
        vehicleModels.setIsAutomatic(vehicleModelDtoRequest.getIsAutomatic());
        vehicleModels.setBrand(brand);
        return vehicleModels;
    }
}