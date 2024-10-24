package logicabancaria;

import java.time.LocalDate; // Importa LocalDate

public class Transaccion {
    private String type;
    private double amount;
    private LocalDate date;
    private double commission;

    public Transaccion(String type, double amount, LocalDate date, double commission) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.commission = commission;
    }

    // Getters
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getCommission() {
        return commission;
    }
}
