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
public class DominanteReceptivaService {

    @Autowired
    private DominanteRepository dominanteRepository;

    @Autowired
    private ReceptivaRepository receptivaRepository;

    /**
     * Asocia un Receptiva existente a un Dominante
     *
     * @param dominanteId Identificador de la instancia de Dominante
     * @param receptivaId Identificador de la instancia de Receptiva
     * @return Instancia de ReceptivaEntity que fue asociada a Dominante
     */
    @Transactional
    public ReceptivaEntity addReceptiva(Long dominanteId, Long receptivaId) throws EntityNotFoundException {
        log.info("Inicia proceso de asociarle receptiva a dominante con id = {0}", dominanteId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrado al asociarle receptiva a dominante");

        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado al asociarle receptiva a dominante");

        dominanteEntity.get().getReceptivas().add(receptivaEntity.get());
        log.info("Termina proceso de asociarle receptiva a dominante con id = {0}", dominanteId);
        return receptivaEntity.get();
    }

    /**
     * Obtiene una colección de instancias de ReceptivaEntity asociadas a una
     * instancia
     * de Dominante
     *
     * @param dominanteId Identificador de la instancia de Dominante
     * @return Colección de instancias de ReceptivaEntity asociadas a la instancia
     *         de
     *         Dominante
     */
    @Transactional
    public List<ReceptivaEntity> getReceptivas(Long dominanteId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar todos receptivas de dominante con id = {0}", dominanteId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrado al obtener receptivas de dominante");
        log.info("Finaliza proceso de consultar receptivas de dominante con id = {0}", dominanteId);
        return dominanteEntity.get().getReceptivas();
    }

    /**
     * Obtiene una instancia de ReceptivaEntity asociada a una instancia de
     * Dominante
     *
     * @param dominanteId Identificador de la instancia de Dominante
     * @param receptivaId Identificador de la instancia de Receptiva
     * @return La entidad del Autor asociada a dominante
     */
    @Transactional
    public ReceptivaEntity getReceptiva(Long dominanteId, Long receptivaId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de consultar receptiva de dominante con id = {0}", dominanteId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);

        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrado al obtener receptiva de dominante");

        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("Dominante no encontrada al obtener receptiva de dominante");
        log.info("Termina proceso de consultar receptiva de dominante con id = {0}", dominanteId);
        if (!dominanteEntity.get().getReceptivas().contains(receptivaEntity.get()))
            throw new IllegalOperationException("Receptva no está asociada a dominante");

        return receptivaEntity.get();
    }

    @Transactional
    /**
     * Remplaza las instancias de Receptiva asociadas a una instancia de Dominante
     *
     * @param dominanteId Identificador de la instancia de Dominante
     * @param list        Colección de instancias de ReceptivaEntity a asociar a
     *                    instancia
     *                    de Dominante
     * @return Nueva colección de ReceptivaEntity asociada a la instancia de
     *         Dominante
     */
    public List<ReceptivaEntity> replaceReceptivas(Long dominanteId, List<ReceptivaEntity> list)
            throws EntityNotFoundException {
        log.info("Inicia proceso de reemplazar receptivas de dominante con id = {0}", dominanteId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);
        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("no se encuentra dominante al reemplazar las receptivas del dominante");

        for (ReceptivaEntity receptiva : list) {
            Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptiva.getId());
            if (receptivaEntity.isEmpty())
                throw new EntityNotFoundException(
                        "no se encuentra receptiva al reemplazar las receptivas del dominante");

            if (!dominanteEntity.get().getReceptivas().contains(receptivaEntity.get()))
                dominanteEntity.get().getReceptivas().add(receptivaEntity.get());
        }
        log.info("Termina proceso de reemplazar receptivas de dominante con id = {0}", dominanteId);
        return getReceptivas(dominanteId);
    }

    @Transactional
    /**
     * Desasocia un Receptiva existente de un Dominante existente
     *
     * @param dominanteId Identificador de la instancia de Dominante
     * @param receptivaId Identificador de la instancia de Receptiva
     */
    public void removeReceptiva(Long dominanteId, Long receptivaId) throws EntityNotFoundException {
        log.info("Inicia proceso de borrar receptiva de dominante con id = {0}", dominanteId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        Optional<DominanteEntity> dominanteEntity = dominanteRepository.findById(dominanteId);

        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("no se encuentra receptiva al borrar las receptivas del dominante");

        if (dominanteEntity.isEmpty())
            throw new EntityNotFoundException("no se encuentra dominante al borrar las receptivas del dominante");

        dominanteEntity.get().getReceptivas().remove(receptivaEntity.get());

        log.info("Termina proceso de borrar receptiva de dominante con id = {0}", dominanteId);
    }
}
