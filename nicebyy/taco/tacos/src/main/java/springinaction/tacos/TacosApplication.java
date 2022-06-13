package springinaction.tacos;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import springinaction.tacos.domain.entity.Ingredient;
import springinaction.tacos.domain.entity.User;
import springinaction.tacos.domain.repository.IngredientRepository;
import springinaction.tacos.domain.repository.UserRepository;
import springinaction.tacos.security.RegistrationForm;
import springinaction.tacos.web.controller.OrderProps;

import static springinaction.tacos.domain.entity.Ingredient.*;


@SpringBootApplication
public class TacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TacosApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(IngredientRepository ingredientRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                ingredientRepository.save(new Ingredient("FLTO", "Flour Tortilla", Type.WRAP));
                ingredientRepository.save(new Ingredient("COTO", "Corn Tortilla", Type.WRAP));
                ingredientRepository.save(new Ingredient("GRBF", "Ground Beef", Type.PROTEIN));
                ingredientRepository.save(new Ingredient("CARN", "Carnitas", Type.PROTEIN));
                ingredientRepository.save(new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES));
                ingredientRepository.save(new Ingredient("LETC", "Lettuce", Type.VEGGIES));
                ingredientRepository.save(new Ingredient("CHED", "Cheddar", Type.CHEESE));
                ingredientRepository.save(new Ingredient("JACK", "Monterrey Jack", Type.CHEESE));
                ingredientRepository.save(new Ingredient("SLSA", "Salsa", Type.SAUCE));
                ingredientRepository.save(new Ingredient("SRCR", "Sour Cream", Type.SAUCE));

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
            }
        };
    }
}