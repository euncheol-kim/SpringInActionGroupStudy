package springinaction.tacos.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springinaction.tacos.domain.entity.Taco;
import springinaction.tacos.domain.repository.TacoRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//@RestController
//@RequestMapping(path = "/api/design",produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignTacoApiController {

    private final TacoRepository tacoRepository;
    private final TacoResourceAssembler assembler;
//    private final EntityLink entityLink;


//    @GetMapping("/recent")
    public CollectionModel<Taco> recentTacosV1(){
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();
        CollectionModel<Taco> recentResources = CollectionModel.of(tacos);

        Link link = linkTo(methodOn(DesignTacoApiController.class).recentTacosV1())
                .withRel("recents");
        recentResources.add(link);

        return recentResources;

    }

    @GetMapping("/recent")
    public CollectionModel<EntityModel<TacoResources>> recentTacos(){
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();

        return assembler.toCollectionModel(tacos);
    }



    @GetMapping
    public List<Taco> findAll(){
        return tacoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> findTacoById(@PathVariable Long id){
        Optional<Taco> findTaco = tacoRepository.findById(id);

        return findTaco.map(taco -> new ResponseEntity<>(taco, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @PostMapping(path = "/new",consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Taco createTaco(@RequestBody Taco taco){
        return tacoRepository.save(taco);
    }
}
