package springinaction.tacos.api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springinaction.tacos.domain.entity.Taco;
import springinaction.tacos.domain.repository.TacoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/design/api",produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DesignTacoApiController {

    private final TacoRepository tacoRepository;
//    private final EntityLink entityLink;

    @GetMapping("/recent")
    public Iterable<Taco> recentTacos(){
        PageRequest pageRequest = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return tacoRepository.findAll(pageRequest).getContent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable Long id){
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
