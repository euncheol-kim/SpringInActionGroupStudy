package sia.tacocloud.tacos.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.tacos.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long> {

}
