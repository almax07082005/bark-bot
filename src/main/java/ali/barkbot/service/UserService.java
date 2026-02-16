package ali.barkbot.service;

import ali.barkbot.entity.UserEntity;
import ali.barkbot.mapper.UserMapper;
import ali.barkbot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void save(UserEntity user) {
        userRepository.findById(user.getPid())
                .ifPresentOrElse(
                        existingUser -> {
                            userMapper.updateEntity(existingUser, user);
                            userRepository.save(existingUser);
                        },
                        () -> userRepository.save(user)
                );
    }
}
