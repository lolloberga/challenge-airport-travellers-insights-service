package ai.faire.challenge.airport.service;

import ai.faire.challenge.airport.model.Trip;
import ai.faire.challenge.airport.repository.TripRepository;
import org.springframework.stereotype.Service;

@Service
public class TripService {

  private final TripRepository tripRepository;

  public TripService(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public Trip saveOrUpdate(Trip trip) {
    return this.tripRepository.save(trip);
  }

  public void deleteById(String id) {
    this.tripRepository.deleteById(id);
  }
}
