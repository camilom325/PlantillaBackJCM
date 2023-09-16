package co.edu.uniandes.dse.parcialejemplo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.parcialejemplo.entities.ReceptivaEntity;
import co.edu.uniandes.dse.parcialejemplo.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.parcialejemplo.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.parcialejemplo.repositories.ReceptivaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReceptivaService {

    @Autowired
    ReceptivaRepository receptivaRepository;

    @Transactional // TODO create
    public ReceptivaEntity createReceptiva(ReceptivaEntity receptivaEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de receptiva");

        // Verificación que tenga sí o sí un atributo
        if (receptivaEntity.getPropiedad() == null)
            throw new IllegalOperationException("Propiedad no puede estar vacío");

        // verificación que no se repita el valor de un atributo
        if (!receptivaRepository.findByPropiedad(receptivaEntity.getPropiedad()).isEmpty())
            throw new IllegalOperationException("Propiedad ya existe para otra entidad");

        // verificación que tenga caracteres que coinciden
        // if (!receptivaEntity.getPropiedad().startsWith("A"))
        // throw new IllegalOperationException("Propiedad no empieza por la letra A");

        // verificación de la longitud de caracteres de un número
        int n = receptivaEntity.getNumero();
        String stringN = String.valueOf(n);
        if (stringN.length() < 2)
            throw new IllegalOperationException("La propiedad tiene menor longitud que 2");

        log.info("Termina proceso de creación de receptiva");
        return receptivaRepository.save(receptivaEntity);
    }

    @Transactional // TODO get all
    public List<ReceptivaEntity> getReceptivas() {
        log.info("Inicia proceso de consultar todos los receptivas");
        return receptivaRepository.findAll();
    }

    @Transactional // TODO get
    public ReceptivaEntity getReceptiva(Long receptivaId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("No se encuentra receptiva con este id");
        log.info("Termina proceso de consultar receptiva con id = {0}", receptivaId);
        return receptivaEntity.get();
    }

    @Transactional // TODO update
    public ReceptivaEntity updateReceptiva(Long receptivaId, ReceptivaEntity receptiva)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrado para actualizar");

        // TODO se pegan las mismas validaciones que en create
        if (receptiva.getPropiedad() == null)
            throw new IllegalOperationException("Receptiva debe tener algo");

        // TODO se hace el set de las propiedades a actualizar
        receptiva.setId(receptivaId);

        log.info("Termina proceso de actualizar receptiva con id = {0}", receptivaId);
        return receptivaRepository.save(receptiva);
    }

    @Transactional // TODO delete
    public void deleteReceptiva(Long receptivaId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar receptiva con id = {0}", receptivaId);
        Optional<ReceptivaEntity> receptivaEntity = receptivaRepository.findById(receptivaId);
        if (receptivaEntity.isEmpty())
            throw new EntityNotFoundException("Receptiva no encontrado al borrar");

        receptivaRepository.deleteById(receptivaId);
        log.info("Termina proceso de borrar receptiva con id = {0}", receptivaId);
    }

}
