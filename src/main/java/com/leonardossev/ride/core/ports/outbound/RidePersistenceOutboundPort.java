package com.leonardossev.ride.core.ports.outbound;

import com.leonardossev.ride.core.model.Account;
import com.leonardossev.ride.core.model.Ride;
import com.leonardossev.ride.shared.exceptions.PersistenceException;

import java.util.Optional;
import java.util.UUID;

public interface RidePersistenceOutboundPort {

    boolean save(Ride ride) throws PersistenceException;

    boolean update(Ride ride) throws PersistenceException;

    Optional<Ride> getRideByid(UUID id) throws PersistenceException;

}
