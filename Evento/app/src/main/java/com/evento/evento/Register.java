package com.evento.evento;

/**
 * Created by kumbh on 3/20/2019.
 */

public class Register {
    String regiserationdate;
    String regiserationid;
    String eventid;
    String studentid;
    String ename;
    String sname;

    public Register() {

    }

    public Register(String regiserationdate, String regiserationid, String eventid, String studentid,String ename, String sname) {
        this.regiserationdate = regiserationdate;
        this.regiserationid = regiserationid;
        this.eventid = eventid;
        this.studentid = studentid;
        this.ename = ename;
        this.sname = sname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getRegiserationdate() {
        return regiserationdate;
    }

    public void setRegiserationdate(String regiserationdate) {
        this.regiserationdate = regiserationdate;
    }

    public String getRegiserationid() {
        return regiserationid;
    }

    public void setRegiserationid(String regiserationid) {
        this.regiserationid = regiserationid;
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }
}
