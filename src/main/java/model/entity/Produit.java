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
public class Produit {
    
    private int productId;
    private int manufacturer;
    private String porductCode;
    private double purchaseCost;
    private int quantityOnHand;
    private double markup;
    private String available;
    private String description;

    public Produit(int productId, int manufacturer, String porductCode, double purchaseCost, int quantityOnHand, double markup, String available, String description) {
        this.productId = productId;
        this.manufacturer = manufacturer;
        this.porductCode = porductCode;
        this.purchaseCost = purchaseCost;
        this.quantityOnHand = quantityOnHand;
        this.markup = markup;
        this.available = available;
        this.description = description;
    }

    public int getProductId() {
        return productId;
    }

    public int getManufacturer() {
        return manufacturer;
    }

    public String getPorductCode() {
        return porductCode;
    }

    public double getPurchaseCost() {
        return purchaseCost;
    }

    public int getQuantityOnHand() {
        return quantityOnHand;
    }

    public double getMarkup() {
        return markup;
    }

    public String getAvailable() {
        return available;
    }

    public String getDescription() {
        return description;
    }
    
    
    
}
