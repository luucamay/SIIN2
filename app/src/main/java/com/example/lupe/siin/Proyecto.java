package com.example.lupe.siin;

/**
 * Created by lupe on 19/12/17.
 */

public class Proyecto {

    private final int proyId;
    private final String proySisin;
    private final String proyDescrip;
    private final float proyLong;
    private final String proyEstado;
    private final String proyTipo;

    public Proyecto(int proyId, String proySisin, String proyDescrip, float proyLong, String proyEstado, String proyTipo) {
        this.proyId = proyId;
        this.proySisin = proySisin;
        this.proyDescrip = proyDescrip;
        this.proyLong = proyLong;
        this.proyEstado = proyEstado;
        this.proyTipo = proyTipo;
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

    public float getProyLong() {
        return proyLong;
    }

    public String getProyEstado() {
        return proyEstado;
    }

    public String getProyTipo() {
        return proyTipo;
    }
}
