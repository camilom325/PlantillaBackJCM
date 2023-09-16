package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.DominanteEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.DominanteRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.ReceptivaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DominanteService {

    @Autowired
    DominanteRepository dominanteRepository;

    @Autowired
    ReceptivaRepository receptivaRepository;

    @Transactional // TODO create
    public DominanteEntity createDominante(DominanteEntity dominanteEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de dominante");

        // // Verificación de otra clase no vacía dentro de esta
        // if (dominanteEntity.getReceptivas() == null)
        // throw new IllegalOperationException("Receptiva no puede estar vacío");

        // Verificación que tenga sí o sí un atributo
        if (dominanteEntity.getPropiedad() == null)
            throw new IllegalOperationException("Propiedad no puede estar vacío");

        // verificación que no se repita el valor de un atributo
        if (!dominanteRepository.findByPropiedad(dominanteEntity.getPropiedad()).isEmpty())
            throw new IllegalOperationException("Propiedad ya existe para otra entidad");

        // verificación que tenga caracteres que coinciden
        // if (!dominanteEntity.getPropiedad().startsWith("A"))
        // throw new IllegalOperationException("Propiedad no empieza por la letra A");

        // verificación de la longitud de caracteres de un número
        int n = dominanteEntity.getNumero();
        String stringN = String.valueOf(n);
        if (stringN.length() < 2)
            throw new IllegalOperationException("La propiedad tiene menor longitud que 2");

        log.info("Termina proceso de creación de dominante");
        return dominanteRepository.save(dominanteEntity);
    }

    @Transactional // TODO get all
    public List<DominanteEntity> getDominantes() {
        log.info("Inicia proceso de consultar todos los dominantes");
        return dominanteRepository.findAll();
    }

    @Transactional // TODO get
    public DominanteEntity getDominante(Long dominanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar dominante con id = {0}", dominanteId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("No se encuentra dominante con este id");
        log.info("Termina proceso de consultar dominante con id = {0}", dominanteId);
        return dominanteEntity.get();
    }

    @Transactional // TODO update
    public DominanteEntity updateDominante(Long dominanteId, DominanteEntity dominante)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar dominante con id = {0}", dominanteId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado para actualizar");

        // TODO se pegan las mismas validaciones que en create

        if (dominante.getPropiedad() == null)
            throw new IllegalOperationException("Propiedad no puede estar vacío...");

        // TODO se hace el set de las propiedades a actualizar
        dominante.setId(dominanteId);

        log.info("Termina proceso de actualizar dominante con id = {0}", dominanteId);
        return dominanteRepository.save(dominante);
    }

    @Transactional // TODO delete
    public void deleteDominante(Long dominanteId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar dominante con id = {0}", dominanteId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado al borrar");

        dominanteRepository.deleteById(dominanteId);
        log.info("Termina proceso de borrar dominante con id = {0}", dominanteId);
    }

}
