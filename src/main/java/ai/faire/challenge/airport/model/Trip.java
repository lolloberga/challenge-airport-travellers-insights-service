package ai.faire.challenge.airport.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRIP")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Trip {

  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "ORIGIN_AIRPORT_CODE")
  private String originAirportCode;

  @Column(name = "DESTINATION_AIRPORT_CODE")
  private String destinationAirportCode;

  @Column(name = "DEPARTURE_DATE")
  private String departureDate;

  @Column(name = "RETURN_DATE")
  private String returnDate;

}
