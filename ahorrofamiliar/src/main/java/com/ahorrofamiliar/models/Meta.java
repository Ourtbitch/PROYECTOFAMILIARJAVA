/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.models;

import java.util.Date;

/**
 *
 * @author Alondra
 */
public class Meta {

    private int id;
    private String nombre_meta;
    private Date Fecha_creacion;
    private Date Fecha_fin;
    private double Importe_Inicial_e;
    private double Importe_Final_e;
    private double Importe_Inicial_r;
    private double Importe_Final_r;
    private String situacion;

    public int getId() {
        return id;
    }

    public String getNombre_meta() {
        return nombre_meta;
    }

    public Date getFecha_creacion() {
        return Fecha_creacion;
    }

    public Date getFecha_fin() {
        return Fecha_fin;
    }

    public double getImporte_Inicial_e() {
        return Importe_Inicial_e;
    }

    public double getImporte_Final_e() {
        return Importe_Final_e;
    }

    public double getImporte_Inicial_r() {
        return Importe_Inicial_r;
    }

    public double getImporte_Final_r() {
        return Importe_Final_r;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre_meta(String nombre_meta) {
        this.nombre_meta = nombre_meta;
    }

    public void setFecha_creacion(Date Fecha_creacion) {
        this.Fecha_creacion = Fecha_creacion;
    }

    public void setFecha_fin(Date Fecha_fin) {
        this.Fecha_fin = Fecha_fin;
    }

    public void setImporte_Inicial_e(double Importe_Inicial_e) {
        this.Importe_Inicial_e = Importe_Inicial_e;
    }

    public void setImporte_Final_e(double Importe_Final_e) {
        this.Importe_Final_e = Importe_Final_e;
    }

    public void setImporte_Inicial_r(double Importe_Inicial_r) {
        this.Importe_Inicial_r = Importe_Inicial_r;
    }

    public void setImporte_Final_r(double Importe_Final_r) {
        this.Importe_Final_r = Importe_Final_r;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String toString() {
        return this.nombre_meta; // o el atributo que quieras mostrar
    }

}
