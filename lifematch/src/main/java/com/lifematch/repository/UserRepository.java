package com.lifematch.repository;

import java.util.List;

import com.lifematch.model.User;

import jakarta.enterprise.context.ApplicationScoped;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext(unitName = "LifeMatchPU")
    private EntityManager entityManager;


    // SAVE USER
    @Transactional
    public User save(User user) {

        entityManager.persist(user);

        return user;
    }


    // FIND ALL USERS
    public List<User> findAll() {

        return entityManager.createQuery(
                "SELECT u FROM User u ORDER BY u.id",
                User.class
        ).getResultList();
    }


    // FIND USER BY ID
    public User findById(int id) {

        return entityManager.find(User.class, id);
    }


    // FIND USER BY USERNAME
    public User findByUsername(String username) {

        if (username == null) {
            return null;
        }

        List<User> users = entityManager.createQuery(
                "SELECT u FROM User u " +
                "WHERE LOWER(u.username) = LOWER(:username)",
                User.class
        )
        .setParameter("username", username)
        .setMaxResults(1)
        .getResultList();

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }


    // UPDATE USER
    @Transactional
    public User update(int id, User updatedUser) {

        User existingUser = findById(id);

        if (existingUser == null) {
            return null;
        }

        existingUser.setUsername(
                updatedUser.getUsername()
        );

        existingUser.setPassword(
                updatedUser.getPassword()
        );

        existingUser.setRole(
                updatedUser.getRole()
        );

        return existingUser;
    }


    // DELETE USER
    @Transactional
    public boolean delete(int id) {

        User user = findById(id);

        if (user == null) {
            return false;
        }

        entityManager.remove(user);

        return true;
    }
}
