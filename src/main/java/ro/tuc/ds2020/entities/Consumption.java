package ro.tuc.ds2020.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "consumptions")
public class Consumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "energy", nullable = false)
    private double energy;

    public Consumption(){
    }

    public Consumption(Long timestamp, double energy) {
        this.timestamp = timestamp;
        this.energy = energy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energy = energyConsumption;
    }
}
