package ali.barkbot.service;

import ali.barkbot.entity.UserEntity;
import ali.barkbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void save(UserEntity user) {
        userRepository.save(user);
    }
}
