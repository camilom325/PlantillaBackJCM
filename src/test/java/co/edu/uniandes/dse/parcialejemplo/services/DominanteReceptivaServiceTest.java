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
import co.edu.uniandes.dse.parcialejemplo.services.DominanteReceptivaService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(DominanteReceptivaService.class)
public class DominanteReceptivaServiceTest {

    @Autowired
    private DominanteReceptivaService dominanteReceptivaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private DominanteEntity dominante = new DominanteEntity();
    private List<ReceptivaEntity> receptivaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReceptivaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from DominanteEntity").executeUpdate();
    }

    private void insertData() {

        dominante = factory.manufacturePojo(DominanteEntity.class);
        entityManager.persist(dominante);

        for (int i = 0; i < 3; i++) {
            ReceptivaEntity entity = factory.manufacturePojo(ReceptivaEntity.class);
            entityManager.persist(entity);
            entity.getDominantes().add(dominante);
            receptivaList.add(entity);
            dominante.getReceptivas().add(entity);
        }
    }

    // TODO tests de create

    @Test
    void testAddReceptiva() throws EntityNotFoundException, IllegalOperationException {
        DominanteEntity newDominante = factory.manufacturePojo(DominanteEntity.class);
        entityManager.persist(newDominante);

        ReceptivaEntity receptiva = factory.manufacturePojo(ReceptivaEntity.class);
        entityManager.persist(receptiva);

        dominanteReceptivaService.addReceptiva(newDominante.getId(), receptiva.getId());

        ReceptivaEntity lastReceptiva = dominanteReceptivaService.getReceptiva(newDominante.getId(), receptiva.getId());
        assertEquals(receptiva.getId(), lastReceptiva.getId());
        assertEquals(receptiva.getPropiedad(), lastReceptiva.getPropiedad());
    }

    @Test
    void testAddInvalidReceptiva() {
        assertThrows(EntityNotFoundException.class, () -> {
            DominanteEntity newDominante = factory.manufacturePojo(DominanteEntity.class);
            entityManager.persist(newDominante);
            dominanteReceptivaService.addReceptiva(newDominante.getId(), 0L);
        });
    }

    @Test
    void testAddReceptivaInvalidDominante() throws EntityNotFoundException, IllegalOperationException {
        assertThrows(EntityNotFoundException.class, () -> {
            ReceptivaEntity receptiva = factory.manufacturePojo(ReceptivaEntity.class);
            entityManager.persist(receptiva);
            dominanteReceptivaService.addReceptiva(0L, receptiva.getId());
        });
    }

}
