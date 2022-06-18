package springinaction.tacos.api;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import springinaction.tacos.domain.entity.Taco;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class TacoResourceAssembler implements RepresentationModelAssembler<Taco, EntityModel<TacoResources>> {


    @Override
    public EntityModel<TacoResources> toModel(Taco taco) {

        return EntityModel.of(new TacoResources(taco),
                linkTo(methodOn(DesignTacoApiController.class).findTacoById(taco.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<TacoResources>> toCollectionModel(Iterable<? extends Taco> tacos) {
        return RepresentationModelAssembler.super.toCollectionModel(tacos).add(
                        linkTo(DesignTacoApiController.class).withSelfRel(),
                linkTo(methodOn(DesignTacoApiController.class).recentTacos()).withRel("recents")
        );
    }

}
