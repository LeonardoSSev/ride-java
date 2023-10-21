package com.leonardossev.ride.adapters.outbound.persistence.ride;

import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import com.leonardossev.ride.shared.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Primary
public class PostgreRideClientAdapter implements RidePersistenceOutboundPort {

    @Autowired
    Connection connection;

    @Override
    public boolean save(Ride ride) throws PersistenceException {
        return false;
    }

    @Override
    public boolean update(Ride ride) throws PersistenceException {
        return false;
    }

    @Override
    public Optional<Ride> findById(UUID id) throws PersistenceException {
        return Optional.empty();
    }

    @Override
    public List<Ride> findAllInProgressFromPassenger(UUID passengerId) throws PersistenceException {
        return null;
    }

    @Override
    public List<Ride> findAllInProgressFromDriver(UUID driverId) throws PersistenceException {
        return null;
    }

}
