package portfolio.pdf_reader;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio
@Repository
public interface RepositorioDocumento extends JpaRepository<Documento, Long>{

}
