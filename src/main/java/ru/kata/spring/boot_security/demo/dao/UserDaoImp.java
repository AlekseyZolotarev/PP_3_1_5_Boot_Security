package ru.kata.spring.boot_security.demo.dao;


import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers(String sql) {
        return entityManager.createQuery(sql, User.class).getResultList();
    }

    @Override
    public User getUser(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            return user;
        } else {
            throw new EntityNotFoundException("getUser no User");
        }
    }

    @Override
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public void editUser(int id, User user) {
        if (entityManager.find(User.class, id) != null) {
            User user1 = getUser(id);
            user1.setName(user.getName());
        } else {
            throw new EntityNotFoundException("editUser no User");
        }
    }

    @Override
    public void deleteUser(int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        } else {
            throw new EntityNotFoundException("deleteUser no User");
        }
    }
}
