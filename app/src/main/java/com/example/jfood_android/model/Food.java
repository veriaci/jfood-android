package com.example.jfood_android.model;

/**
 * Class sebagai Blueprint dari objek makanan
 *
 * @author Hanif Imam
 * @version 05/03/2020
 */
public class Food
{
    // instance variables
    private int id;
    private String name;
    private Seller seller;
    private int price;
    private String category;

    /**
     * Constructor for objects of class Food (Makanan)
     * @param id merupakan id dari objek makanan
     * @param name merupakan nama dari makanan tersebut
     * @param seller merupakan penjual dari makanan tersebut
     * @param price merupakan harga dari makanan tersebut
     * @param category merupakan kategori dari makanan tersebut
     */
    public Food(int id, String name, Seller seller, int price, String category)
    {
        this.id = id;
        this.name = name;
        this.seller = seller;
        this.price = price;
        this.category = category;
    }

    // Accessor
    /**
     * Mengakses nilai id Food atau makanan
     * @return id dari makanan
     */
    public int getId()
    {
        return id;
    }

    /**
     * Mengakses nilai nama Food atau makanan
     * @return nama dari makanan
     */
    public String getName()
    {
        return name;
    }

    /**
     * Mengakses objek penjual Food atau makanan
     * @return Seller dari makanan
     */
    public Seller getSeller()
    {
        return seller;
    }

    /**
     * Mengakses nilai harga Food atau makanan
     * @return price/harga dari makanan
     */
    public int getPrice()
    {
        return price;
    }

    /**
     * Mengakses nilai kategori Food atau makanan
     * @return kategori dari makanan
     */
    public String getCategory()
    {
        return category;
    }

    // Mutator
    /**
     * Mengubah nilai id Food atau makanan
     * @param id dari food/makanan
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Mengubah nilai nama Food atau makanan
     * @param name/name dari food/makanan
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mengubah Seller Food atau makanan
     * @param seller/penjual dari food/makanan
     */
    public void setSeller(Seller seller)
    {
        this.seller = seller;
    }

    /**
     * Mengubah nilai price/harga Food atau makanan
     * @param price/harga dari food/makanan
     */
    public void setPrice(int price)
    {
        this.price = price;
    }

    /**
     * Mengubah nilai kategori Food atau makanan
     * @param category dari food/makanan
     */
    public void setCategory(String category)
    {
        this.category = category;
    }

    //toString
    /**
     * memperbaharui method toString()
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        return "==========Food==========" +
                "\nId         = " + id +
                "\nNama       = " + name +
                "\nSeller     = " + getSeller().getName() +
                "\nCity       = " + getSeller().getLocation().getCity() +
                "\nPrice      = " + price +
                "\nCategory   = " + getCategory() +
                "\n========================\n";
    }
}

