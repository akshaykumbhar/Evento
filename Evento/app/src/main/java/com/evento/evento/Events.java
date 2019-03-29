package com.evento.evento;

/**
 * Created by kumbh on 10/6/2018.
 */

public class Events {
    String id;
    String name;
    String sub;
    String imgurl;
    String email;
    int seat;
    int avail;
    String startdate;
    String enddate;
    String address;
    int price;
    String category;
    String update;
    String time;



    public Events() {
    }

    public Events(String id,String name, String sub, String imgurl, String email, int seat, int avail, String startdate, String enddate, String address, int price, String category,String update,String time) {
        this.id = id;
        this.name = name;
        this.sub = sub;

        this.imgurl = imgurl;
        this.email = email;
        this.seat = seat;
        this.avail = avail;
        this.startdate = startdate;
        this.enddate = enddate;
        this.address = address;
        this.price = price;
        this.category = category;
        this.update = update;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

