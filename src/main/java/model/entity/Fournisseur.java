/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

/**
 *
 * @author tmejane
 */
public class Fournisseur {
    
    private int manufacturerId;
    
    private String name;
    
    private String adressLine1;
    
    private String adressLine2;
    
    private String city;
    
    private String state;
    
    private String zip;
    
    private String phone;
    
    private String fax;
    
    private String email;
    
    private String rep;

    public Fournisseur(int manufacturerId, String name, String adressLine1, String adressLine2, String city, String state, String zip, String phone, String fax, String email, String rep) {
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.adressLine1 = adressLine1;
        this.adressLine2 = adressLine2;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.fax = fax;
        this.email = email;
        this.rep = rep;
    }

    public int getManufacturerId() {
        return manufacturerId;
    }

    public String getName() {
        return name;
    }

    public String getAdressLine1() {
        return adressLine1;
    }

    public String getAdressLine2() {
        return adressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getEmail() {
        return email;
    }

    public String getRep() {
        return rep;
    }

}
