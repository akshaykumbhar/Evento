package com.evento.evento;

/**
 * Created by kumbh on 1/29/2019.
 */

public class Student {
    String name;
    String email;
    String col;
    String phone;
    String prouri;
    String userid;
    int wallet;

    public Student() {
        super();
    }

    public Student(String name, String email, String col, String phone, String userid, String prouri,int wallet) {
        this.name = name;
        this.email = email;
        this.col = col;
        this.phone = phone;
        this.wallet = wallet;
        this.userid =  userid;
        this.prouri = prouri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public String getCol() {
        return col;
    }

    public void setCol(String col) {
        this.col = col;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProuri() {
        return prouri;
    }

    public void setProuri(String prouri) {
        this.prouri = prouri;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
