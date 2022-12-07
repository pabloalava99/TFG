package com.viamedsalud.gvp.modelo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String episodio;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fecha")
    private Date fecha;

    private int nPaciente;

    private String nomPaciente;

    private String apellPaciente;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaNac;

    private String sexo;

    private String diagnostico;

    private String doctor;

    private int cama;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaAlta;

    private String alergia;

    private String medicacion;

    public Episodio() {
    }

    public Episodio(int id, String episodio, Date fecha, int nPaciente, String nomPaciente, String apellPaciente, Date fechaNac, String sexo, String diagnostico, String doctor, int cama, Date fechaAlta, String alergia, String medicacion) {
        this.id = id;
        this.episodio = episodio;
        this.fecha = fecha;
        this.nPaciente = nPaciente;
        this.nomPaciente = nomPaciente;
        this.apellPaciente = apellPaciente;
        this.fechaNac = fechaNac;
        this.sexo = sexo;
        this.diagnostico = diagnostico;
        this.doctor = doctor;
        this.cama = cama;
        this.fechaAlta = fechaAlta;
        this.alergia = alergia;
        this.medicacion = medicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEpisodio() {
        return episodio;
    }

    public void setEpisodio(String episodio) {
        this.episodio = episodio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getnPaciente() {
        return nPaciente;
    }

    public void setnPaciente(int nPaciente) {
        this.nPaciente = nPaciente;
    }

    public String getNomPaciente() {
        return nomPaciente;
    }

    public void setNomPaciente(String nomPaciente) {
        this.nomPaciente = nomPaciente;
    }

    public String getApellPaciente() {
        return apellPaciente;
    }

    public void setApellPaciente(String apellPaciente) {
        this.apellPaciente = apellPaciente;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public int getCama() {
        return cama;
    }

    public void setCama(int cama) {
        this.cama = cama;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public String getAlergia() {
        return alergia;
    }

    public void setAlergia(String alergia) {
        this.alergia = alergia;
    }

    public String getMedicacion() {
        return medicacion;
    }

    public void setMedicacion(String medicacion) {
        this.medicacion = medicacion;
    }
}
