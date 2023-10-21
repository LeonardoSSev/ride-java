package com.leonardossev.ride.adapters.outbound.persistence.ride;

import com.leonardossev.ride.adapters.outbound.persistence.InMemoryPersistenceBase;
import com.leonardossev.ride.adapters.outbound.persistence.account.InMemoryAccountClientAdapter;
import com.leonardossev.ride.adapters.outbound.persistence.model.RideEntity;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.model.ride.RideStatus;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import com.leonardossev.ride.shared.exceptions.PersistenceException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryRideClientAdapter extends InMemoryPersistenceBase implements RidePersistenceOutboundPort {

    private final RideStatus[] IN_PROGRESS_STATUS = { RideStatus.REQUESTED, RideStatus.IN_PROGRESS, RideStatus.ACCEPTED };

    @Override
    public boolean save(Ride ride) {
        RideEntity rideEntity = RideEntity.fromRide(ride);
        InMemoryRideClientAdapter.rides.put(ride.getId(), rideEntity);
        return true;
    }

    @Override
    public boolean update(Ride ride) {
        RideEntity rideEntity = RideEntity.fromRide(ride);
        InMemoryRideClientAdapter.rides.put(ride.getId(), rideEntity);
        return true;
    }

    @Override
    public Optional<Ride> findById(UUID id) {
        RideEntity rideEntity = InMemoryAccountClientAdapter.rides.get(id);

        return Optional.ofNullable(RideEntity.toRide(rideEntity));
    }

    @Override
    public List<Ride> findAllInProgressFromPassenger(UUID passengerId) {
        return InMemoryRideClientAdapter.rides
                .values()
                .stream()
                .filter(rideEntity -> rideEntity.passengerId().equals(passengerId))
                .filter(this::isInProgress)
                .map(RideEntity::toRide)
                .toList();
    }

    @Override
    public List<Ride> findAllInProgressFromDriver(UUID driverId) throws PersistenceException {
        return InMemoryRideClientAdapter.rides
                .values()
                .stream()
                .filter(rideEntity -> rideEntity.driverId().equals(driverId))
                .filter(this::isInProgress)
                .map(RideEntity::toRide)
                .toList();
    }

    private boolean isInProgress(RideEntity rideEntity) {
        return Arrays.asList(this.IN_PROGRESS_STATUS)
                .contains(RideStatus.valueOf(rideEntity.status()));
    }
}
