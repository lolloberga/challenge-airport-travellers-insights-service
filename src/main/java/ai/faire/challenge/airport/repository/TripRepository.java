package ai.faire.challenge.airport.repository;

import ai.faire.challenge.airport.model.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Trip, String> {
}
