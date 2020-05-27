package com.example.jfood_android.model;

/**
 * Class sebagai Blueprint dari objek Invoice
 *
 * @author Hanif Imam
 * @version 20/03/2020
 */

import java.util.*;

public abstract class Invoice
{
    // instance variable
    private int id;
    private ArrayList<Food> foods;
    private Calendar date;
    protected int totalPrice;
    private Customer customer;
    private InvoiceStatus invoiceStatus;

    /**
     * Constructor for objects of class Invoice
     * @param id merupakan id dari objek invoice
     * @param foods merupakan object makanan pada invoice
     * @param customer merupakan pelanggan yang membeli makanan dan tertulis pada invoice
     */
    public Invoice(int id, ArrayList<Food> foods, Customer customer)
    {
        this.id = id;
        this.foods = foods;
        this.date = Calendar.getInstance();
        this.customer = customer;
        this.invoiceStatus = InvoiceStatus.Ongoing;
    }

    // Accessor
    /**
     * Mengakses nilai id dari Invoice
     * @return id dari invoice
     */
    public int getId()
    {
        return id;
    }

    /**
     * Mengakses objek makanan dari invoice
     * @return object dari makanan dalam invoice
     */
    public ArrayList<Food> getFoods()
    {
        return foods;
    }

    /**
     * Mengakses nilai tanggal dari Invoice
     * @return tanggal dari invoice
     */
    public Calendar getDate()
    {
        return date;
    }

    /**
     * Mengakses nilai harga total dari Invoice
     * @return harga total dari invoice
     */
    public int getTotalPrice()
    {
        return totalPrice;
    }

    /**
     * Mengakses pelanggan dari Invoice
     * @return id dari invoice
     */
    public Customer getCustomer()
    {
        return customer;
    }

    /**
     * Mengakses tipe bayaran dari PaymentType
     * @return - tidak ada return
     */
    public abstract PaymentType getPaymentType();

    /**
     * Mengakses status invoice dari InvoiceStatus
     * @return InvoiceStatus atau status invoice
     */
    public InvoiceStatus getInvoiceStatus()
    {
        return invoiceStatus;
    }

    // Mutator
    /**
     * Mengubah nilai id Invoice
     * @param id dari invoice
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Mengubah nilai object makanan dalam Invoice
     * @param foods dari object makanan dalam invoice
     */
    public void setFoods(ArrayList<Food> foods)
    {
        this.foods = foods;
    }

    /**
     * Mengubah nilai tanggal Invoice
     * @param date dari invoice
     */

    public void setDate(Calendar date)
    {
        this.date = date;
    }

    public void setDate(int year, int month, int dayOfMonth)
    {
        this.date = new GregorianCalendar(year, month, dayOfMonth);
    }

    /**
     * Mengubah nilai harga total Invoice
     */
    public abstract void setTotalPrice();

    /**
     * Mengubah pelanggan Invoice
     * @param customer pada invoice
     */
    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    /**
     * Mengakses status invoice dari InvoiceStatus
     */
    public void setInvoiceStatus(InvoiceStatus invoiceStatus)
    {
        this.invoiceStatus = invoiceStatus;
    }

    // Print
    /**
     * Mem-Print sesuatu
     * @return - tidak mengembalikan apapun
     */
    public abstract String toString();

}

