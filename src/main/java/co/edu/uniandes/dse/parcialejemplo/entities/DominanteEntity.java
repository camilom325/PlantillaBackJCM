package co.edu.uniandes.dse.parcialejemplo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class DominanteEntity extends BaseEntity {

    // Atributos comunes
    private String propiedad;
    private int numero;

    // Atributos temporales
    @Temporal(TemporalType.DATE)
    private Date fecha;

    // Relaciones

    // TODO caso de ser de muchas a una y este es el muchas
    // @PodamExclude
    // @ManyToOne
    // private ReceptivaEntity receptiva;

    // TODO caso de ser muchos a muchos y este domine la relación (no tiene el
    // mapped by)
    @PodamExclude
    @ManyToMany
    private List<ReceptivaEntity> receptivas = new ArrayList<>();

    // TODO caso de ser uno a uno y este domine la relación (no tiene el mapped by)
    // @PodamExclude
    // @OneToOne
    // private ReceptivaEntity receptiva = new ArrayList<>();

}
