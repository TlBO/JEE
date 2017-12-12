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
public class CategorieProduit {

    private String prodCode;

    private String discountCode;

    private String description;

    public CategorieProduit(String prodCode, String discountCode, String description) {
        this.prodCode = prodCode;
        this.discountCode = discountCode;
        this.description = description;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public String getDescription() {
        return description;
    }


    
    

}
