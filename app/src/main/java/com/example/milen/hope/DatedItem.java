package com.example.milen.hope;

import java.util.Date;
public abstract class DatedItem
{
    private Date date;

    private String description;

    public DatedItem(Date date, String description)
    {
        this.date = date;
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return date.toString() + " " + description;
    }
}
