package br.com.joga_together.service;

import br.com.joga_together.dto.user.ConfirmCodeDto;
import br.com.joga_together.dto.user.UserCreateRequestDto;
import br.com.joga_together.exception.CodeInvalidOrExpireException;
import br.com.joga_together.exception.UserByEmailNotFoundException;
import br.com.joga_together.mapper.UserMapper;
import br.com.joga_together.model.User;
import br.com.joga_together.model.enums.UserStatus;
import br.com.joga_together.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void creteUser(UserCreateRequestDto request) {
        User user = userMapper.toEntity(request);
        user.setUserStatus(UserStatus.INACTIVE);
        user.setCreationDate(LocalDateTime.now());
        user.setVerificationCode(generateToken());
        // encode password before saving
        if(user.getPassword() != null){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
        emailService.sendConfirmCodeRegistrer(user.getEmail(), user.getVerificationCode());
        user.setCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
    }

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        int n = 100_000 + random.nextInt(900_000);
        return String.valueOf(n);
    }

    @Transactional
    public void confirmRegister(ConfirmCodeDto dto){
        User user = userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new UserByEmailNotFoundException("user this email not found")
        );
        if(valideteCodeRegister(dto.code(), user)){
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            emailService.sendEmailRegisterConfirmed(user.getEmail(), user.getUsername());
            return;
        }
        throw new CodeInvalidOrExpireException("code invalid or expire");
    }

    private boolean valideteCodeRegister(String code, User user){
        if(code.equals(user.getVerificationCode()) && user.getCodeExpiresAt().isAfter(LocalDateTime.now())){
            return true;
        }
        return false;
    }

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("user with this id: "+id + ", not found")
        );
    }

}
