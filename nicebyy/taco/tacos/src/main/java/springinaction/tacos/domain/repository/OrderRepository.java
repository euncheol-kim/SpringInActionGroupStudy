package springinaction.tacos.domain.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import springinaction.tacos.domain.entity.Order;
import springinaction.tacos.domain.entity.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserOrderByPlacedAtDesc(User user, Pageable pageable);
}
