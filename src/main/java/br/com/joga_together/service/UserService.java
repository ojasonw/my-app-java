package br.com.joga_together.service;

import br.com.joga_together.dto.user.ConfirmCodeDto;
import br.com.joga_together.dto.user.UserCreateRequestDto;
import br.com.joga_together.exception.CodeExpireException;
import br.com.joga_together.exception.CodeInvalidOrExpireException;
import br.com.joga_together.exception.UserAlreadyExistsException;
import br.com.joga_together.exception.UserByEmailNotFoundException;
import br.com.joga_together.mapper.UserMapper;
import br.com.joga_together.model.User;
import br.com.joga_together.model.enums.UserStatus;
import br.com.joga_together.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private static final Integer VERIFICATION_CODE_BOUND = 1_000_000;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public UserService(UserMapper userMapper, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void creteUser(UserCreateRequestDto request){
        userRepository.findByEmail(request.email()).ifPresentOrElse(
                existingUser -> {
                    updateExistingUserForReRegistration(existingUser, request);
                    userRepository.save(existingUser);
                    emailService.sendConfirmCodeRegistrer(existingUser.getEmail(), existingUser.getVerificationCode());
                },
                () -> {
                    User user = userMapper.toEntity(request);
                    initializeNewUser(user);
                    userRepository.save(user);
                    emailService.sendConfirmCodeRegistrer(user.getEmail(), user.getVerificationCode());

                }
        );


    }

    private String generateToken() {
        return String.format("%06d", SECURE_RANDOM.nextInt(VERIFICATION_CODE_BOUND));
    }

    @Transactional
    public void confirmRegister(ConfirmCodeDto dto) {
        User user = userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new UserByEmailNotFoundException("user this email not found")
        );
        if (valideteCodeRegister(dto.code(), user)) {
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            emailService.sendEmailRegisterConfirmed(user.getEmail(), user.getUsername());
            return;
        }
        throw new CodeInvalidOrExpireException("code invalid or expire");
    }

    private boolean valideteCodeRegister(String code, User user) {
        if (code != null) {
            if (LocalDateTime.now().isAfter(user.getCodeExpiresAt())) {
                user.setUserStatus(UserStatus.EXPIRED);
                userRepository.save(user);
                throw new CodeExpireException("Code has expired, try send again");
            }
            if (user.getVerificationCode().equals(code)) {
                return true;
            }
        }
        throw new CodeInvalidOrExpireException("Code invalid");
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("user with this id: " + id + ", not found")
        );
    }

    private void initializeNewUser(User user) {
        user.setCreationDate(LocalDateTime.now());
        applyVerificationSetup(user);
    }

    private void updateExistingUserForReRegistration(User existingUser, UserCreateRequestDto request) {
        if (existingUser.getUserStatus().equals(UserStatus.ACTIVE)) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        existingUser.setUsername(request.username());
        applyVerificationSetup(existingUser);
    }

    private void applyVerificationSetup(User user) {
        user.setVerificationCode(generateToken());
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setUserStatus(UserStatus.INACTIVE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

}
