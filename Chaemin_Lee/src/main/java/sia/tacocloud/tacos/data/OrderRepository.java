package sia.tacocloud.tacos.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.tacos.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {

}
