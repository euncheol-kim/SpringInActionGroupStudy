package springinaction.tacos;

import org.aspectj.weaver.ast.Or;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinaction.tacos.domain.entity.Ingredient;
import springinaction.tacos.domain.entity.Order;
import springinaction.tacos.domain.entity.Taco;
import springinaction.tacos.domain.entity.User;
import springinaction.tacos.domain.repository.IngredientRepository;
import springinaction.tacos.domain.repository.OrderRepository;
import springinaction.tacos.domain.repository.TacoRepository;
import springinaction.tacos.domain.repository.UserRepository;
import springinaction.tacos.security.RegistrationForm;
import springinaction.tacos.web.controller.OrderProps;

import java.util.List;

import static springinaction.tacos.domain.entity.Ingredient.*;


@SpringBootApplication
public class TacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacosApplication.class, args);
    }

    @Bean
    public CommandLineRunner saveInitIngredient(IngredientRepository ingredientRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
                ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
                ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
                ingredientRepository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
                ingredientRepository        .save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
                ingredientRepository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
                ingredientRepository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
                ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
                ingredientRepository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
                ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

            }
        };
    }

    @Bean
    public CommandLineRunner initDesignAndOrder(IngredientRepository ingredientRepository,
                                                UserRepository userRepository,
                                                PasswordEncoder passwordEncoder,
                                                OrderRepository orderRepository,
                                                TacoRepository tacoRepository){

        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                RegistrationForm userForm = RegistrationForm.builder()
                        .username("user")
                        .password("1")
                        .fullname("1")
                        .street("aaa")
                        .city("111")
                        .state("111")
                        .zip("111")
                        .phone("1111")
                        .build();
                User user = userForm.toUser(passwordEncoder);
                userRepository.save(user);

                Taco taco = new Taco();
                taco.setName("MyTaco");
                List<Ingredient> ingredients = ingredientRepository.findAll();
                taco.setIngredients(ingredients);
                tacoRepository.save(taco);

                Order order = new Order();
                order.setCcCVV("345");
                order.setCcExpiration("1234");
                order.setDeliveryCity("seoul");
                order.setDeliveryState("gangnam");
                order.setDeliveryStreet("seohyunro");
                order.setDeliveryName(user.getUsername());
                order.setDeliveryZip("12412");
                order.setUser(user);

                order.addDesign(taco);

                orderRepository.save(order);
            }
        };
    }


}