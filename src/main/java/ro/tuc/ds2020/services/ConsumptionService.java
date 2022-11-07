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
import ro.tuc.ds2020.entities.Consumption;
import ro.tuc.ds2020.entities.Device;
import ro.tuc.ds2020.entities.User;
import ro.tuc.ds2020.repositories.ConsumptionRepository;
import ro.tuc.ds2020.repositories.DeviceRepository;

import javax.transaction.Transactional;
import java.net.UnknownServiceException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConsumptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumptionService.class);

    private final DeviceRepository deviceRepository;

    private final ConsumptionRepository consumptionRepository;

    @Autowired
    public ConsumptionService(ConsumptionRepository consumptionRepository, DeviceRepository deviceRepository) {
        this.consumptionRepository = consumptionRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public List<ConsumptionDTO> findConsumptions() {
        List<Consumption> consumptionList = consumptionRepository.findAll();
        return consumptionList.stream()
                .map(ConsumptionBuilder::toConsumptionDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsumptionDTO findConsumptionById(UUID id) {
        Optional<Consumption> prosumerOptional = consumptionRepository.findById(id);
        if (!prosumerOptional.isPresent()) {
            LOGGER.error("User with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName());
        }
        return ConsumptionBuilder.toConsumptionDTO(prosumerOptional.get());
    }

    @Transactional
    public UUID insert(ConsumptionDTO consumptionDTO) {
        Consumption consumption = ConsumptionBuilder.toConsumptionEntity(consumptionDTO);
        consumption = consumptionRepository.save(consumption);
        LOGGER.debug("Consumption with id {} was inserted in db", consumption.getId());
        return consumption.getId();
    }

    @Transactional
    public UUID deleteConsumptionById(UUID id) {
        consumptionRepository.deleteById(id);
        return id;
    }

    @Transactional
    public UUID updateConsumption(ConsumptionDTO consumptionDTO) {
        Optional<Consumption> consumptionOptional = consumptionRepository.findById(consumptionDTO.getId());
        if (!consumptionOptional.isPresent()) {
            LOGGER.error("Consumption with id {} was not found in db", consumptionDTO.getId());
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + consumptionDTO.getId());
        }
        Consumption consumption = ConsumptionBuilder.toConsumptionEntity(consumptionDTO);
        consumption.setId(consumptionDTO.getId());
        consumption = consumptionRepository.save(consumption);
        return consumption.getId();
    }

}
