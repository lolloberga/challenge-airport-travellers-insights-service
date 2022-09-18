package ai.faire.challenge.airport.command.airport;

import ai.faire.challenge.airport.command.BaseCommand;
import ai.faire.challenge.airport.exception.CommandException;
import ai.faire.challenge.airport.model.dto.AirportInsightsDto;
import ai.faire.challenge.airport.model.dto.AirportInsightsResponse;
import ai.faire.challenge.airport.service.AmadeusService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GetAirportInsightsCommand extends BaseCommand<AirportInsightsResponse> {

  private GetAirportInsightsCommand.CommandInput commandInput;
  private final AmadeusService amadeusService;

  public GetAirportInsightsCommand(AmadeusService amadeusService) {
    this.amadeusService = amadeusService;
  }

  public GetAirportInsightsCommand setCommandInput(GetAirportInsightsCommand.CommandInput commandInput) {
    this.commandInput = commandInput;
    return this;
  }

  @Override
  protected boolean canExecute() throws CommandException {
    return this.commandInput != null && this.commandInput.getAirportInsightsDto() != null
      && this.commandInput.getAirportInsightsDto().isValid();
  }

  @Override
  protected AirportInsightsResponse doExecute() {
    return null;
  }

  @Data
  @AllArgsConstructor
  public static class CommandInput {
    private final AirportInsightsDto airportInsightsDto;
  }
}
