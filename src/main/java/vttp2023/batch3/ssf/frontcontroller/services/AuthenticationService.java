package vttp2023.batch3.ssf.frontcontroller.services;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vttp2023.batch3.ssf.frontcontroller.respositories.AuthenticationRepository;

import java.util.Optional;

@Service
public class AuthenticationService {
	@Autowired
	private AuthenticationRepository repository;

	@Value("${auth.service.url}")
	private String authServiceUrl;

	public void saveFailedAuthAttempt(String username) {
		repository.saveFailedAuthAttempt(username);
	}

	public Optional<Integer> retrieveFailedAuthAttempts(String username) {
		Integer result = repository.retrieveFailedAuthAttempts(username);
		if (result != null) {
			return Optional.of(result);
		}
		return Optional.empty();
	}

	// TODO: Task 2
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write the authentication method in here

	public void authenticate(String username, String password) throws Exception {
		JsonObject obj = Json.createObjectBuilder()
				.add("username", username)
				.add("password", password)
				.build();

		RequestEntity<String> request = RequestEntity
				.post(authServiceUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Accept", MediaType.APPLICATION_JSON_VALUE)
				.body(obj.toString(), String.class);

		RestTemplate template = new RestTemplate();

		template.exchange(request, String.class);
	}

	// TODO: Task 3
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to disable a user account for 30 mins
	public void disableUser(String username) {
		// redundant method here as attempts are already added to the redis database with expiration
	}

	// TODO: Task 5
	// DO NOT CHANGE THE METHOD'S SIGNATURE
	// Write an implementation to check if a given user's login has been disabled
	public boolean isLocked(String username) {
		try {
			Optional<Integer> attempts = this.retrieveFailedAuthAttempts(username);
			return attempts.isPresent() && attempts.get() >= 3 - 1;
		} catch (Exception e) {
			return false;
		}
	}
}
