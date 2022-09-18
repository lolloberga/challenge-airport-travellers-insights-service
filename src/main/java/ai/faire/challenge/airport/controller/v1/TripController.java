package ai.faire.challenge.airport.controller.v1;

import ai.faire.challenge.airport.command.trip.DeleteTripCommand;
import ai.faire.challenge.airport.command.trip.RegisterTripCommand;
import ai.faire.challenge.airport.model.api.Response;
import ai.faire.challenge.airport.model.dto.RegisterTripDto;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/trips")
public class TripController {

  private final BeanFactory beanFactory;

  public TripController(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }

  @PostMapping("")
  public Response<String> registerTrip(@Validated @RequestBody RegisterTripDto registerTripDto) {
    return Response.<String>builder()
      .code("TRV-000")
      .data(this.beanFactory.getBean(RegisterTripCommand.class)
        .setCommandInput(new RegisterTripCommand.CommandInput(registerTripDto))
        .execute())
      .build();
  }

  @DeleteMapping("/{id}")
  public Response<String> deleteTrip(@PathVariable @NotBlank String id) {
    return Response.<String>builder()
      .code("TRV-001")
      .data(this.beanFactory.getBean(DeleteTripCommand.class)
        .setCommandInput(new DeleteTripCommand.CommandInput(id))
        .execute())
      .build();
  }

}
