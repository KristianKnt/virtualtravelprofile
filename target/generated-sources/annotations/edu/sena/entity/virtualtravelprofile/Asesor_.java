package edu.sena.entity.virtualtravelprofile;

import edu.sena.entity.virtualtravelprofile.Cita;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.7.7.v20200504-rNA", date="2022-09-12T12:43:46")
@StaticMetamodel(Asesor.class)
public class Asesor_ { 

    public static volatile CollectionAttribute<Asesor, Cita> citaCollection;
    public static volatile SingularAttribute<Asesor, String> estado;
    public static volatile SingularAttribute<Asesor, String> idAsesor;
    public static volatile SingularAttribute<Asesor, String> apellido;
    public static volatile SingularAttribute<Asesor, Integer> telefono;
    public static volatile SingularAttribute<Asesor, String> nombre;
    public static volatile SingularAttribute<Asesor, String> email;

}