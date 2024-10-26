package logicabancaria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private int id; 
    private Usuario owner;
    private String pin; 
    
    private LocalDate fechaCreacion = LocalDate.now();
    private double balance = 0;
    private boolean status = true;
    private List<Transaccion> transactions = new ArrayList<>();
    
    public Cuenta(Usuario pUser, String pPIN, double pAmount) {
        this.id = GeneradorID.generarID();
        this.owner = pUser;
        this.pin = pPIN;
        this.balance += pAmount;
    }
    
    public Cuenta(Usuario pUser, String pPIN, double pAmount, int pId) {
        this.id = pId;
        this.owner = pUser;
        this.pin = pPIN;
        this.balance += pAmount;
    }
    // Funciones Auxiliares
    private void cashWithdrawalAux(double pAmount) {
        this.balance -= pAmount;
    }
    
    private void cashDepositAux(double pAmount) {
        this.balance += pAmount;
    }
    
    // Funciones principales
    public void cashWithdrawal(double pAmount) {
        LocalDate date = LocalDate.now();
        double commission = 0;

        if (transactions.size() <= 5) {
            if (this.balance >= pAmount) {
                cashWithdrawalAux(pAmount);
                Transaccion transaction = new Transaccion("Withdrawal", pAmount, date, commission);
                this.transactions.add(transaction);
            } else {
                throw new IllegalArgumentException("Fondos insuficientes.");
            }
        } else {
            if (this.balance >= pAmount + pAmount * 0.02) {
                commission = pAmount * 0.02;
                cashWithdrawalAux(pAmount + commission); // Se resta el monto más comisión
                Transaccion transaction = new Transaccion("Withdrawal", pAmount, date, commission);
                this.transactions.add(transaction);
            } else {
                throw new IllegalArgumentException("Fondos insuficientes.");
            }
        }
    }
    
    public void addTransaccion(Transaccion t){
        this.transactions.add(t);
    }
    
    public void cashDeposit(double pAmount) {
        LocalDate date = LocalDate.now();
        double commission = 0;

        if (transactions.size() <= 5) {
            cashDepositAux(pAmount);
            Transaccion transaction = new Transaccion("Deposit", pAmount, date, commission);
            this.transactions.add(transaction);
        } else {
            commission = pAmount * 0.02;
            cashDepositAux(pAmount - commission); // Se suma el monto menos la comisión
            Transaccion transaction = new Transaccion("Deposit", pAmount, date, commission);
            this.transactions.add(transaction);
        }
    }

    public boolean verificarPin(String pPin) {
        return this.pin.equals(pPin);
    }

    public List<Transaccion> getTransacciones() {
        return transactions; // Método para obtener las transacciones
    }
    
    public boolean getStatus() {
        return status; // Método para obtener el status
    }
    
    public int getId() {
        return id; // Método para obtener el ID
    }

    public String getPin() {
        return pin; // Método para obtener el PIN
    }

    public double getBalance() {
        return balance; // Método para obtener el saldo
    }

    public Usuario getOwner() {
        return owner; // Método para obtener el dueño de la cuenta
    }
    
    public void setPin(String nuevoPin) {
        this.pin = nuevoPin;
    }
    
    public void transferir(double monto, Cuenta cuentaDestino) {
        if (balance >= monto) {
            balance -= monto; // Reduce el saldo de la cuenta de origen
            cuentaDestino.cashDeposit(monto); // Aumenta el saldo de la cuenta de destino
        } else {
            throw new IllegalArgumentException("Fondos insuficientes.");
        }
    }
    
    public LocalDate getfechaCreacion(){
        return fechaCreacion;
    }
    
    private double calcularComision() {
        return transactions.size() > 5 ? 0.02 * balance : 0;
    }
}