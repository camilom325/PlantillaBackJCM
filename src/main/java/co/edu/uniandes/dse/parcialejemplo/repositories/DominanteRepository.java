package co.edu.uniandes.dse.parcialejemplo.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.parcialejemplo.entities.DominanteEntity;

@Repository
public interface DominanteRepository extends JpaRepository<DominanteEntity, Long> {
    // TODO si es necesario
    List<DominanteEntity> findByPropiedad(String propiedad);

}
