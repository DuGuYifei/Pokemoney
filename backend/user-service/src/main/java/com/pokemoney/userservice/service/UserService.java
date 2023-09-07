package com.pokemoney.userservice.service;

import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.entity.User;
import com.pokemoney.userservice.repository.UserRepository;
import com.pokemoney.userservice.feignclient.LeafClient;
import org.springframework.stereotype.Service;

/**
 * User business logic service.
 */
@Service
public class UserService {

    /**
     * Repository of t_users table.
     */
    private final UserRepository userRepository;

    /**
     * Leaf feign client.
     */
    private final LeafClient leafClient;

    /**
     * Constructor.
     *
     * @param userRepository Repository of t_users table.
     */
    public UserService(UserRepository userRepository, LeafClient leafClient) {
        this.userRepository = userRepository;
        this.leafClient = leafClient;
    }

    /**
     * Save user.
     *
     * @param user User.
     * @return User.
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Check if user exists by username.
     *
     * @param username Username.
     * @return True if user exists, false otherwise.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if user exists by email.
     *
     * @param email Email.
     * @return True if user exists, false otherwise.
     */
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Get segment id from leaf-service and add to RegisterUserDto
     *
     * @param requestRegisterUserDto RegisterUserDto contains user information from user.
     */
    public void setSegmentId(RequestRegisterUserDto requestRegisterUserDto) {
        String segmentId = leafClient.getSnowflakeId(Constants.USER_IN_LEAF_KEY);
        requestRegisterUserDto.setId(Long.parseLong(segmentId));
    }

    /**
     * Create a user from RegisterUserDto
     *
     * @param requestRegisterUserDto RegisterUserDto contains user information from user.
     * @return User.
     */
    public User createUser(RequestRegisterUserDto requestRegisterUserDto) {
        User user = User.fromRegisterUserDto(requestRegisterUserDto);
        return userRepository.save(user);
    }
}
