package vttp2023.batch3.ssf.frontcontroller.respositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class AuthenticationRepository {
    @Autowired
    private RedisTemplate<String, Object> template;

	// TODO Task 5
	// Use this class to implement CRUD operations on Redis
    public void saveFailedAuthAttempt(String username) {
        if (template.opsForValue().getOperations().hasKey(username)) {
            int attempts = Integer.parseInt((String) template.opsForValue().get(username));
            template.opsForValue().set(username, String.valueOf(attempts + 1), Duration.ofMinutes(30));
        } else {
            template.opsForValue().set(username, String.valueOf(1), Duration.ofMinutes(30));
        }
    }

    public Integer retrieveFailedAuthAttempts(String username) {
        return Integer.parseInt((String) template.opsForValue().get(username));
    }
}
