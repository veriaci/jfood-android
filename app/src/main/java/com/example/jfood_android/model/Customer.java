package com.example.jfood_android.model;

/**
 * Class sebagai Blueprint dari objek Customer (pelanggan)
 *
 * @author Hanif Imam
 * @version 28/02/2020
 */

import java.util.*;
import java.text.*;
import java.util.regex.*;

public class Customer
{
    // instance variables
    private String id;
    private String name;
    private String email;
    private String password;
    private Calendar joinDate;

    /**
     * Constructor for objects of class Customer
     * @param id merupakan id dari objek customer (pelanggan)
     * @param name merupakan nama dari pelanggan
     * @param email merupakan email dari pelanggan
     * @param password merupakan password dari akun pelanggan
     * @param joinDate merupakan tanggal join dengan JFood dari pelanggan tersebut
     */
    public Customer(String id, String name, String email, String password, Calendar joinDate)
    {
        this.id = id;
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = joinDate;
    }

    public Customer(String id, String name, String email, String password, int year, int month, int dayOfMonth)
    {
        this.id = id;
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = new GregorianCalendar(year, month, dayOfMonth);
    }

    public Customer(String id, String name, String email, String password)
    {
        this.id = id;
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = new GregorianCalendar();
    }
    public Customer(String name, String email, String password, Calendar joinDate)
    {
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = joinDate;
    }

    public Customer(String name, String email, String password, int year, int month, int dayOfMonth)
    {
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = new GregorianCalendar(year, month, dayOfMonth);
    }

    public Customer(String name, String email, String password)
    {
        this.name = name;
        setEmail(email);
        setPassword(password);
        this.joinDate = new GregorianCalendar();
    }

    // Accessor
    /**
     * Mengakses nilai id dari Customer atau pelanggan
     * @return id dari pelanggan
     */
    public String getId()
    {
        return id;
    }

    /**
     * Mengakses nilai nama dari Customer atau pelanggan
     * @return nama dari pelanggan
     */
    public String getName()
    {
        return name;
    }

    /**
     * Mengakses nilai email dari Customer atau pelanggan
     * @return email dari pelanggan
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Mengakses nilai password dari Customer atau pelanggan
     * @return password dari pelanggan
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Mengakses nilai tanggal join dari Customer atau pelanggan
     * @return tanggal join dari pelanggan
     */
    public Calendar getJoinDate()
    {
        return joinDate;
    }

    // Mutator
    /**
     * Mengubah nilai id Customer atau pelanggan
     * @param id dari pelanggan
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * Mengubah nilai nama Customer atau pelanggan
     * @param name dari pelanggan
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Mengubah nilai email Customer atau pelanggan
     * @param email dari pelanggan
     */
    public void setEmail(String email)
    {
        Pattern p = Pattern.compile("^[\\w+&*~-]+(?:\\.[\\w+&*~-]+)*@(?!-)(?:[\\w-]+\\.)[a-zA-Z]{2,7}$");
        Matcher m = p.matcher(email);

        if (m.matches()){
            // System.out.println("Email = " + email);
            this.email = email;
        } else {
            //System.out.println("Use an appropriate email");
            //this.email = "";
            throw new IllegalArgumentException("Please use an appropriate email");
        }
    }

    /**
     * Mengubah nilai password Customer atau pelanggan
     * @param password dari pelanggan
     */
    public void setPassword(String password)
    {
        Pattern p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}");
        Matcher m = p.matcher(password);

        if (m.matches()){
            // System.out.println("Password = " + password);
            this.password = password;
        } else {
            //System.out.println("Password not strong enough, use Upper Case, Lower Case, and a number");
            //this.password = "";
            throw new IllegalArgumentException("Password not strong enough, use Upper Case, Lower Case, and a number");
        }
    }

    /**
     * Mengubah nilai tanggal join Customer atau pelanggan
     * @param joinDate join dari pelanggan
     */
    public void setJoinDate(Calendar joinDate)
    {
        this.joinDate = joinDate;
    }

    public void setJoinDate(int year, int month, int dayOfMonth)
    {
        this.joinDate = new GregorianCalendar(year, month, dayOfMonth);
    }

    //toString
    /**
     * memperbaharui method toString()
     * @return - tidak mengembalikan apapun
     */
    public String toString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        return "==========Customer==========" +
                "\nId         = " + id +
                "\nNama       = " + name +
                "\nEmail      = " + email +
                "\nPassword   = " + password +
                "\nJoin Date  = " + sdf.format(getJoinDate().getTime()) +
                "\n============================\n";
    }
}

