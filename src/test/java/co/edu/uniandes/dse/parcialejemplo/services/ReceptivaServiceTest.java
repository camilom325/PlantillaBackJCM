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

/**
 * Pruebas de logica de Receptivas
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(ReceptivaService.class)
class ReceptivaServiceTest {

    @Autowired
    private ReceptivaService receptivaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ReceptivaEntity> receptivaList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReceptivaEntity");
        entityManager.getEntityManager().createQuery("delete from DominanteEntity");
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            ReceptivaEntity receptivaEntity = factory.manufacturePojo(ReceptivaEntity.class);
            entityManager.persist(receptivaEntity);
            receptivaList.add(receptivaEntity);
        }

        DominanteEntity dominanteEntity = factory.manufacturePojo(DominanteEntity.class);
        entityManager.persist(dominanteEntity);
        dominanteEntity.getReceptivas().add(receptivaList.get(0));
        receptivaList.get(0).getDominantes().add(dominanteEntity);
    }

    /**
     * Prueba para crear un Receptiva
     */
    @Test // TODO prueba create
    void testCreateReceptiva() throws EntityNotFoundException, IllegalOperationException {
        ReceptivaEntity newEntity = factory.manufacturePojo(ReceptivaEntity.class);
        ReceptivaEntity result = receptivaService.createReceptiva(newEntity);
        assertNotNull(result);
        ReceptivaEntity entity = entityManager.find(ReceptivaEntity.class, result.getId());
        // TODO agregar todos los getters
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getPropiedad(), entity.getPropiedad());
    }

    @Test // TODO vrificar que propiedad no se anulo
    void testCreateReceptivaWithNoValidPropiedad() {
        assertThrows(IllegalOperationException.class, () -> {
            ReceptivaEntity newEntity = factory.manufacturePojo(ReceptivaEntity.class);
            newEntity.setPropiedad(null);
            receptivaService.createReceptiva(newEntity);
        });
    }

    @Test // TODO verificar que no sean duplicados
    void testCreateReceptivaWithStoredPropiedad() {
        assertThrows(IllegalOperationException.class, () -> {
            ReceptivaEntity newEntity = factory.manufacturePojo(ReceptivaEntity.class);
            newEntity.setPropiedad(receptivaList.get(0).getPropiedad());
            receptivaService.createReceptiva(newEntity);
        });
    }

    @Test // TODO get receptivas
    void testGetReceptivas() {
        List<ReceptivaEntity> list = receptivaService.getReceptivas();
        assertEquals(receptivaList.size(), list.size());
        for (ReceptivaEntity entity : list) {
            boolean found = false;
            for (ReceptivaEntity storedEntity : receptivaList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test // consultar solo un receptiva
    void testGetReceptiva() throws EntityNotFoundException {
        ReceptivaEntity entity = receptivaList.get(0);
        ReceptivaEntity resultEntity = receptivaService.getReceptiva(entity.getId());
        assertNotNull(resultEntity);
        // TODO agregar todos los atributos
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getPropiedad(), resultEntity.getPropiedad());
    }

    @Test // TODO receptiva que no existe
    void testGetInvalidReceptiva() {
        assertThrows(EntityNotFoundException.class, () -> {
            receptivaService.getReceptiva(0L);
        });
    }

    @Test // TODO update cominante
    void testUpdateReceptiva() throws EntityNotFoundException, IllegalOperationException {
        ReceptivaEntity entity = receptivaList.get(0);
        ReceptivaEntity pojoEntity = factory.manufacturePojo(ReceptivaEntity.class);
        pojoEntity.setId(entity.getId());
        receptivaService.updateReceptiva(entity.getId(), pojoEntity);

        ReceptivaEntity resp = entityManager.find(ReceptivaEntity.class, entity.getId());
        // TODO agregar todos los atributos
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getPropiedad(), resp.getPropiedad());
    }

    @Test // TODO update inválido
    void testUpdateReceptivaInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            ReceptivaEntity pojoEntity = factory.manufacturePojo(ReceptivaEntity.class);
            pojoEntity.setId(0L);
            receptivaService.updateReceptiva(0L, pojoEntity);
        });
    }

    @Test // TODO update de propiedad inválida
    void testUpdateReceptivaWithNoValidPropiedad() {
        assertThrows(IllegalOperationException.class, () -> {
            ReceptivaEntity entity = receptivaList.get(0);
            ReceptivaEntity pojoEntity = factory.manufacturePojo(ReceptivaEntity.class);
            pojoEntity.setPropiedad(null);
            pojoEntity.setId(entity.getId());
            receptivaService.updateReceptiva(entity.getId(), pojoEntity);
        });
    }

    @Test // eliminar receptiva
    void testDeleteReceptiva() throws EntityNotFoundException, IllegalOperationException {
        ReceptivaEntity entity = receptivaList.get(1);
        receptivaService.deleteReceptiva(entity.getId());
        ReceptivaEntity deleted = entityManager.find(ReceptivaEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test // eliminar receptiva que no existe
    void testDeleteInvalidReceptiva() {
        assertThrows(EntityNotFoundException.class, () -> {
            receptivaService.deleteReceptiva(0L);
        });
    }

}
