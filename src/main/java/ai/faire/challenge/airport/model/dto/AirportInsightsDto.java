package ai.faire.challenge.airport.model.dto;

import ai.faire.challenge.airport.util.GeneralUtils;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Data
public class AirportInsightsDto {

  @NotNull(message = "TRO-400") private String airportCode;
  @NotNull(message = "TRO-400") private String date;

  public boolean isValid() {
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    try {
      sdf.parse(this.date);
    } catch (ParseException e) {
      return false;
    }
    return GeneralUtils.isIATAcode(this.airportCode);

  }

}
