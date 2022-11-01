//package ro.tuc.ds2020.services;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import ro.tuc.ds2020.controllers.handlers.exceptions.model.ResourceNotFoundException;
//import ro.tuc.ds2020.dtos.ConsumptionDTO;
//import ro.tuc.ds2020.dtos.DeviceDTO;
//import ro.tuc.ds2020.dtos.builders.ConsumptionBuilder;
//import ro.tuc.ds2020.entities.Consumption;
//import ro.tuc.ds2020.entities.Device;
//import ro.tuc.ds2020.repositories.ConsumptionRepository;
//import ro.tuc.ds2020.repositories.DeviceRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//public class ConsumptionService {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumptionService.class);
//
//    private final DeviceRepository deviceRepository;
//
//    private final ConsumptionRepository consumptionRepository;
//
//    @Autowired
//    public ConsumptionService(ConsumptionRepository consumptionRepository, DeviceRepository deviceRepository) {
//        this.consumptionRepository = consumptionRepository;
//        this.deviceRepository = deviceRepository;
//    }
//
//    public List<ConsumptionDTO> findConsumptions() {
//        List<Consumption> consumptionList = consumptionRepository.findAll();
//        return consumptionList.stream()
//                .map(ConsumptionBuilder::toConsumptionDTO)
//                .collect(Collectors.toList());
//    }
//
//    public ConsumptionDTO findConsumptionById(UUID id) {
//        Optional<Consumption> prosumerOptional = consumptionRepository.findById(id);
//        if (!prosumerOptional.isPresent()) {
//            LOGGER.error("User with id {} was not found in db", id);
//            throw new ResourceNotFoundException(Device.class.getSimpleName());
//        }
//        return ConsumptionBuilder.toConsumptionDTO(prosumerOptional.get());
//    }
//
//    public UUID insert(ConsumptionDTO consumptionDTO, UUID deviceID) {
//        Consumption consumption = ConsumptionBuilder.toConsumptionEntity(consumptionDTO);
//        Optional<Device> d = deviceRepository.findById(deviceID);
//        if (d.isPresent()) {
//            List<Consumption> consumptions = d.get().getConsumptionList();
//            consumptions.add(consumption);
//            d.get().setConsumptionList(0);
//        }
//    }
//
//}
