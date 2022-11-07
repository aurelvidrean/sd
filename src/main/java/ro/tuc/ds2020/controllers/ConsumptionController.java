package ro.tuc.ds2020.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.services.ConsumptionService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin
@RequestMapping(value = "/consumption")
public class ConsumptionController {

    private final ConsumptionService consumptionService;

    @Autowired
    public ConsumptionController(ConsumptionService consumptionService) {
        this.consumptionService = consumptionService;
    }

    @GetMapping()
    public ResponseEntity<List<ConsumptionDTO>> getConsumptions() {
        List<ConsumptionDTO> dtos = consumptionService.findConsumptions();
        for (ConsumptionDTO dto : dtos) {
            Link consumptionLink = linkTo(methodOn(ConsumptionController.class)
                    .getConsumption(dto.getId())).withRel("consumptionDetails");
            dto.add(consumptionLink);
        }
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsumptionDTO> getConsumption(@PathVariable("id") UUID consumptionId) {
        ConsumptionDTO dto = consumptionService.findConsumptionById(consumptionId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> insertConsumption(@Valid @RequestBody ConsumptionDTO consumptionDTO) {
        UUID consumptionId = consumptionService.insert(consumptionDTO);
        return new ResponseEntity<>(consumptionId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<UUID> deleteConsumption(@PathVariable("id") UUID consumptionId) {
        UUID deletedConsumption = consumptionService.deleteConsumptionById(consumptionId);
        return new ResponseEntity<>(deletedConsumption, HttpStatus.OK);

    }

    @PutMapping()
    public ResponseEntity<UUID> updateConsumption(@Valid @RequestBody ConsumptionDTO consumptionDTO) {
        UUID consumptionId = consumptionService.updateConsumption(consumptionDTO);
        return new ResponseEntity<>(consumptionId, HttpStatus.OK);
    }

}
