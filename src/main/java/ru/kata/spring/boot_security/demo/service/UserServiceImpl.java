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
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getById(id);
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
    public User updateUser(Long id, User updateUser) { //Save user with this id
        updateUser.setId(id);
        User existUser = userRepository.getById(id);
        System.out.println(updateUser.getPassword());
        if (!updateUser.getUsername().equals(existUser.getUsername()) && userRepository.findByUsername(updateUser.getUsername()) != null) {
            throw new EntityNotFoundException("username is exist");
        }
        if (updateUser.getRoles() == null) {
            updateUser.setRoles(existUser.getRoles());
        }
        if (updateUser.getPassword() == null) {
            updateUser.setPassword(existUser.getPassword());
            return userRepository.save(updateUser);
        }
        if (!userRepository.existsById(id) || !updateUser.getPassword().equals(existUser.getPassword())) {
            updateUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
        return userRepository.save(updateUser);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
