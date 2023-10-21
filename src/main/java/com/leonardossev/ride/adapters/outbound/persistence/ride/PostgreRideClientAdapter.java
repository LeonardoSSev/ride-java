package com.leonardossev.ride.adapters.outbound.persistence.ride;

import com.leonardossev.ride.adapters.outbound.persistence.model.AccountEntity;
import com.leonardossev.ride.adapters.outbound.persistence.model.RideEntity;
import com.leonardossev.ride.core.model.ride.Ride;
import com.leonardossev.ride.core.ports.outbound.RidePersistenceOutboundPort;
import com.leonardossev.ride.shared.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        PreparedStatement insertStatement = null;
        try {
            RideEntity entity = RideEntity.fromRide(ride);
            insertStatement = this.connection.prepareStatement("INSERT INTO cccat13.ride (ride_id, passenger_id, driver_id, status, from_lat, from_long, to_lat, to_long, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            insertStatement.setObject(1, entity.rideId());
            insertStatement.setObject(2, entity.passengerId());
            if (entity.driverId() != null) {
                insertStatement.setObject(3, entity.driverId());
            } else {
                insertStatement.setObject(3, null);
            }
            insertStatement.setString(4, entity.status());
            insertStatement.setDouble(5, entity.fromLat());
            insertStatement.setDouble(6, entity.fromLong());
            insertStatement.setDouble(7, entity.toLat());
            insertStatement.setDouble(8, entity.toLong());
            insertStatement.setDate(9, entity.createdAt());
            insertStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public boolean update(Ride ride) throws PersistenceException {
        PreparedStatement updateStatement = null;
        try {
            RideEntity entity = RideEntity.fromRide(ride);
            updateStatement = this.connection.prepareStatement("UPDATE ride SET driver_id = ?, status = ?");
            if (entity.driverId() != null) {
                updateStatement.setObject(1, entity.driverId());
            } else {
                updateStatement.setObject(1, null);
            }
            updateStatement.setString(2, entity.status());
            updateStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public Optional<Ride> findById(UUID id) throws PersistenceException {
        PreparedStatement findByIdPreparedStatement = null;
        try {
            findByIdPreparedStatement = this.connection.prepareStatement("SELECT * FROM cccat13.ride WHERE ride_id = ?");
            findByIdPreparedStatement.setObject(1, UUID.fromString(id.toString()));
            ResultSet resultSet = findByIdPreparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(RideEntity.toRide(RideEntity.fromResultSet(resultSet)));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new PersistenceException(e.getMessage());
        }
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
