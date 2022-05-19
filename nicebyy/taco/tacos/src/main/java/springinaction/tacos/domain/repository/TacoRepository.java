package springinaction.tacos.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springinaction.tacos.domain.entity.Taco;

@Repository
public interface TacoRepository extends JpaRepository<Taco,Long> {
}
