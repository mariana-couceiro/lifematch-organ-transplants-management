package com.lifematch.config;

import com.lifematch.model.User;
import com.lifematch.repository.UserRepository;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import org.mindrot.jbcrypt.BCrypt;

@Singleton
@Startup
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AdminInitializer {

    @Inject
    private UserRepository repository;

    @PostConstruct
    public void initializeAdmin() {

        // Do not create another administrator if one exists.
        if (repository.findByUsername("admin") != null) {
            return;
        }

        String adminPassword =
                System.getenv("LIFEMATCH_ADMIN_PASSWORD");

        if (adminPassword == null || adminPassword.isBlank()) {
            throw new IllegalStateException(
                    "Environment variable LIFEMATCH_ADMIN_PASSWORD must be configured."
            );
        }

        User admin = new User(
                "admin",
                BCrypt.hashpw(adminPassword, BCrypt.gensalt(12)),
                "ADMIN"
        );

        repository.save(admin);
    }
}
