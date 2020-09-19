package com.example.internd;

public class Company {
    String cname,edate,sdate,caddress,advisor,iprofile;
    public Company()
    {}

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return
                "Company Name : " + cname  +"\n"+
                        "Job Profile : "+iprofile+"\n"+
                 "Start Date : "+sdate+"\t\t\t  "+
                "End Date : "+ edate+ " \n"+
                "Company Address : " + caddress + "\n"+
                "Advisor : "+advisor;

    }

    public String getEdate() {
        return edate;
    }

    public void setEdate(String edate) {
        this.edate = edate;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    public String getAdvisor() {
        return advisor;
    }

    public void setAdvisor(String advisor) {
        this.advisor = advisor;
    }

    public String getIprofile() {
        return iprofile;
    }

    public void setIprofile(String iprofile) {
        this.iprofile = iprofile;
    }

    public Company(String cname, String edate, String sdate, String caddress, String advisor, String iprofile) {
        this.cname = cname;
        this.edate = edate;
        this.sdate = sdate;
        this.caddress = caddress;
        this.advisor = advisor;
        this.iprofile = iprofile;
    }
}
