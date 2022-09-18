package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.exception.DomainRuntimeException;
import ai.faire.challenge.airport.infrastructure.AmadeusConfiguration;
import ai.faire.challenge.airport.model.dto.RegisterTripDto;
import ai.faire.challenge.airport.util.GeneralUtils;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.Response;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.amadeus.resources.FlightPrice;
import com.amadeus.resources.Prediction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AmadeusService {

  private final AmadeusConfiguration configuration;
  private final Amadeus amadeus;

  private final static Logger LOGGER = Logger.getLogger(AmadeusService.class.getName());

  public AmadeusService(AmadeusConfiguration configuration) {
    this.configuration = configuration;
    this.amadeus = Amadeus.builder(this.configuration.getApiKey(), this.configuration.getApiSecret())
      .setHostname(this.configuration.getEnvironment())
      .setLogger(LOGGER)
      .setLogLevel(this.configuration.getLoglevel())
      .build();
  }

  /**
   * Register a new trip.
   *
   * @param registerTripDto DTO with the necessary information about the trip
   * @return The id of the registered trip.
   */
  public String registerTrip(RegisterTripDto registerTripDto) throws ResponseException {

    FlightOrder.Traveler traveler = new FlightOrder.Traveler();
    //traveler.setId(GeneralUtils.generateRandomUniqueId());
    traveler.setId("1");
    traveler.setDateOfBirth("2000-04-14");
    traveler.setName(new FlightOrder.Name("JORGE", "GONZALES"));

    FlightOrder.Phone[] phone = new FlightOrder.Phone[1];
    phone[0] = new FlightOrder.Phone();
    phone[0].setCountryCallingCode("33");
    phone[0].setNumber("675426222");

    FlightOrder.Contact contact = new FlightOrder.Contact();
    contact.setPhones(phone);
    contact.setDeviceType("MOBILE");
    traveler.setContact(contact);

    FlightOrder.Document[] document = new FlightOrder.Document[1];
    document[0] = new FlightOrder.Document();
    document[0].setDocumentType("PASSPORT");
    document[0].setNumber("480080076");
    document[0].setExpiryDate("2022-10-11");
    document[0].setIssuanceCountry("ES");
    document[0].setNationality("ES");
    document[0].setHolder(true);
    traveler.setDocuments(document);

    FlightOrder.Traveler[] travelers = new FlightOrder.Traveler[1];
    travelers[0] = traveler;
    System.out.println(travelers[0]);

    FlightOfferSearch[] flightOfferSearches = this.amadeus.shopping.flightOffersSearch.get(
      Params.with("originLocationCode", registerTripDto.getOriginAirportCode())
        .and("destinationLocationCode", registerTripDto.getDestinationAirportCode())
        .and("departureDate", registerTripDto.getDepartureDate())
        .and("returnDate", registerTripDto.getReturnDate())
        .and("adults", 1)
        .and("max", 3)
    );

    if (flightOfferSearches == null || flightOfferSearches.length <= 0)
      throw DomainRuntimeException.builder()
        .code("TRE-003")
        .status(HttpStatus.NOT_FOUND)
        .build();

    // Take the 1st flight of the list to confirm the price and the availability
    FlightPrice flightPricing = amadeus.shopping.flightOffersSearch.pricing.post(
      flightOfferSearches[0]);

    // Book the flight previously priced
    //FlightOrder order = amadeus.booking.flightOrders.post(flightPricing, travelers);
    FlightOrder order = amadeus.booking.flightOrders.post(this.addDeviceType(flightPricing, travelers));

    return order.getId();
  }

  /**
   * Add device type to Phone object. This method was created beacuse the API version of Amadeus library
   * doesn't support this mandatory parameter (deviceType)
   *
   * @param flightPricing
   * @param travelers
   * @return
   */
  private JsonObject addDeviceType(FlightPrice flightPricing, FlightOrder.Traveler[] travelers) {
    JsonObject typeObject = new JsonObject();
    typeObject.addProperty("type", "flight-order");

    Gson gson = new GsonBuilder().create();
    JsonArray flightOffersArray = GeneralUtils.buildFlightOffersJSON(flightPricing.getFlightOffers());
    typeObject.add("flightOffers", flightOffersArray);

    // Build Traveler JSON
    JsonArray travelerArray = GeneralUtils.buildTravelersJSON(travelers);
    typeObject.add("travelers", travelerArray);

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("data", typeObject);

    return jsonObject;
  }

  /**
   * Delete an existing trip.
   *
   * @param tripId Id of the trip
   * @return The id of the deleted trip.
   */
  public String deleteTrip(String tripId) throws ResponseException {

    Response order = this.amadeus.booking.flightOrder(tripId).delete();
    if (!String.valueOf(order.getStatusCode()).startsWith("20"))
      throw new ResponseException(order);

    return tripId;
  }

  public Prediction tripPurposePrediction() throws ResponseException {

    Prediction tripPurpose = this.amadeus.travel.predictions.tripPurpose.get(Params
      .with("originLocationCode", "NYC")
      .and("destinationLocationCode", "MAD")
      .and("departureDate", "2022-08-01")
      .and("returnDate", "2022-08-12"));

    if (!String.valueOf(tripPurpose.getResponse().getStatusCode()).startsWith("20"))
      throw new ResponseException(tripPurpose.getResponse());

    return tripPurpose;
  }

}
