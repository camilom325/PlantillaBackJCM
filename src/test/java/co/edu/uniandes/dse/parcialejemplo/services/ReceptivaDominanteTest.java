package co.edu.uniandes.dse.parcialejemplo.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import co.edu.uniandes.dse.parcialejemplo.entities.ReceptivaEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.DominanteEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import({ ReceptivaDominanteService.class, DominanteService.class })
public class ReceptivaDominanteTest {
    @Autowired
    private ReceptivaDominanteService receptivaDominanteService;

    @Autowired
    private DominanteService dominanteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ReceptivaEntity receptiva = new ReceptivaEntity();
    private List<DominanteEntity> dominanteList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReceptivaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from DominanteEntity").executeUpdate();
    }

    // TODO pruebas de create

    private void insertData() {

        receptiva = factory.manufacturePojo(ReceptivaEntity.class);
        entityManager.persist(receptiva);

        for (int i = 0; i < 3; i++) {
            DominanteEntity entity = factory.manufacturePojo(DominanteEntity.class);
            entity.getReceptivas().add(receptiva);
            entityManager.persist(entity);
            dominanteList.add(entity);
            receptiva.getDominantes().add(entity);
        }
    }

    /**
     * Prueba para asociar un libro a un receptiva.
     *
     */
    @Test
    void testAddDominante() throws EntityNotFoundException, IllegalOperationException {
        DominanteEntity newDominante = factory.manufacturePojo(DominanteEntity.class);
        dominanteService.createDominante(newDominante);

        DominanteEntity dominanteEntity = receptivaDominanteService.addDominante(receptiva.getId(),
                newDominante.getId());
        assertNotNull(dominanteEntity);

        // TODO poner todas los atributos
        assertEquals(dominanteEntity.getId(), newDominante.getId());
        assertEquals(dominanteEntity.getPropiedad(), newDominante.getPropiedad());

        DominanteEntity lastDominante = receptivaDominanteService.getDominante(receptiva.getId(), newDominante.getId());

        // TODO poner todas los atributos
        assertEquals(lastDominante.getId(), newDominante.getId());
        assertEquals(lastDominante.getPropiedad(), newDominante.getPropiedad());

    }

    @Test
    void testAddInvalidDominante() {
        assertThrows(EntityNotFoundException.class, () -> {
            receptivaDominanteService.addDominante(receptiva.getId(), 0L);
        });
    }

    @Test
    void testAddDominanteInvalidReceptiva() {
        assertThrows(EntityNotFoundException.class, () -> {
            DominanteEntity newDominante = factory.manufacturePojo(DominanteEntity.class);
            dominanteService.createDominante(newDominante);
            receptivaDominanteService.addDominante(0L, newDominante.getId());
        });
    }

}
