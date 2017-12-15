package com.example.lupe.siin;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by lupe on 28/11/17.
 */

public class Tramo {
    private final String id;
    private final ArrayList<LatLng> coordenadas;
    private final int OBJECTID_1;
    private final int OBJECTID;
    private final float Distancia;
    private final int OBJECTID_2;
    private final String Poblacion;
    private final String Tramo;
    private final float Shape_Leng;
    private final String color;
    private final int proyId;
    private final int idSubproyecto;


    /**
     * Construye un nuevo {@link Tramo}.
     */

    public Tramo(String id, ArrayList<LatLng> coordenadas, int objectid_1, int objectid, float distancia, int objectid_2, String poblacion, String tramo, float shape_leng, String color, int proyId, int idSubproyecto) {
        this.id = id;
        this.coordenadas = coordenadas;
        OBJECTID_1 = objectid_1;
        OBJECTID = objectid;
        Distancia = distancia;
        OBJECTID_2 = objectid_2;
        Poblacion = poblacion;
        Tramo = tramo;
        Shape_Leng = shape_leng;
        this.color = color;
        this.proyId = proyId;
        this.idSubproyecto = idSubproyecto;
    }

    public String getId() {
        return id;
    }

    public ArrayList<LatLng> getCoordenadas() {
        return coordenadas;
    }

    public String getTramo() {
        return Tramo;
    }

    public int getOBJECTID_1() {
        return OBJECTID_1;
    }

    public int getOBJECTID() {
        return OBJECTID;
    }

    public float getDistancia() {
        return Distancia;
    }

    public int getOBJECTID_2() {
        return OBJECTID_2;
    }

    public String getPoblacion() {
        return Poblacion;
    }

    public float getShape_Leng() {
        return Shape_Leng;
    }

    public String getColor() {
        return color;
    }

    public int getProyId() {
        return proyId;
    }

    public int getIdSubproyecto() {
        return idSubproyecto;
    }

    public String convierteACadena() {
        String cadena = "";
        cadena = "id: " + this.getId()
                + " tramo: " + this.getTramo()
                + " objectid1: " + this.getOBJECTID()
                + " objectid: " + this.getOBJECTID()
                + " objectid2: " + this.getOBJECTID_2()
                + " distancia: " + this.getDistancia()
                + " poblacion: " + this.getPoblacion()
                + " shapeleng: " + this.getShape_Leng()
                + " color: " + this.getColor()
                + " poryId: " + this.getProyId()
                + " idSubproyecto: " + this.getIdSubproyecto();
        return cadena;
    }
}
