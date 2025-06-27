package com.ahorrofamiliar.dto;

import java.math.BigDecimal;

/**
 * DTO for usuario_ingreso table
 */
public class UsuarioIngresoDTO {
    private int id;
    private int idUsuario;
    private BigDecimal aporte;
    private String situacion;

    // Constructors
    public UsuarioIngresoDTO() {
    }

    public UsuarioIngresoDTO(int id, int idUsuario, BigDecimal aporte, String situacion) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.aporte = aporte;
        this.situacion = situacion;
    }

    public UsuarioIngresoDTO(int idUsuario, BigDecimal aporte, String situacion) {
        this.idUsuario = idUsuario;
        this.aporte = aporte;
        this.situacion = situacion;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigDecimal getAporte() {
        return aporte;
    }

    public void setAporte(BigDecimal aporte) {
        this.aporte = aporte;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    @Override
    public String toString() {
        return "UsuarioIngresoDTO{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", aporte=" + aporte +
                ", situacion='" + situacion + '\'' +
                '}';
    }
}