package net.avalith.carDriver.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.avalith.carDriver.models.dtos.requests.MishapDtoRequest;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mishaps")
public class Mishap implements Serializable {

    @Id
    @Column(name = "id_mishap")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @OneToOne
    @JoinColumn(name = "id_ride", referencedColumnName = "id_ride")
    private Ride ride;

    public Mishap (MishapDtoRequest mishap){
        this.title = mishap.getTitle();
        this.description = mishap.getDescription();
    }
}
