package com.evento.evento;

/**
 * Created by kumbh on 1/30/2019.
 */

public class Institute {
    String name;
    String email;
    String address;
    String phone;
    String prouri;
    String userid;
    int wallet;

    public Institute() {
    }

    public Institute(String name, String email, String address, String phone, String userid, String prouri,int wallet) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.prouri = prouri;
        this.userid = userid;
        this.wallet = wallet;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
