package com.leonardossev.ride.core.services;

import com.leonardossev.ride.core.validators.ICpfValidator;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;

public class AccountService {

    ICpfValidator cpfValidator;

    public AccountService(ICpfValidator cpfValidator) {
        this.cpfValidator = cpfValidator;
    }

    public void sendEmail(String email, String subject, String message) {
        System.out.println(email + " " + subject + " " + message);
    }

    public Map<String, Object> signup(Map<String, Object> input) throws Exception {
        Connection connection = null;
        try {
            String accountId = UUID.randomUUID().toString();
            String verificationCode = UUID.randomUUID().toString();
            LocalDateTime date = LocalDateTime.now();

            // Conecta ao banco de dados PostgreSQL
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "postgres", "123456");

            // Verifica se a conta já existe
            PreparedStatement existingAccountStatement = connection.prepareStatement("SELECT * FROM cccat13.account WHERE email = ?");
            existingAccountStatement.setString(1, input.get("email").toString());
            ResultSet existingAccountResultSet = existingAccountStatement.executeQuery();

            if (!existingAccountResultSet.next()) {
                // Valida nome
                if (input.get("name").toString().matches("[a-zA-Z]+ [a-zA-Z]+")) {
                    // Valida e-mail
                    if (input.get("email").toString().matches("^(.+)@(.+)$")) {
                        // Valida CPF
                        this.cpfValidator.validate(input.get("cpf").toString());
                        // Valida placa do carro
                        if (input.get("isDriver") != null && input.get("isDriver").equals(true)) {
                            if (input.get("carPlate") != null && input.get("carPlate").toString().matches("[A-Z]{3}[0-9]{4}")) {
                                // Insere a conta no banco de dados
                                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                                insertStatement.setObject(1, UUID.fromString(accountId));
                                insertStatement.setString(2, input.get("name").toString());
                                insertStatement.setString(3, input.get("email").toString());
                                insertStatement.setString(4, input.get("cpf").toString());
                                insertStatement.setString(5, input.get("carPlate").toString());
                                insertStatement.setBoolean(6, false);
                                insertStatement.setBoolean(7, true);
                                insertStatement.setDate(8, Date.valueOf(String.valueOf(date.toLocalDate())));
                                insertStatement.setBoolean(9, false);
                                insertStatement.setObject(10, UUID.fromString(verificationCode));
                                insertStatement.executeUpdate();

                                // Envie o e-mail de verificação
                                sendEmail(input.get("email").toString(), "Verification", "Please verify your code at first login " + verificationCode);

                                Map<String, Object> response = new HashMap<>();
                                response.put("accountId", accountId);
                                return response;
                            } else {
                                throw new Exception("Invalid plate");
                            }
                        } else {
                            // Insere a conta no banco de dados
                            PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO cccat13.account (account_id, name, email, cpf, car_plate, is_passenger, is_driver, date, is_verified, verification_code) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                            insertStatement.setObject(1, UUID.fromString(accountId));
                            insertStatement.setString(2, input.get("name").toString());
                            insertStatement.setString(3, input.get("email").toString());
                            insertStatement.setString(4, input.get("cpf").toString());
                            insertStatement.setString(5, "");
                            insertStatement.setBoolean(6, true);
                            insertStatement.setBoolean(7, false);
                            insertStatement.setDate(8, Date.valueOf(String.valueOf(date.toLocalDate())));
                            insertStatement.setBoolean(9, false);
                            insertStatement.setObject(10, UUID.fromString(verificationCode));
                            insertStatement.executeUpdate();

                            // Envie o e-mail de verificação
                            sendEmail(input.get("email").toString(), "Verification", "Please verify your code at first login " + verificationCode);

                            Map<String, Object> response = new HashMap<>();
                            response.put("accountId", accountId);
                            return response;
                        }
                    } else {
                        throw new Exception("Invalid email");
                    }
                } else {
                    throw new Exception("Invalid name");
                }
            } else {
                throw new Exception("Account already exists");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public Map<String, Object> getAccount(String accountId) {
        Connection connection = null;
        try {
            // Conecta ao banco de dados PostgreSQL
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/app", "postgres", "123456");

            // Consulta a conta pelo ID
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM cccat13.account WHERE account_id = ?");
            statement.setObject(1, UUID.fromString(accountId));
            ResultSet resultSet = statement.executeQuery();

            Map<String, Object> accountInfo = new HashMap<>();
            if (resultSet.next()) {
                accountInfo.put("accountId", resultSet.getString("account_id"));
                accountInfo.put("name", resultSet.getString("name"));
                accountInfo.put("email", resultSet.getString("email"));
                accountInfo.put("cpf", resultSet.getString("cpf"));
                accountInfo.put("carPlate", resultSet.getString("car_plate"));
                accountInfo.put("isPassenger", String.valueOf(resultSet.getBoolean("is_passenger")));
                accountInfo.put("isDriver", String.valueOf(resultSet.getBoolean("is_driver")));
                accountInfo.put("date", resultSet.getDate("date").toString());
                accountInfo.put("isVerified", String.valueOf(resultSet.getBoolean("is_verified")));
                accountInfo.put("verificationCode", resultSet.getString("verification_code"));
            }

            return accountInfo;
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}