package com.leonardossev.ride.core.validators;

import org.springframework.stereotype.Component;

@Component
public class CpfValidator implements ICpfValidator {

    @Override
    public void validate(String cpf) throws RuntimeException {
        cpf = this.clearCpf(cpf);

        if (cpf.length() != 11) {
            this.handleBadCpf(cpf);
        }

        if (this.areAllTheSameDigit(cpf)) {
            this.handleBadCpf(cpf);
        }

        String validFirstDigit = this.defineDigit(cpf, 8);
        String validSecondDigit = this.defineDigit(cpf, 9);

        String cpfFirstDigit = String.valueOf(cpf.charAt(9));
        String cpfSecondDigit = String.valueOf(cpf.charAt(10));

        if (!validFirstDigit.equals(cpfFirstDigit) || !validSecondDigit.equals(cpfSecondDigit)) {
            this.handleBadCpf(cpf);
        }
    }

    private String defineDigit(String cpf, int indexBeforeDigit) {
        int total = 0;
        int numberToCalculate = 2;

        for (int count = indexBeforeDigit; count >= 0; count--) {
            int digit = Integer.parseInt(String.valueOf(cpf.charAt(count)));
            total = total + (numberToCalculate * digit);
            numberToCalculate++;
        }

        int rest = total % 11;

        if (rest < 2) {
            return "0";
        }

        return (String.valueOf(11 - rest));
    }

    private String clearCpf(String cpf) {
        if (cpf == null) {
            return "";
        }

        return cpf
                .replaceAll("\\.", "")
                .replaceAll("-", "")
                .replaceAll(" ", "");
    }

    private boolean areAllTheSameDigit(String cpf) {
        final char firstChar = cpf.charAt(0);
        return cpf.chars().allMatch(c -> c == firstChar);
    }

    private void handleBadCpf(String cpf) {
        throw new RuntimeException(String.format("Invalid cpf: %s", cpf));
    }
}
