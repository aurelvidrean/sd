package ro.tuc.ds2020.dtos.builders;

import ro.tuc.ds2020.dtos.ConsumptionDTO;
import ro.tuc.ds2020.entities.Consumption;

public class ConsumptionBuilder {

    private ConsumptionBuilder() {
    }

    public static ConsumptionDTO toConsumptionDTO(Consumption consumption) {
        return new ConsumptionDTO(consumption.getTimestamp(), consumption.getEnergy());
    }

    public static Consumption toConsumptionEntity(ConsumptionDTO consumptionDTO) {
        return new Consumption(consumptionDTO.getTimestamp(), consumptionDTO.getEnergy());
    }

}
