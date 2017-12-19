package com.example.lupe.siin;

/**
 * Created by lupe on 19/12/17.
 */

public class Proyecto {

    private final int proyId;
    private final String proySISIN;
    private final String proyDescrip;

    public Proyecto(int proyId, String proySISIN, String proyDescrip) {
        this.proyId = proyId;
        this.proySISIN = proySISIN;
        this.proyDescrip = proyDescrip;
    }

    public int getProyId() {
        return proyId;
    }

    public String getProySISIN() {
        return proySISIN;
    }

    public String getProyDescrip() {
        return proyDescrip;
    }
}
