package com.example.jfood_android.model;

/**
 * Class sebagai Blueprint dari objek Seller (Penjual)
 *
 * @author Hanif Imam
 * @version 28/02/2020
 */
public class Seller
{
    // instance variables
    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private Location location;

    /**
     * Constructor for objects of class Seller
     * @param id merupakan id dari objek Seller (Penjual)
     * @param name merupakan nama dari penjual
     * @param email merupakan email dari penjual
     * @param phoneNumber merupakan nomor telepon penjual
     * @param location merupakan Lokasi penjual tersebut
     */
    public Seller(int id, String name, String email, String phoneNumber, Location location)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    // Accessor
    /**
     * Mengakses nilai id dari Seller atau Penjual
     * @return id dari penjual
     */
    public int getId()
    {
        return id;
    }

    /**
     * Mengakses nilai nama dari Seller atau Penjual
     * @return nama dari penjual
     */
    public String getName()
    {
        return name;
    }

    /**
     * Mengakses nilai email dari Seller atau Penjual
     * @return email dari penjual
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Mengakses nilai nomor telepon dari Seller atau Penjual
     * @return nomor telepon dari penjual
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Mengakses nilai lokasi dari Seller atau Penjual
     * @return lokasi dari penjual
     */
    public Location getLocation()
    {
        return location;
    }


    // Mutator
    /**
     * Mengubah nilai id Seller atau Penjual
     * @param id dari penjual
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Mengubah nilai nama Seller atau Penjual
     * @param name dari penjual
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mengubah nilai email Seller atau Penjual
     * @param email dari penjual
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Mengubah nilai nomor telepon Seller atau Penjual
     * @param phoneNumber telepon dari penjual
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Mengubah nilai lokasi Seller atau Penjual
     * @param location dari penjual
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }

    //toString
    /**
     * memperbaharui method toString()
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        return "==========Seller==========" +
                "\nId         = " + id +
                "\nNama       = " + name +
                "\nPhoneNumber= " + phoneNumber +
                "\nLocation   = " + getLocation().getCity() +
                "\n==========================\n";
    }

}

