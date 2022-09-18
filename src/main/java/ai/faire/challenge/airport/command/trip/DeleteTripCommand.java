package ai.faire.challenge.airport.command.trip;

import ai.faire.challenge.airport.command.BaseCommand;
import ai.faire.challenge.airport.exception.CommandException;
import ai.faire.challenge.airport.exception.DomainRuntimeException;
import ai.faire.challenge.airport.service.AmadeusService;
import ai.faire.challenge.airport.service.TripService;
import com.amadeus.exceptions.ResponseException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DeleteTripCommand extends BaseCommand<String> {

  private DeleteTripCommand.CommandInput commandInput;
  private final AmadeusService amadeusService;
  private final TripService tripService;

  public DeleteTripCommand(AmadeusService amadeusService, TripService tripService) {
    this.amadeusService = amadeusService;
    this.tripService = tripService;
  }

  public DeleteTripCommand setCommandInput(CommandInput commandInput) {
    this.commandInput = commandInput;
    return this;
  }

  @Override
  protected boolean canExecute() throws CommandException {
    return this.commandInput != null;
  }

  @Override
  protected String doExecute() {
    try {
      String tripId = this.amadeusService.deleteTrip(this.commandInput.getId());
      this.tripService.deleteById(tripId);

      return tripId;
    } catch (ResponseException e) {
      throw DomainRuntimeException
        .builder()
        .code("TRE-002")
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .args(new Object[]{e.getMessage()})
        .build();
    }
  }

  @Data
  @AllArgsConstructor
  public static class CommandInput {
    private final String id;
  }

}
