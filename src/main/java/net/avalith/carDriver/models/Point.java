package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.PointDtoRequest;
import net.avalith.carDriver.models.dtos.requests.PointDtoUpdateRequest;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "points")
@NoArgsConstructor
@AllArgsConstructor
public class Point implements Serializable {

    @Id
    @Column(name = "id_point")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_origin")
    private Boolean isOrigin;

    @Column(name = "is_destination")
    private Boolean isDestination;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city", referencedColumnName = "id_city")
    private City city;

    public Point (PointDtoRequest pointDtoRequest, City city){
        isOrigin = pointDtoRequest.getIsOrigin();
        lat = pointDtoRequest.getLat();
        lng = pointDtoRequest.getLng();
        capacity = pointDtoRequest.getCapacity();
        stock = pointDtoRequest.getStock();
        this.city = city;
        isActive = Boolean.TRUE;
    }

    public Point (PointDtoUpdateRequest pointDtoUpdateRequest, City city){
        isOrigin = pointDtoUpdateRequest.getIsOrigin();
        isDestination = pointDtoUpdateRequest.getIsDestination();
        lat = pointDtoUpdateRequest.getLat();
        lng = pointDtoUpdateRequest.getLng();
        capacity = pointDtoUpdateRequest.getCapacity();
        stock = pointDtoUpdateRequest.getStock();
        this.city = city;
    }
}
