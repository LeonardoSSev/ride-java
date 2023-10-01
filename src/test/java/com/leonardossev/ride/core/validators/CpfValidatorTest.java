package com.leonardossev.ride.core.validators;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CpfValidatorTest {

    ICpfValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CpfValidator();
    }

    @Test
    public void shouldValidateCpf() {
        List<String> cpfs = Arrays.asList(
                "958.187.055-52",
                "012345678-90",
                "565.486.78060",
                "14786411000"
        );

        for (String cpf : cpfs) {
            assertDoesNotThrow(() -> this.validator.validate(cpf));
        }
    }

    @Test
    public void shouldNotValidateCpf() {
        List<String> cpfs = Arrays.asList(
                "95818705500",
                "958187055",
                "958.187.055-00",
                "958.187.055"
        );

        for (String cpf : cpfs) {
            assertThrows(RuntimeException.class, () -> this.validator.validate(cpf), "Invalid cpf");
        }
    }
}