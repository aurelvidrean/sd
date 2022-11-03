package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.UUID;

public class ConsumptionDTO extends RepresentationModel<ConsumptionDTO> {

    private UUID id;
    private Timestamp timestamp;
    private double energy;

    public ConsumptionDTO() {
    }

    public ConsumptionDTO(Timestamp timestamp, double energy) {
        this.timestamp = timestamp;
        this.energy = energy;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
