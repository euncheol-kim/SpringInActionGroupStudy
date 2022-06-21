package springinaction.tacos.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import springinaction.tacos.domain.entity.Taco;
import springinaction.tacos.domain.repository.TacoRepository;

import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class RecentTacosController {

    private final TacoRepository tacoRepository;
    private final TacoResourceAssembler assembler;

    @GetMapping(path = "/tacos/recent",produces = "application/hal+json")
    public ResponseEntity<CollectionModel<EntityModel<TacoResources>>> recentTacos(){
        PageRequest pageRequest = PageRequest
                .of(0, 12, Sort.by("createdAt").descending());

        List<Taco> tacos = tacoRepository.findAll(pageRequest).getContent();

        return new ResponseEntity<>(assembler.toCollectionModel(tacos), HttpStatus.OK);
    }
}
