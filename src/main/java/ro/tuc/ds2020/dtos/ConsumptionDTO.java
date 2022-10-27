package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;

public class ConsumptionDTO extends RepresentationModel<ConsumptionDTO> {

    private Timestamp timestamp;
    private double energy;

    public ConsumptionDTO() {
    }

    public ConsumptionDTO(Timestamp timestamp, double energy) {
        this.timestamp = timestamp;
        this.energy = energy;
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
