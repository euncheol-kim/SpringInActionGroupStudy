package springinaction.tacos.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springinaction.tacos.domain.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

}
