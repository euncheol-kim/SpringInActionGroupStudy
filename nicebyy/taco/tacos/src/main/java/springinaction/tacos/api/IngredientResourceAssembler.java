package springinaction.tacos.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import springinaction.tacos.domain.entity.Ingredient;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class IngredientResourceAssembler implements RepresentationModelAssembler<Ingredient, EntityModel<IngredientResources>> {

    @Override
    public EntityModel<IngredientResources> toModel(Ingredient ingredient) {
        return EntityModel.of(new IngredientResources(ingredient),
                linkTo(methodOn(IngredientController.class).findIngredientById(ingredient.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<IngredientResources>> toCollectionModel(Iterable<? extends Ingredient> ingredients) {

        return RepresentationModelAssembler.super.toCollectionModel(ingredients).add(
                linkTo(IngredientController.class).withSelfRel()
        );
    }
}
