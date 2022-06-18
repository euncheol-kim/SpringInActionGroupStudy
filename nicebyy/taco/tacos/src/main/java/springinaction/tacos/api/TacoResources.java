package springinaction.tacos.api;

import lombok.Getter;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;
import springinaction.tacos.domain.entity.Taco;

import java.util.Date;

@Relation(value = "taco",collectionRelation = "tacos")
public class TacoResources extends RepresentationModel<TacoResources> {

    private static final IngredientResourceAssembler ingredientResourceAssembler = new IngredientResourceAssembler();

    @Getter
    private final String name;

    @Getter
    private final Date createAt;

    @Getter
    private final CollectionModel<EntityModel<IngredientResources>> ingredients;

    public TacoResources(Taco taco) {
        this.name=taco.getName();
        this.createAt=taco.getCreatedAt();
        this.ingredients=ingredientResourceAssembler.toCollectionModel(taco.getIngredients());
    }
}
