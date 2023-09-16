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

import co.edu.uniandes.dse.parcialejemplo.entities.DominanteEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.ReceptivaEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Pruebas de logica de Dominantes
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@Import(DominanteService.class)
class DominanteServiceTest {

    @Autowired
    private DominanteService dominanteService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<DominanteEntity> dominanteList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from DominanteEntity");
        entityManager.getEntityManager().createQuery("delete from ReceptivaEntity");
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            DominanteEntity dominanteEntity = factory.manufacturePojo(DominanteEntity.class);
            entityManager.persist(dominanteEntity);
            dominanteList.add(dominanteEntity);
        }

        ReceptivaEntity receptivaEntity = factory.manufacturePojo(ReceptivaEntity.class);
        entityManager.persist(receptivaEntity);
        receptivaEntity.getDominantes().add(dominanteList.get(0));
        dominanteList.get(0).getReceptivas().add(receptivaEntity);
    }

    /**
     * Prueba para crear un Dominante
     */
    @Test // TODO prueba create
    void testCreateDominante() throws EntityNotFoundException, IllegalOperationException {
        DominanteEntity newEntity = factory.manufacturePojo(DominanteEntity.class);
        DominanteEntity result = dominanteService.createDominante(newEntity);
        assertNotNull(result);
        DominanteEntity entity = entityManager.find(DominanteEntity.class, result.getId());
        // TODO agregar todos los getters
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getPropiedad(), entity.getPropiedad());
    }

    @Test // TODO vrificar que propiedad no se anulo
    void testCreateDominanteWithNoValidPropiedad() {
        assertThrows(IllegalOperationException.class, () -> {
            DominanteEntity newEntity = factory.manufacturePojo(DominanteEntity.class);
            newEntity.setPropiedad(null);
            dominanteService.createDominante(newEntity);
        });
    }

    @Test // TODO verificar que no sean duplicados
    void testCreateDominanteWithStoredPropiedad() {
        assertThrows(IllegalOperationException.class, () -> {
            DominanteEntity newEntity = factory.manufacturePojo(DominanteEntity.class);
            newEntity.setPropiedad(dominanteList.get(0).getPropiedad());
            dominanteService.createDominante(newEntity);
        });
    }

    @Test // TODO get dominantes
    void testGetDominantes() {
        List<DominanteEntity> list = dominanteService.getDominantes();
        assertEquals(dominanteList.size(), list.size());
        for (DominanteEntity entity : list) {
            boolean found = false;
            for (DominanteEntity storedEntity : dominanteList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test // consultar solo un dominante
    void testGetDominante() throws EntityNotFoundException {
        DominanteEntity entity = dominanteList.get(0);
        DominanteEntity resultEntity = dominanteService.getDominante(entity.getId());
        assertNotNull(resultEntity);
        // TODO agregar todos los atributos
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getPropiedad(), resultEntity.getPropiedad());
    }

    @Test // TODO dominante que no existe
    void testGetInvalidDominante() {
        assertThrows(EntityNotFoundException.class, () -> {
            dominanteService.getDominante(0L);
        });
    }

    @Test // TODO update cominante
    void testUpdateDominante() throws EntityNotFoundException, IllegalOperationException {
        DominanteEntity entity = dominanteList.get(0);
        DominanteEntity pojoEntity = factory.manufacturePojo(DominanteEntity.class);
        pojoEntity.setId(entity.getId());
        dominanteService.updateDominante(entity.getId(), pojoEntity);

        DominanteEntity resp = entityManager.find(DominanteEntity.class, entity.getId());
        // TODO agregar todos los atributos
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getPropiedad(), resp.getPropiedad());
    }

    @Test // TODO update inválido
    void testUpdateDominanteInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            DominanteEntity pojoEntity = factory.manufacturePojo(DominanteEntity.class);
            pojoEntity.setId(0L);
            dominanteService.updateDominante(0L, pojoEntity);
        });
    }

    @Test // TODO update de propiedad inválida
    void testUpdateDominanteWithNoValidISBN() {
        assertThrows(IllegalOperationException.class, () -> {
            DominanteEntity entity = dominanteList.get(0);
            DominanteEntity pojoEntity = factory.manufacturePojo(DominanteEntity.class);
            pojoEntity.setPropiedad(null);
            pojoEntity.setId(entity.getId());
            dominanteService.updateDominante(entity.getId(), pojoEntity);
        });
    }

    @Test // eliminar dominante
    void testDeleteDominante() throws EntityNotFoundException, IllegalOperationException {
        DominanteEntity entity = dominanteList.get(1);
        dominanteService.deleteDominante(entity.getId());
        DominanteEntity deleted = entityManager.find(DominanteEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test // eliminar dominante que no existe
    void testDeleteInvalidDominante() {
        assertThrows(EntityNotFoundException.class, () -> {
            dominanteService.deleteDominante(0L);
        });
    }

}