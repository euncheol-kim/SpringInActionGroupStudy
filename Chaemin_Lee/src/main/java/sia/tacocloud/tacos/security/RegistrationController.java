package sia.tacocloud.tacos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sia.tacocloud.tacos.data.UserRepository;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String registerForm(){
        return "registration";
    }

    @PostMapping
    public String processingRegistration(RegistrationForm form){
        userRepository.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
