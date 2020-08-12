package net.avalith.carDriver.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.enums.RideState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@Entity
@NoArgsConstructor
@Table(name = "rides_logs")
public class RideLog {

    @Id
    @Column(name = "id_ride_log")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_ride")
    private Long idRide;

    @Enumerated(EnumType.STRING)
    private RideState state;

    @Column(name = "lat")
    private String lat;

    @Column(name = "lng")
    private String lng;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public RideLog(Long idRide, RideState state){
        this.idRide = idRide;
        this.state = state;
    }

    public RideLog(Long idRide, RideState state, String lat, String lng){
        this.idRide = idRide;
        this.state = state;
        this.lat = lat;
        this.lng = lng;
    }
}
