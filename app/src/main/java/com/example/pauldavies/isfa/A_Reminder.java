package com.example.pauldavies.isfa;

public class A_Reminder
{
    String remider;
    boolean status;
    String rem_id;

    public A_Reminder()
    {

    }

    public void setRemider(String remider) {
        this.remider = remider;
    }

    public String getRemider() {
        return remider;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public String getRem_id() {
        return rem_id;
    }

    public void setRem_id(String rem_id) {
        this.rem_id = rem_id;
    }
}
