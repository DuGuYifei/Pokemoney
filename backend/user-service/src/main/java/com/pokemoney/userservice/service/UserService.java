package com.pokemoney.userservice.service;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestLoginDto;
import com.pokemoney.userservice.dto.VerifyLoginDto;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.entity.User;
import com.pokemoney.userservice.repository.UserRepository;
import com.pokemoney.userservice.feignclient.LeafClient;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * User business logic service.
 */
@Service
@Validated
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
        return save(user);
    }

    /**
     * Find a user based on username or email.
     *
     * @param requestLoginDto The {@link RequestLoginDto} send by user.
     * @return The {@link VerifyLoginDto} which will be verified.
     */
    public VerifyLoginDto generateVerifyLoginDto (RequestLoginDto requestLoginDto){
        VerifyLoginDto verifyLoginDto = VerifyLoginDto.builder()
                .password(requestLoginDto.getPassword())
                .build();
        if(requestLoginDto.getUsernameOrEmail().contains("@")) {
            verifyLoginDto.setEmail(requestLoginDto.getUsernameOrEmail());
        } else {
            verifyLoginDto.setUsername(requestLoginDto.getUsernameOrEmail());
        }
        return verifyLoginDto;
    }

    /**
     * Verify login.
     *
     * @param verifyLoginDto The {@link VerifyLoginDto} to be verified.
     * @return User if login successfully, null otherwise.
     */
    public User verifyLogin(@Valid VerifyLoginDto verifyLoginDto) throws GenericForbiddenError {
        User user;
        if (verifyLoginDto.getEmail() != null) {
            user = userRepository.findByEmail(verifyLoginDto.getEmail());
        } else {
            user = userRepository.findByUsername(verifyLoginDto.getUsername());
        }
        if (user == null) {
            throw new GenericForbiddenError("User not exist.");
        }
        if (user.verifyPassword(verifyLoginDto.getPassword())) {
            return user;
        } else {
            throw new GenericForbiddenError("Invalid account or password.");
        }
    }
}
