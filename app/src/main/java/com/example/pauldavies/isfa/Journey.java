package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class Journey
{
    /*
     * This defines the properties of a route.
     * A route will contain a name, description and journeys.
     * And a journey in the route will have a name and description.*/

    public String customerCode;
    public String customerName;
    String colour;
    public ArrayList<String[]> journeys    =   new ArrayList<>();

    public Journey(String customerCode,String customerName)
    {
        this.customerCode          =   customerCode;
        this.customerName          =   customerName;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
}