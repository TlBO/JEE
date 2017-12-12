package model.entity;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rlajmi
 */
public class Client {
    
    private int idClient;
    
    private String nom;
    
    private String email;
    
    private String telephone;
    
    private String addresse;

    public Client(int idClient, String nom, String email, String telephone, String addresse) {
        this.idClient = idClient;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.addresse = addresse;
    }

    public int getIdClient() {
        return idClient;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getAddresse() {
        return addresse;
    }    
    
}