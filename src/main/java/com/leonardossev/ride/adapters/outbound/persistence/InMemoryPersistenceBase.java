package com.leonardossev.ride.adapters.outbound.persistence;

import com.leonardossev.ride.adapters.outbound.persistence.model.AccountEntity;
import com.leonardossev.ride.adapters.outbound.persistence.model.RideEntity;
import com.leonardossev.ride.core.model.ride.Ride;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InMemoryPersistenceBase {

    protected static final Map<UUID, AccountEntity> accounts = new ConcurrentHashMap<>();

    protected static final Map<UUID, RideEntity> rides = new ConcurrentHashMap<>();

}
