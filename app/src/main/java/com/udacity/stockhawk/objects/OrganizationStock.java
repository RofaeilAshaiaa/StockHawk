package com.udacity.stockhawk.objects;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by anonymous on 4/4/17.
 */

public class OrganizationStock {

    private String NameStock ;
    private String SymbolStock;
    private ArrayList<Calendar> DateStock;
    private ArrayList<Long> TimeInMillis ;

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
