package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.Device;

import java.util.UUID;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device, UUID userId) {
        return new DeviceDTO(device.getId(), device.getDescription(), device.getAddress(), device.getConsumptionList(), userId);
    }

    public static Device toDeviceEntity(DeviceDTO deviceDTO) {
        return new Device(deviceDTO.getDescription(), deviceDTO.getAddress(), deviceDTO.getConsumptions());
    }

}
