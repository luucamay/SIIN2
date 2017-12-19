package com.example.lupe.siin;

/**
 * Created by lupe on 19/12/17.
 */

public class Proyecto {

    private final int proyId;
    private final String proySisin;
    private final String proyDescrip;

    public Proyecto(int proyId, String proySisin, String proyDescrip) {
        this.proyId = proyId;
        this.proySisin = proySisin;
        this.proyDescrip = proyDescrip;
    }

    public int getProyId() {
        return proyId;
    }

    public String getProySisin() {
        return proySisin;
    }

    public String getProyDescrip() {
        return proyDescrip;
    }
}
