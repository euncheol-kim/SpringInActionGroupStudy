package springinaction.tacos.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import springinaction.tacos.domain.entity.Ingredient;

import static springinaction.tacos.domain.entity.Ingredient.*;

@Getter
@Setter
@Relation(value = "ingredient",collectionRelation = "ingredients")
public class IngredientResources extends RepresentationModel<IngredientResources> {

    private String name;
    private Type type;

    public IngredientResources(Ingredient ingredient) {
        this.name = ingredient.getName();
        this.type = ingredient.getType();
    }
}
