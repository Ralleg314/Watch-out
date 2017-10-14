package com.example.comquimes.myapplicationd;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by alejandroayerdimbp on 14/10/17.
 */

public class CheckPoint {

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public CheckPoint(String ID, Double latitud, Double longitud, String info, Drawable image) {
        this.ID = ID;
        this.longitud = longitud;
        this.latitud = latitud;
        this.info = info;
        this.image = image;
    }

    public String getID() {

        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    String ID;
    Double longitud,latitud;
    String info;
    Drawable image;


}
