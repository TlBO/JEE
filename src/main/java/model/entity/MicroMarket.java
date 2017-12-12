/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entity;

/**
 *
 * @author madjovi
 */
public class MicroMarket {
    private String zipCode;
    private double Radius;
    private double AreaLength;
    private double AreaWidth;

    public MicroMarket(String zipCode, double Radius, double AreaLength, double AreaWidth) {
        this.zipCode = zipCode;
        this.Radius = Radius;
        this.AreaLength = AreaLength;
        this.AreaWidth = AreaWidth;
    }

    public String getZipCode() {
        return zipCode;
    }

    public double getRadius() {
        return Radius;
    }

    public double getAreaLength() {
        return AreaLength;
    }

    public double getAreaWidth() {
        return AreaWidth;
    }
    


    
    
}