package ai.faire.challenge.airport.model.dto;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.util.GeneralUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class RegisterTripDto {

  @NotNull(message = "TRO-400") private String originAirportCode;
  @NotNull(message = "TRD-400") private String destinationAirportCode;
  @NotNull(message = "TRDA-400") private String departureDate;
  @NotNull(message = "TRDA-401") private String returnDate;

  public boolean isValid() {
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      sdf.parse(this.departureDate);
      sdf.parse(this.returnDate);
    } catch (ParseException e) {
      return false;
    }
    return GeneralUtils.isIATAcode(this.originAirportCode) && GeneralUtils.isIATAcode(this.destinationAirportCode);
  }

  public Trip buildTrip() {
    return Trip.builder()
      .originAirportCode(this.originAirportCode)
      .destinationAirportCode(this.destinationAirportCode)
      .departureDate(this.departureDate)
      .returnDate(this.returnDate)
      .build();
  }
}
