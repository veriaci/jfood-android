package com.example.jfood_android.model;
import java.util.*;
import java.text.*;

public class CashlessInvoice extends Invoice
{
    // instance variables - replace the example below with your own
    private static final PaymentType PAYMENT_TYPE = PaymentType.Cashless;
    private Promo promo;

    /**
     * Constructor for objects of class CashlessInvoice
     */
    public CashlessInvoice(int id, ArrayList<Food> foods, Customer customer)
    {
        super(id, foods, customer);
        setTotalPrice();
    }


    /**
     * Constructor for objects of class CashlessInvoice
     */
    public CashlessInvoice(int id, ArrayList<Food> foods, Customer customer, Promo promo)
    {
        super(id, foods, customer);
        this.promo = promo;
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
     * mengakses object Promo
     *
     * @return  promo mengembalikan object promo
     */
    public Promo getPromo()
    {
        return promo;
    }

    /**
     * ngeset nilai promo
     *
     * @param  promo object nya promo
     */
    public void setPromo(Promo promo)
    {
        this.promo = promo;
    }


    /**
     * Mengubah nilai harga total Invoice setelah diskon (kalo dapet promo)
     */
    public void setTotalPrice(){
        int total = 0;
        for (int i = 0; i < super.getFoods().size(); i++){
            total = total + super.getFoods().get(i).getPrice();
        }
        this.totalPrice = total;

        if (promo != null && promo.getActive() && this.totalPrice>promo.getMinPrice()){
            this.totalPrice -= promo.getDiscount();
        }
    }

    public String toString()
    {
        String foodNames = "";
        for (Food foodList : super.getFoods()){
            foodNames = foodNames + foodList.getName() + ", ";
        }
        foodNames = foodNames.substring(0, foodNames.length() - 2);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        if(promo != null  && promo.getActive() && this.totalPrice>promo.getMinPrice()){
            return "=============Invoice=============" +
                    "\nId          = " + super.getId() +
                    "\nFood        = " + foodNames +
                    "\nDate        = " + sdf.format(getDate().getTime()) +
                    "\nCustomer    = " + super.getCustomer().getName() +
                    "\nPromo       = " + promo.getCode() +
                    "\n     Discount    = " + promo.getDiscount() +
                    "\nTotal Price = " + totalPrice +
                    "\nStatus      = " + super.getInvoiceStatus() +
                    "\nPayment Type= " + PAYMENT_TYPE +
                    "\n=================================\n";
        } else {
            return "=============Invoice=============" +
                    "\nId          = " + super.getId() +
                    "\nFood        = " + foodNames +
                    "\nDate        = " + sdf.format(getDate().getTime()) +
                    "\nCustomer    = " + super.getCustomer().getName() +
                    "\nTotal Price = " + totalPrice +
                    "\nStatus      = " + super.getInvoiceStatus() +
                    "\nPayment Type= " + PAYMENT_TYPE +
                    "\n=================================\n";
        }
    }

}

