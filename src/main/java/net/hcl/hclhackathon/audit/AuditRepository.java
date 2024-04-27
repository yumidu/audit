package net.hcl.hclhackathon.audit;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
    

@Repository
public interface AuditRepository extends JpaRepository<Audit,String>{


Optional<Audit>  findById(String id);
    
}