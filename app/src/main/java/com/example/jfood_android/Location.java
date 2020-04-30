package com.example.jfood_android;

/**
 * Class sebagai Blueprint dari objek Location (lokasi)
 *
 * @author Hanif Imam
 * @version 28/02/2020
 */
public class Location
{
    // instance variables
    private String province;
    private String description;
    private String city;

    /**
     * Constructor for objects of class Location (Lokasi)
     * @param province merupakan nama provinsi untuk lokasi tersebut
     * @param description merupakan deskripsi untuk lokasi tersebut
     * @param city merupakan nama kota untuk lokasi tersebut
     */
    public Location(String province, String description, String city)
    {
        this.province = province;
        this.description = description;
        this.city = city;
    }

    // Accessor
    /**
     * Mengakses nama Provinsi dari Lokasi
     * @return nama provinsi dari lokasi
     */
    public String getProvince()
    {
        return province;
    }

    /**
     * Mengakses Deskripsi dari Lokasi
     * @return deskripsi dari lokasi
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Mengakses nama Kota dari Lokasi
     * @return nama Kota dari lokasi
     */
    public String getCity()
    {
        return city;
    }

    // Mutator
    /**
     * Mengubah nama Provinsi dari Lokasi
     * @param province provinsi dari Lokasi
     */
    public void setProvince(String province)
    {
        this.province = province;
    }

    /**
     * Mengubah deskripsi dari Lokasi
     * @param description dari Lokasi
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Mengubah nama Kota dari Lokasi
     * @param city kota dari Lokasi
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    //toString
    /**
     * memperbaharui method toString()
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        // System.out.println(name);
        return "==========Location==========" +
                "\nProvince   = " + province +
                "\nCity       = " + city +
                "\nDescription= " + description +
                "\n============================\n";
    }
}

