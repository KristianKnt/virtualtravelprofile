package edu.sena.entity.virtualtravelprofile;

import edu.sena.entity.virtualtravelprofile.Asesor;
import edu.sena.entity.virtualtravelprofile.Cliente;
import edu.sena.entity.virtualtravelprofile.Tipocita;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-12T12:43:46")
@StaticMetamodel(Cita.class)
public class Cita_ { 

    public static volatile SingularAttribute<Cita, String> idCita;
    public static volatile SingularAttribute<Cita, Date> fechaCita;
    public static volatile SingularAttribute<Cita, String> estadoCita;
    public static volatile SingularAttribute<Cita, String> hora;
    public static volatile SingularAttribute<Cita, String> observaciones;
    public static volatile SingularAttribute<Cita, Asesor> idAsesorCita;
    public static volatile SingularAttribute<Cita, Cliente> idClienteCita;
    public static volatile CollectionAttribute<Cita, Tipocita> tipocitaCollection;

}