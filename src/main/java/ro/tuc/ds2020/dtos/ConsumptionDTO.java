package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.UUID;

public class ConsumptionDTO extends RepresentationModel<ConsumptionDTO> {

    private UUID id;
    private Long timestamp;
    private double energy;

    public ConsumptionDTO() {
    }

    public ConsumptionDTO(UUID id, Long timestamp, double energy) {
        this.id = id;
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

    public void setEnergy(double energy) {
        this.energy = energy;
    }
}
