package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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

@Data
@Entity
@Builder
@Table(name = "sales")
@NoArgsConstructor
@AllArgsConstructor
public class Sale implements Serializable {

    @Id
    @Column(name = "id_sale")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profit")
    private Double profit;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "id_ride", referencedColumnName = "id_ride")
    private Ride ride;

    public Sale(Double profit, Long idRide) {
        this.profit = profit;
        this.ride = Ride.builder().id(idRide).build();
    }
}
