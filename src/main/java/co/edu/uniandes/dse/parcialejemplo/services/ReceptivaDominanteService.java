package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.DominanteEntity;
import co.edu.uniandes.dse.parcialejemplo.entities.ReceptivaEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.DominanteRepository;
import co.edu.uniandes.dse.parcialejemplo.repositories.ReceptivaRepository;
import lombok.extern.slf4j.Slf4j;

// TODO (!) SOLO SI ES MANY TO MANY

@Slf4j
@Service
public class ReceptivaDominanteService {

    @Autowired
    private DominanteRepository dominanteRepository;

    @Autowired
    private ReceptivaRepository receptivaRepository;

    /**
     * Asocia un Dominante existente a un Receptiva
     *
     * @param receptivaId Identificador de la instancia de Receptiva
     * @param dominanteId Identificador de la instancia de Dominante
     * @return Instancia de DominanteEntity que fue asociada a Receptiva
     */

    @Transactional
    public DominanteEntity addDominante(Long receptivaId, Long dominanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle dominante a receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);

        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("No se encuentra receptiva al agregar dominante");

        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("No se encuentra dominante para receptiva");

        dominanteEntity.get().getReceptivas().add(receptivaEntity.get());
        log.info("Termina proceso de asociarle dominante a receptiva con id = {0}", receptivaId);
        return dominanteEntity.get();
    }

    /**
     * Obtiene una colección de instancias de DominanteEntity asociadas a una
     * instancia
     * de Receptiva
     *
     * @param receptivasId Identificador de la instancia de Receptiva
     * @return Colección de instancias de DominanteEntity asociadas a la instancia
     *         de
     *         Receptiva
     */
    @Transactional
    public List<DominanteEntity> getDominantes(Long receptivaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar dominantes de receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("No se encuentra receptiva al buscar dominantes");

        log.info("Termina proceso de consultar dominantes de receptiva con id = {0}", receptivaId);
        return receptivaEntity.get().getDominantes();
    }

    /**
     * Obtiene una instancia de DominanteEntity asociada a una instancia de
     * Receptiva
     *
     * @param receptivasId Identificador de la instancia de Receptiva
     * @param dominantesId Identificador de la instancia de Dominante
     * @return La entidadd de Libro del receptiva
     */
    @Transactional
    public DominanteEntity getDominante(Long receptivaId, Long dominanteId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar dominante con id = {0} de receptiva con id = " + receptivaId,
                dominanteId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);

        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrada consultando dominante");

        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado consultando dominante");

        log.info("Termina proceso de consultar dominante con id = {0} de receptiva con id = " + receptivaId,
                dominanteId);

        if (!dominanteEntity.get().getReceptivas().contains(receptivaEntity.get()))
            throw new IllegalOperationException("The dominante is not associated to the receptiva");

        return dominanteEntity.get();
    }

    /**
     * Remplaza las instancias de Dominante asociadas a una instancia de Receptiva
     *
     * @param receptivaId Identificador de la instancia de Receptiva
     * @param dominantes  Colección de instancias de DominanteEntity a asociar a
     *                    instancia
     *                    de Receptiva
     * @return Nueva colección de DominanteEntity asociada a la instancia de
     *         Receptiva
     */
    @Transactional
    public List<DominanteEntity> addDominantes(Long receptivaId, List<DominanteEntity> dominantes)
            throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar los libros asociados al receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontyrada al agregarlo a dominante");

        for (DominanteEntity dominante : dominantes) {
            Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominante.getId());
            if (dominanteEntity.isEmpty())
                throw new EntityNotFoundException("Dominante no encontrado al agregarlo en dominante");

            if (!dominanteEntity.get().getReceptivas().contains(receptivaEntity.get()))
                dominanteEntity.get().getReceptivas().add(receptivaEntity.get());
        }
        log.info("Finaliza proceso de reemplazar los libros asociados al receptiva con id = {0}", receptivaId);
        receptivaEntity.get().setDominantes(dominantes);
        return receptivaEntity.get().getDominantes();
    }

    /**
     * Desasocia un Dominante existente de un Receptiva existente
     *
     * @param receptivasId Identificador de la instancia de Receptiva
     * @param dominantesId Identificador de la instancia de Dominante
     */
    @Transactional
    public void removeDominante(Long receptivaId, Long dominanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar un libro del receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrado al eliminar dominante de receptiva");

        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado al remover de receptiva");

        dominanteEntity.get().getReceptivas().remove(receptivaEntity.get());
        receptivaEntity.get().getDominantes().remove(dominanteEntity.get());
        log.info("Finaliza proceso de borrar un libro del receptiva con id = {0}", receptivaId);
    }
}