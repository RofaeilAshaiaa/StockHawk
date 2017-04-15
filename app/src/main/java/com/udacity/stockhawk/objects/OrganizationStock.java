package com.udacity.stockhawk.objects;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by anonymous on 4/4/17.
 */

public class OrganizationStock {

    private String NameStock ;
    private String SymbolStock;
    private float price;
    private float change;
    private ArrayList<Calendar> DateStock;
    private ArrayList<Long> TimeInMillis ;

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<Calendar> getDateStock() {
        return DateStock;
    }

    public void setDateStock(ArrayList<Calendar> dateStock) {
        DateStock = dateStock;
    }

    public ArrayList<Long> getTimeInMillis() {
        return TimeInMillis;
    }

    public void setTimeInMillis(ArrayList<Long> timeInMillis) {
        TimeInMillis = timeInMillis;
    }

    public String getNameStock() {
        return NameStock;
    }

    public void setNameStock(String nameStock) {
        NameStock = nameStock;
    }

    public String getSymbolStock() {
        return SymbolStock;
    }

    public void setSymbolStock(String symbolStock) {
        SymbolStock = symbolStock;
    }

}
