package com.example.jfood_android.model;

/**
 * Class sebagai Blueprint dari objek Promo
 *
 * @author Hanif Imam
 * @version 12/03/2020
 */
public class Promo
{
    // instance variable
    private int id;
    private String code;
    private int discount;
    private int minPrice;
    private boolean active;

    /**
     * Constructor for objects of class Promo
     * @param id merupakan id dari objek Promo
     * @param code merupakan kode promo
     * @param discount merupakan harga discount
     * @param minPrice merupakan harga minimal promo
     * @param active merupakan penanda aktifnya promo
     */
    public Promo(int id, String code, int discount, int minPrice, boolean active)
    {
        this.id = id;
        this.code = code;
        this.discount = discount;
        this.minPrice = minPrice;
        this.active = active;
    }

    // Accessor
    /**
     * Mengakses nilai id dari Promo
     * @return id dari Promo
     */
    public int getId()
    {
        return id;
    }

    /**
     * Mengakses kode Promo
     * @return string kode promo
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Mengakses nilai diskon dari Promo
     * @return nilai diskon dari Promo
     */
    public int getDiscount()
    {
        return discount;
    }

    /**
     * Mengakses nilai harga minimal dari Promo
     * @return harga minimal dari Promo
     */
    public int getMinPrice()
    {
        return minPrice;
    }

    /**
     * Mengakses apakah promo aktif
     * @return boolean keaktifan promo
     */
    public boolean getActive()
    {
        return active;
    }

    // Mutator
    /**
     * Mengubah nilai id Promo
     * @param id dari Promo
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Mengubah kode Promo
     * @param code promo
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * Mengubah nilai diskon Promo
     * @param discount Promo
     */
    public void setDiscount(int discount)
    {
        this.discount = discount;
    }

    /**
     * Mengubah harga minimal Promo
     * @param minPrice pada Promo
     */
    public void setMinPrice(int minPrice)
    {
        this.minPrice = minPrice;
    }

    /**
     * Mengakses boolean aktifnya promo
     * @param active tidak ada param
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    //toString
    /**
     * memperbaharui method toString()
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        return "==========Promo==========" +
                "\nId         = " + id +
                "\nCode       = " + code +
                "\nDiscount   = " + discount +
                "\nMin Price  = " + minPrice +
                "\nActive     = " + active +
                "\n=========================\n";
    }

}
