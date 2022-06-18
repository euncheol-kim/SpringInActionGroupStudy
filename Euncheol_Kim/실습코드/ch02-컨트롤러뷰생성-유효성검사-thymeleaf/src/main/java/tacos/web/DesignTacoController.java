package tacos.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j // 자바에서 사용하는 Simple Logging Facade Logger를 생성한다.
@Controller // 스프링의 컨트롤러로 식별하게 하여, 스프링 컨텍스트 빈으로 DesignController의 인스턴스를 생성한다.
@RequestMapping("/design")
public class DesignTacoController {
    @PostMapping
    public String processDesign(@Valid Taco design, Errors errors) {
        log.info("Processing design : " + design);
        if(errors.hasErrors()) {
            return "design";
        }

        // 이 지점에서 타코 디자인 (선택된 식자재 내역)을 저장한다.
        // 이 작업은 3장에서 진행

        return "redirect:/orders/current";
    }

    @GetMapping
    public String showDesignForm(Model model) { // Model -> Model은 컨트롤러와 데이터를 보여주는 뷰 사이에서 데이터를 운반하는 객체이다.
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values(); // enum Type의 상수를 배열에 저장한다.

        for(Type type : types){
            // Model.addAttribute -> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/ui/Model.html
            // Model.addAttribute(key, value) -> key와 value형태로 값으로 view에 전달할 수 있다.
            model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
        }

        model.addAttribute("taco", new Taco());

        return "design";
    }

    private List<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients.stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }

}
