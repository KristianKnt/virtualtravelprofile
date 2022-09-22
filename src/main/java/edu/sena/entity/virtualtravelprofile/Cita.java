/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sena.entity.virtualtravelprofile;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Consola de Juegos
 */
@Entity
@Table(name = "cita")
@NamedQueries({
    @NamedQuery(name = "Cita.findAll", query = "SELECT c FROM Cita c")})
public class Cita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "idCita")
    private String idCita;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaCita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCita;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "hora")
    private String hora;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "estadoCita")
    private String estadoCita;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "observaciones")
    private String observaciones;
    @OneToMany(mappedBy = "idCita", fetch = FetchType.LAZY)
    private Collection<Tipocita> tipocitaCollection;
    @JoinColumn(name = "idClienteCita", referencedColumnName = "idcliente")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente idClienteCita;
    @JoinColumn(name = "idAsesorCita", referencedColumnName = "idAsesor")
    @ManyToOne(fetch = FetchType.LAZY)
    private Asesor idAsesorCita;

    public Cita() {
    }

    public Cita(String idCita) {
        this.idCita = idCita;
    }

    public Cita(String idCita, Date fechaCita, String hora, String estadoCita, String observaciones) {
        this.idCita = idCita;
        this.fechaCita = fechaCita;
        this.hora = hora;
        this.estadoCita = estadoCita;
        this.observaciones = observaciones;
    }

    public String getIdCita() {
        return idCita;
    }

    public void setIdCita(String idCita) {
        this.idCita = idCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadoCita() {
        return estadoCita;
    }

    public void setEstadoCita(String estadoCita) {
        this.estadoCita = estadoCita;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Collection<Tipocita> getTipocitaCollection() {
        return tipocitaCollection;
    }

    public void setTipocitaCollection(Collection<Tipocita> tipocitaCollection) {
        this.tipocitaCollection = tipocitaCollection;
    }

    public Cliente getIdClienteCita() {
        return idClienteCita;
    }

    public void setIdClienteCita(Cliente idClienteCita) {
        this.idClienteCita = idClienteCita;
    }

    public Asesor getIdAsesorCita() {
        return idAsesorCita;
    }

    public void setIdAsesorCita(Asesor idAsesorCita) {
        this.idAsesorCita = idAsesorCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCita != null ? idCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cita)) {
            return false;
        }
        Cita other = (Cita) object;
        if ((this.idCita == null && other.idCita != null) || (this.idCita != null && !this.idCita.equals(other.idCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sena.entity.virtualtravelprofile.Cita[ idCita=" + idCita + " ]";
    }
    
}
