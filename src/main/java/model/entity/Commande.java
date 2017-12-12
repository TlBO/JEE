package model.entity;


import java.sql.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rlajmi
 */
public class Commande {
    
    private int idCommande;
    
    private int idClient;
    
    private int idProduit;
    
    private int quantite;
    
    private double shippingCost;
    
    private Date dateComm;
    
    private Date shippingDate;
    
    private String idCompa;

    public Commande(int idCommande, int idClient, int idProduit, int quantite, double shippingCost, Date dateComm, Date shippingDate, String idCompa) {
        this.idCommande = idCommande;
        this.idClient = idClient;
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.shippingCost = shippingCost;
        this.dateComm = dateComm;
        this.shippingDate = shippingDate;
        this.idCompa = idCompa;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public int getIdClient() {
        return idClient;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getShippingCost() {
        return shippingCost;
    }

    public Date getDateComm() {
        return dateComm;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public String getIdCompa() {
        return idCompa;
    }

    
   
}