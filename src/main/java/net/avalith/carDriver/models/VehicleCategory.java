package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.VehicleCategoryDtoRequest;
import net.avalith.carDriver.models.enums.VehicleCategoryEnum;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vehicle_categories")
public class VehicleCategory implements Serializable {

    @Id
    @Column(name = "id_category_vehicle")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VehicleCategoryEnum name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "price_hour")
    private Double priceHour;

    @Column(name = "price_day")
    private Double priceDay;

    @Column(name = "price_week")
    private Double priceWeek;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private  Timestamp updatedAt;

    public VehicleCategory(VehicleCategoryDtoRequest vehicleCategoryDtoRequest) {
        this.name = vehicleCategoryDtoRequest.getName();
        this.isActive = Boolean.TRUE;
        this.priceHour = vehicleCategoryDtoRequest.getPriceHour();
        this.priceDay = vehicleCategoryDtoRequest.getPriceDay();
        this.priceWeek = vehicleCategoryDtoRequest.getPriceWeek();
    }
}