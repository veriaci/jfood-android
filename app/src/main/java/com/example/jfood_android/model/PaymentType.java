package com.example.jfood_android.model;

/**
 * Enumeration class PaymentType - write a description of the enum class here
 *
 * @author Hanif Imam
 * @version 05/03/2020
 */
public enum PaymentType
{
    Cashless("Cashless"),
    Cash("Cash");

    private String paymentType;

    PaymentType(String paymentType){
        this.paymentType = paymentType;
    }

    public String toString(){
        return paymentType;
    }
}
