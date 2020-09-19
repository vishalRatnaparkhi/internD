

package com.example.internd;

public class User {
    String fname;
    String mname;
    String lname;
    String gmail;
    String grnumber;
    String roll;
    String password;
    long createdAt;


    public User(String fname, String mname, String lname, String gmail, String grnumber, String password,String roll,long createdAt) {
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.gmail = gmail;
        this.grnumber = grnumber;
        this.roll=roll;
        this.password = password;
        this.createdAt=createdAt;
    }


    User(){}

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getGrnumber() {
        return grnumber;
    }

    public void setGrnumber(String grnumber) {
        this.grnumber = grnumber;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return
                "Name = " + fname + ' ' + mname + ' ' + lname + '\n' +
                "Gmail = " + gmail + "\n" + "GR Number = " + grnumber ;

    }
}
