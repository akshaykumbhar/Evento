package com.evento.evento;

/**
 * Created by kumbh on 10/6/2018.
 */

public class Events {
    String id;
    String name;
    String sub;
    String contact;
    String eventno;
    String imgurl;
    String email;
    int seat;
    int avail;
    int startdate;
    int enddate;
    String address;
    int price;



    public Events() {
    }

    public Events(String id,String name, String sub, String contact, String eventno, String imgurl, String email, int seat, int avail, int startdate, int enddate, String address, int price) {
        this.id = id;
        this.name = name;
        this.sub = sub;
        this.contact = contact;
        this.eventno = eventno;
        this.imgurl = imgurl;
        this.email = email;
        this.seat = seat;
        this.avail = avail;
        this.startdate = startdate;
        this.enddate = enddate;
        this.address = address;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEventno() {
        return eventno;
    }

    public void setEventno(String eventno) {
        this.eventno = eventno;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public int getAvail() {
        return avail;
    }

    public void setAvail(int avail) {
        this.avail = avail;
    }

    public int getStartdate() {
        return startdate;
    }

    public void setStartdate(int startdate) {
        this.startdate = startdate;
    }

    public int getEnddate() {
        return enddate;
    }

    public void setEnddate(int enddate) {
        this.enddate = enddate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

