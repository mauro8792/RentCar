package net.avalith.carDriver.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.RideDtoRequest;
import net.avalith.carDriver.models.dtos.requests.RideDtoUpdateRequest;
import net.avalith.carDriver.models.enums.RideState;
import net.avalith.carDriver.models.enums.TariffType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Builder
@Table(name = "rides")
@NoArgsConstructor
@AllArgsConstructor
public class Ride implements Serializable {

    @Id
    @Column(name = "id_ride")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private RideState state;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    private TariffType tariffType;

    @Column(name = "price")
    private Double price;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehicle", referencedColumnName = "id_vehicle")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_origin_point", referencedColumnName = "id_point")
    private Point originPoint;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_destination_point", referencedColumnName = "id_point")
    private Point destinationPoint;

    @OneToOne
    @JoinColumn(name = "id_user")
    private User user;

    public Ride (RideDtoRequest rideDto, Vehicle vehicle, Point point, User user){
        startDate = rideDto.getStartDate();
        endDate = rideDto.getEndDate();
        state = RideState.RESERVED;
        tariffType = rideDto.getTariffType();
        this.vehicle = vehicle;
        this.originPoint = point;
        this.user = user;
    }

    public Ride (RideDtoUpdateRequest rideDto, Vehicle vehicle, Point originPoint, Point destinationPoint, User user){
        startDate = rideDto.getStartDate();
        endDate = rideDto.getEndDate();
        tariffType = rideDto.getTariffType();
        this.vehicle = vehicle;
        this.originPoint = originPoint;
        this.destinationPoint = destinationPoint;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ride{" +
                "id=" + id +
                '}';
    }
}
