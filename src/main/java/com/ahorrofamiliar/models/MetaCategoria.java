/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.models;

/**
 *
 * @author USER
 */
public class MetaCategoria {

    private int id;
    private String nombre;
    private String descripcion;
    private String situacion;

    public MetaCategoria() {
        this.situacion = "A";
    }

    public MetaCategoria(int id, String nombre, String descripcion, String situacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.situacion = situacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    @Override
    public String toString() {
        return this.nombre;
    }
}
