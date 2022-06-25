package tacos.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tacos.Taco;
import tacos.data.TacoRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(path = "/design", produces = "application/json")
public class DesignTacoController {
    private TacoRepository tacoRepo;

    @Autowired
    EntityLinks entityLinks;

    public DesignTacoController(TacoRepository tacoRepo) {
        this.tacoRepo = tacoRepo;
    }

    @GetMapping("/recent")
    public CollectionModel<TacoResource> recentTacos() {
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());

//        List<Taco> tacos = tacoRepo.findAll(page).getContent();
//        CollectionModel<EntityModel<Taco>> recentResources = CollectionModel.wrap(tacos);
//
//        recentResources.add(
//                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
//                        .slash("recent")
//                        .withRel("recents")
//                );

        List<Taco> tacos = tacoRepo.findAll(page).getContent();
        List<TacoResource> tacoResources = (List<TacoResource>) new TacoResourceAssembler().toModel((Taco) tacos);
        CollectionModel<TacoResource> recentResources = new CollectionModel<TacoResource>(tacoResources);

        recentResources.add(
                WebMvcLinkBuilder.linkTo(DesignTacoController.class)
                        .slash("recent")
                        .withRel("recents")
                );

        return recentResources;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
        Optional<Taco> optTaco = tacoRepo.findById(id);
        if (optTaco.isPresent()) {
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }


    @PostMapping(consumes="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco postTaco(@RequestBody Taco taco) {
        return tacoRepo.save(taco);
    }
}
