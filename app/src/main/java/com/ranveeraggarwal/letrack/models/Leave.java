package com.ranveeraggarwal.letrack.models;

public class Leave {
    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getFno() {
        return fno;
    }

    public void setFno(int fno) {
        this.fno = fno;
    }

    public Leave() {
        this.setDate(0);
        this.setFno(0);
    }

    public Leave(long pid, long date, int fno) {
        this.pid = pid;
        this.date = date;
        this.fno = fno;
    }

    private long date;
    private long pid;
    private int fno;
}
