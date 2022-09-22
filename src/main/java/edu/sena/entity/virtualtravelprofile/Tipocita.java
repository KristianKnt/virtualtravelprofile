/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sena.entity.virtualtravelprofile;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Consola de Juegos
 */
@Entity
@Table(name = "tipocita")
@NamedQueries({
    @NamedQuery(name = "Tipocita.findAll", query = "SELECT t FROM Tipocita t")})
public class Tipocita implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "iptipoCita")
    private String iptipoCita;
    @Size(max = 100)
    @Column(name = "motivo")
    private String motivo;
    @JoinColumn(name = "idCita", referencedColumnName = "idCita")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cita idCita;

    public Tipocita() {
    }

    public Tipocita(String iptipoCita) {
        this.iptipoCita = iptipoCita;
    }

    public String getIptipoCita() {
        return iptipoCita;
    }

    public void setIptipoCita(String iptipoCita) {
        this.iptipoCita = iptipoCita;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Cita getIdCita() {
        return idCita;
    }

    public void setIdCita(Cita idCita) {
        this.idCita = idCita;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iptipoCita != null ? iptipoCita.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipocita)) {
            return false;
        }
        Tipocita other = (Tipocita) object;
        if ((this.iptipoCita == null && other.iptipoCita != null) || (this.iptipoCita != null && !this.iptipoCita.equals(other.iptipoCita))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.sena.entity.virtualtravelprofile.Tipocita[ iptipoCita=" + iptipoCita + " ]";
    }
    
}
