package sia.tacocloud.tacos.data;

import org.springframework.data.repository.CrudRepository;
import sia.tacocloud.tacos.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{

}
