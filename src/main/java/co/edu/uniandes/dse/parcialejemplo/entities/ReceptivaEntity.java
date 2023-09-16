package co.edu.uniandes.dse.parcialejemplo.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Entity
@Data
public class ReceptivaEntity extends BaseEntity {

    // Atributos comunes
    private String propiedad;
    private int numero;

    // Atributos temporales
    @Temporal(TemporalType.DATE)
    private Date fecha;

    // TODO caso de ser una a muchas y este es el uno
    // @PodamExclude
    // @OneToMany(mappedBy = "receptiva", cascade = CascadeType.PERSIST,
    // orphanRemoval = true)
    // private List<DominanteEntity> dominantes = new ArrayList<>();

    // TODO caso de ser muchos a muchos y este reciba la relación (tiene el mapped
    // by)
    @PodamExclude
    @ManyToMany(mappedBy = "receptivas", cascade = CascadeType.PERSIST)
    private List<DominanteEntity> dominantes = new ArrayList<>();

    // TODO caso de ser uno a uno y este reciba la relación (tiene el mapped by)
    // @PodamExclude
    // @OneToOne(mappedBy = "receptiva", cascade = CascadeType.PERSIST)
    // private ReceptivaEntity receptiva = new ArrayList<>();

}