package ro.tuc.ds2020.dtos;

import org.springframework.hateoas.RepresentationModel;
import ro.tuc.ds2020.entities.Consumption;

import java.util.List;
import java.util.UUID;

public class DeviceDTO extends RepresentationModel<DeviceDTO> {

    private UUID id;
    private String description;
    private String address;
    private List<Consumption> consumptions;

    private UUID ownerId;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String description, String address, List<Consumption> consumptions) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.consumptions = consumptions;
    }

    public DeviceDTO(UUID id, String description, String address, List<Consumption> consumptions, UUID userId) {
        this.id = id;
        this.description = description;
        this.address = address;
        this.consumptions = consumptions;
        this.ownerId = userId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<Consumption> consumptions) {
        this.consumptions = consumptions;
    }
}
