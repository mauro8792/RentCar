package net.avalith.carDriver.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.VehicleDtoRequest;
import net.avalith.carDriver.models.enums.Colors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicles")
public class Vehicle implements Serializable {

    @Id
    @Column(name = "id_vehicle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String domain;

    private Boolean available;

    @Enumerated(EnumType.STRING)
    private Colors color;

    @ManyToOne
    @JoinColumn(name = "id_provider", referencedColumnName = "id_provider")
    @JsonIgnore
    private Provider provider;
  
    @ManyToOne
    @JoinColumn(name = "id_vehicle_model", referencedColumnName = "id_vehicle_model")
    @JsonIgnore
    private VehicleModels vehicleModels;

    @ManyToOne
    @JoinColumn(name = "id_category_vehicle", referencedColumnName = "id_category_vehicle")
    @JsonIgnore
    private VehicleCategory categoryVehicles;

    @Column(name = "is_active")
    private Boolean isActive;


    public Vehicle(VehicleDtoRequest vehicleDtoRequest, Provider provider, VehicleModels vehicleModels, VehicleCategory category_vehicles) {
        this.domain = vehicleDtoRequest.getDomain();
        this.available = Boolean.TRUE;
        this.color = vehicleDtoRequest.getColor();
        this.provider = provider;
        this.vehicleModels = vehicleModels;
        this.categoryVehicles = category_vehicles;
        this.isActive = Boolean.TRUE;
    }

    public Vehicle VehicleFromDtoRequest(Vehicle vehicle,VehicleDtoRequest vehicleDtoRequest, Provider provider, VehicleModels vehicleModels, VehicleCategory category_vehicles){
        vehicle.setDomain(vehicleDtoRequest.getDomain());
        vehicle.setColor(vehicleDtoRequest.getColor());
        vehicle.setProvider(provider);
        vehicle.setVehicleModels(vehicleModels);
        vehicle.setCategoryVehicles(category_vehicles);
        return vehicle;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                '}';
    }
}
