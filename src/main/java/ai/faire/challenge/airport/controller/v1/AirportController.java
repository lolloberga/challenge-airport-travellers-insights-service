package ai.faire.challenge.airport.controller.v1;

import ai.faire.challenge.airport.command.airport.GetAirportInsightsCommand;
import ai.faire.challenge.airport.model.api.Response;
import ai.faire.challenge.airport.model.dto.AirportInsightsDto;
import ai.faire.challenge.airport.model.dto.AirportInsightsResponse;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/airport")
public class AirportController {

  private final BeanFactory beanFactory;

  public AirportController(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @GetMapping("/insights")
  public Response<AirportInsightsResponse> airportInsights(@Validated @RequestBody AirportInsightsDto airportInsightsDto) {
    return Response.<AirportInsightsResponse>builder()
      .data(this.beanFactory.getBean(GetAirportInsightsCommand.class)
        .setCommandInput(new GetAirportInsightsCommand.CommandInput(airportInsightsDto))
        .execute())
      .build();
  }

}
