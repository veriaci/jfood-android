package com.example.jfood_android.model;
import java.util.*;
import java.text.*;

public class CashInvoice extends Invoice
{
    // instance variables - replace the example below with your own
    private static final PaymentType PAYMENT_TYPE = PaymentType.Cash;
    private int deliveryFee = 0;

    /**
     * Constructor for objects of class CashInvoice
     */
    public CashInvoice(int id, ArrayList<Food> foods, Customer customer)
    {
        super(id, foods, customer);
        setTotalPrice();
    }


    /**
     * Constructor for objects of class CashInvoice
     */
    public CashInvoice(int id, ArrayList<Food> foods, Customer customer, int deliveryFee)
    {
        super(id, foods, customer);
        this.deliveryFee = deliveryFee;
        setTotalPrice();
    }

    /**
     * Mengakses tipe bayaran dari PaymentType
     * @return - tidak ada return
     */
    public PaymentType getPaymentType(){
        return PAYMENT_TYPE;
    }

    /**
     * mengakses object DeliveryFee
     *
     * @return  deliveryFee mengembalikan object deliveryFee
     */
    public int getDeliveryFee()
    {
        return deliveryFee;
    }

    /**
     * ngeset nilai deliveryFee
     *
     * @param  deliveryFee object nya deliveryFee
     */
    public void setDeliveryFee(int deliveryFee)
    {
        this.deliveryFee = deliveryFee;
    }


    /**
     * Mengubah nilai harga total Invoice setelah diskon (kalo dapet deliveryFee)
     */
    public void setTotalPrice(){
        int total = 0;
        for (int i = 0; i < super.getFoods().size(); i++){
            total = total + super.getFoods().get(i).getPrice();
        }
        if (deliveryFee != 0){
            total += deliveryFee;
        }

        this.totalPrice = total;
    }

    // Print
    /**
     * Mem-Print sesuatu
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        String foodNames = "";
        for (Food foodList : super.getFoods()){
            foodNames = foodNames + foodList.getName() + ", ";
        }
        foodNames = foodNames.substring(0, foodNames.length() - 2);

        if(super.getDate()!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

            return "=============Invoice=============" +
                    "\nId          = " + super.getId() +
                    "\nFood        = " + foodNames +
                    "\nDate        = " + sdf.format(getDate().getTime()) +
                    "\nCustomer    = " + super.getCustomer().getName() +
                    "\n     Delivery Fee= " + deliveryFee +
                    "\nTotal Price = " + totalPrice +
                    "\nStatus      = " + super.getInvoiceStatus() +
                    "\nPayment Type= " + PAYMENT_TYPE +
                    "\n=================================\n";
        }else{
            return "==========Invoice===========" +
                    "\nId          = " + super.getId() +
                    "\nFood        = " + foodNames +
                    "\nCustomer    = " + super.getCustomer().getName() +
                    "\n     Delivery Fee= " + deliveryFee +
                    "\nTotal Price = " + totalPrice +
                    "\nStatus      = " + super.getInvoiceStatus() +
                    "\nPayment Type= " + PAYMENT_TYPE +
                    "\n============================\n";
        }
    }

}

