package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.dtos.DeviceDTO;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.services.ConsumptionService;
import ro.tuc.ds2020.services.DeviceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/device")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService, ConsumptionService consumptionService) {
        this.deviceService = deviceService;
    }

    @GetMapping()
    public ResponseEntity<List<DeviceDTO>> getDevices() {
        List<DeviceDTO> dtos = deviceService.findDevices();
        for (DeviceDTO dto : dtos) {
            Link userLink = linkTo(methodOn(DeviceController.class)
                    .getDevice(dto.getId())).withRel("deviceDetails");
            dto.add(userLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DeviceDTO> getDevice(@PathVariable("id") UUID deviceId) {
        DeviceDTO dto = deviceService.findDeviceById(deviceId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID deviceID = deviceService.insert(deviceDTO);
        return new ResponseEntity<>(deviceID, HttpStatus.CREATED);
    }

    @PutMapping("/map/{deviceId}/{userId}")
    public ResponseEntity<UUID> mapDeviceToUser(@PathVariable("deviceId") UUID deviceId, @PathVariable("userId") UUID userId) {
        UUID id = deviceService.mapDeviceToUser(deviceId, userId);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping()
    public ResponseEntity<UUID> updateDevice(@Valid @RequestBody DeviceDTO deviceDTO) {
        UUID updatedUser = deviceService.updateDevice(deviceDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteDevice(@PathVariable("id") UUID deviceId) {
        UUID deletedDevice = deviceService.deleteDeviceById(deviceId);
        return new ResponseEntity<>(deletedDevice, HttpStatus.OK);

    }
}