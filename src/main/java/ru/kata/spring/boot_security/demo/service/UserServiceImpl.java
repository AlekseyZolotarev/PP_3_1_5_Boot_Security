package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users != null) {
            return userRepository.findAll();
        } else {
            throw new EntityNotFoundException("getAllUsers no User");
        }
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.getById(id);
        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException("getUserById no User");
        }
    }

    @Override
    @Transactional
    public void addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } else throw new EntityNotFoundException("username is exist");
    }

    @Override
    @Transactional
    public void addUserWOCheckName(User user) {
        User oldUser = userRepository.findByUsername(user.getUsername());
        if (oldUser != null) {
            user.setId(oldUser.getId());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        if (!(user.getPassword().equals(userRepository.getById(user.getId()).getPassword()))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    @Override
    public User findByName(String name) {
        User user = userRepository.findByUsername(name);
        if (user != null) {
            return userRepository.findByUsername(name);
        } else {
            throw new EntityNotFoundException("findByName no User");
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        User user = userRepository.getById(id);
        if (user != null) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("deleteUser no User");
        }
    }
}
