package ai.faire.challenge.airport.model.dto;

import lombok.Data;

@Data
public class AirportInsightsResponse {

  private String airportCode;
  private int totalTravellers;
  private int leisurePurposeTravellers;
  private int leisurePurposeProbability;

}
