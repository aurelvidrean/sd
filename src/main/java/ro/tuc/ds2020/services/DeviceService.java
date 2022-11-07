package ro.tuc.ds2020.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
import ro.tuc.ds2020.controllers.handlers.exceptions.model.UserNotFoundException;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.dtos.builders.ConsumptionBuilder;
import ro.tuc.ds2020.dtos.builders.DeviceBuilder;
import ro.tuc.ds2020.entities.Consumption;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.DeviceRepository;
import ro.tuc.ds2020.repositories.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
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
        List<User> users = userRepository.findAll();
        return deviceList.stream().map(device -> {
            UUID ownerId = getOwner(device.getId()).getId();
            return DeviceBuilder.toDeviceDTO(device, ownerId);
        }).collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName());
        }
        UUID userId = getOwner(prosumerOptional.get().getId()).getId();
        return DeviceBuilder.toDeviceDTO(prosumerOptional.get(), userId);
    }

    public UUID insert(DeviceDTO deviceDTO) {
        Device device = DeviceBuilder.toDeviceEntity(deviceDTO);
        List<Consumption> consumptions = dummyConsumptions();
        device.setConsumptionList(consumptions);

        Optional<User> owner = userRepository.findById(UUID.fromString("b4527c7f-c7fb-489a-8786-f332e1d81de4"));
        if(!owner.isPresent()){
            LOGGER.error("User with id {} was not found in db", UUID.fromString("b4527c7f-c7fb-489a-8786-f332e1d81de4"));
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + UUID.fromString("b4527c7f-c7fb-489a-8786-f332e1d81de4"));
        }
        List<Device> devices = owner.get().getDevices();
        devices.add(device);
        owner.get().setDevices(devices);
        userRepository.save(owner.get());
        LOGGER.debug("Device with id {} was inserted in db", device.getId());
        return owner.get().getId();
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

    @Transactional
    public UUID updateDevice(DeviceDTO deviceDTO) {
        Optional<Device> prosumerOptional = deviceRepository.findById(deviceDTO.getId());
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Update-User with id {} was not found in db", deviceDTO.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + deviceDTO.getId());
        }
        Device deviceToUpdate = DeviceBuilder.toDeviceEntity(deviceDTO);
        deviceToUpdate.setId(deviceDTO.getId());
//                prosumerOptional.get();
//        deviceToUpdate.setDescription(deviceDTO.getDescription());
//        deviceToUpdate.setAddress(deviceDTO.getAddress());
//        deviceToUpdate.setConsumptionList(deviceDTO.getConsumptions());
//        deviceToUpdate.setId(deviceDTO.getId());
        deviceRepository.save(deviceToUpdate);
        return deviceToUpdate.getId();
    }

    @Transactional
    public UUID deleteDeviceById(UUID id) {
        Optional<Device> prosumerOptional = deviceRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("Delete-User with id {} was not found in db", id);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + id);
        }
        deviceRepository.delete(prosumerOptional.get());
        return prosumerOptional.get().getId();
    }

    public User getOwner(UUID deviceId) {
        List<User> allUsers = userRepository.findAll();
        for(User user: allUsers) {
            List<Device> devices = user.getDevices();
            for(Device device: devices) {
                if(device.getId().equals(deviceId)) {
                    return user;
                }
            }
        }
        return null;
    }

    private List<Consumption> dummyConsumptions() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime endDate = start.minusHours(24);
        List<Consumption> consumptions = new ArrayList<>();
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Random rand = new Random();

        for (LocalDateTime date = start; date.isBefore(endDate); date = date.plusHours(1)) {
            double energy = rand.nextDouble();
            consumptions.add(new Consumption(date.toEpochSecond(zoneOffset), energy));
        }

        return consumptions;
    }

}
