package springinaction.tacos.security;


import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.Data;
import springinaction.tacos.domain.entity.User;

import java.util.stream.IntStream;


@Data
@Builder
public class RegistrationForm {

	private String username;
	private String password;
	private String fullname;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;

	public User toUser(PasswordEncoder passwordEncoder) {
		return new User(username,passwordEncoder.encode(password),fullname, street, city, state, zip, phone);
	}
}