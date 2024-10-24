package logicabancaria;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String name;
    private String id;
    private String numberTel;
    private String mail;
    private List<Cuenta> accounts = new ArrayList<>();

    public Usuario(String pName, String pId, String pNumberTel, String pMail) {
        // Faltan las validaciones
        this.name = pName;
        this.id = pId;
        this.numberTel = pNumberTel;
        this.mail = pMail;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getNumberTel() {
        return numberTel;
    }

    public String getMail() {
        return mail;
    }

    public List<Cuenta> getAccounts() {
        return accounts;
    }

    public void addAccount(Cuenta cuenta) {
        accounts.add(cuenta);
    }
    
    public void setNumberTel(String nuevoNumeroTel) {
        this.numberTel = nuevoNumeroTel;
    }
    
    public void setMail(String nuevoMail) {
    this.mail = nuevoMail;
    }  
}
