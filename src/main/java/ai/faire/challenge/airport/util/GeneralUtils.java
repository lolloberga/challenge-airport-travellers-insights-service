package ai.faire.challenge.airport.util;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOrder;
import com.google.gson.*;

import java.util.UUID;

public final class GeneralUtils {

  public GeneralUtils() {
  }

  public static boolean isIATAcode(String code) {
    return code != null && code.length() == 3 && code.matches("[a-zA-Z]+");
  }

  public static String generateRandomUniqueId() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  public static JsonArray buildFlightOffersJSON(FlightOfferSearch[] flightOffers) {
    Gson gson = new GsonBuilder().create();
    JsonArray flightOffersArray = new JsonArray();

    for (FlightOfferSearch offer : flightOffers) {
      JsonElement flightOffer = gson.toJsonTree(offer, FlightOfferSearch.class);
      flightOffersArray.add(flightOffer);
    }
    return flightOffersArray;
  }

  public static JsonArray buildTravelersJSON(FlightOrder.Traveler[] travelers) {
    Gson gson = new GsonBuilder().create();
    JsonArray travelerArray = new JsonArray();

    for (FlightOrder.Traveler value : travelers) {
      JsonElement traveler = gson.toJsonTree(value, FlightOrder.Traveler.class);
      // Add device type
      JsonArray phonesArray = traveler.getAsJsonObject().get("contact").getAsJsonObject().get("phones").getAsJsonArray();
      for (int i = 0; i < phonesArray.size(); i++) {
        JsonElement phone = phonesArray.get(i);
        phone.getAsJsonObject().addProperty("deviceType", "MOBILE");
      }
      travelerArray.add(traveler);
    }
    return travelerArray;
  }

}
