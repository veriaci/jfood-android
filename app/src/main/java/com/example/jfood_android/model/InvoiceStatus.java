package com.example.jfood_android.model;

/**
 * Enumeration class invoiceStatus - write a description of the enum class here
 *
 * @author Hanif Imam
 * @version 06/03/2020
 */
public enum InvoiceStatus
{
    Ongoing("Ongoing"),
    Finished("Finished"),
    Canceled("Canceled");

    private String invoiceStatus;

    InvoiceStatus(String invoiceStatus){
        this.invoiceStatus = invoiceStatus;
    }

    public String toString(){
        return invoiceStatus;
    }
}

