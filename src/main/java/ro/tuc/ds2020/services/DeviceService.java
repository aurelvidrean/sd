package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.UserNotFoundException;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class DeviceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();
        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName());
        }
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get());
    }

    public UUID insert(DeviceDTO deviceDTO, UUID userID) {
        Device device = DeviceBuilder.toDeviceEntity(deviceDTO);
        Optional<User> u = userRepository.findById(userID);
        if (u.isPresent()) {
            List<Device> devices = u.get().getDevices();
            devices.add(device);
            u.get().setDevices(devices);
            userRepository.save(u.get());
            LOGGER.debug("User with id {} was inserted in db", device.getId());
            return device.getId();
        } else {
            throw new UserNotFoundException();
        }

    }

    @Transactional
    public UUID mapDeviceToUser(UUID deviceId, UUID userId) {
        Optional<Device> deviceOptional = deviceRepository.findById(deviceId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (deviceOptional.isPresent() && userOptional.isPresent()) {
            Device device = deviceOptional.get();
            User user = userOptional.get();

            List<Device> devices = user.getDevices();
            devices.add(device);
            user.setDevices(devices);
            userRepository.save(user);
            return device.getId();
        }
        else{
            throw new UserNotFoundException();
        }
    }

    public UUID update(UUID id, DeviceDTO deviceDTO) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Update-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        Device deviceToUpdate = prosumerOptional.get();
        deviceToUpdate.setDescription(deviceDTO.getDescription());
        deviceToUpdate.setAddress(deviceDTO.getAddress());
        deviceToUpdate.setConsumptionList(deviceDTO.getConsumptions());
        deviceRepository.save(deviceToUpdate);
        return deviceToUpdate.getId();
    }

    public UUID delete(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Delete-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.delete(prosumerOptional.get());
        return prosumerOptional.get().getId();
    }

}
