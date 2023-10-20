package com.leonardossev.ride.core.ports.outbound;

import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;
import com.leonardossev.ride.shared.exceptions.PersistenceException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RidePersistenceOutboundPort {

    boolean save(Ride ride) throws PersistenceException;

    boolean update(Ride ride) throws PersistenceException;

    Optional<Ride> findRideById(UUID id) throws PersistenceException;

    List<Ride> findInProgressRidesFromPassenger(UUID passengerId) throws PersistenceException;

    List<Ride> findInProgressRidesFromDriver(UUID driverId) throws PersistenceException;

}
