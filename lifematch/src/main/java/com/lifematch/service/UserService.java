
package com.lifematch.service;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.lifematch.dto.LoginRequest;
import com.lifematch.dto.LoginResponse;
import com.lifematch.dto.UserResponse;
import com.lifematch.model.User;
import com.lifematch.repository.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

        @Inject
        private UserRepository repository;

        // CREATE USER
        public UserResponse createUser(User user) {

                validateUser(user, true);

                User existingUser = repository.findByUsername(user.getUsername());

                if (existingUser != null) {
                        throw new IllegalArgumentException(
                                        "Username already exists.");
                }

                // Store a hash instead of the original password.
                user.setPassword(
                                hashPassword(user.getPassword()));

                User savedUser = repository.save(user);

                return toResponse(savedUser);
        }

        // GET ALL USERS
        public List<UserResponse> getAllUsers() {

                return repository.findAll()
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        // GET USER BY ID
        public UserResponse getUserById(int id) {

                User user = repository.findById(id);

                if (user == null) {
                        return null;
                }

                return toResponse(user);
        }

        // UPDATE USER
        public UserResponse updateUser(
                        int id,
                        User updatedUser,
                        int currentUserId) {

                User existingUser = repository.findById(id);

                if (existingUser == null) {
                        return null;
                }

                // Password is optional when updating.
                validateUser(updatedUser, false);

                User userWithSameUsername = repository.findByUsername(
                                updatedUser.getUsername());

                if (userWithSameUsername != null &&
                                userWithSameUsername.getId() != id) {

                        throw new IllegalArgumentException(
                                        "Username already exists.");
                }

                // Prevent changing your own administrator role.
                if (id == currentUserId &&
                                "ADMIN".equalsIgnoreCase(existingUser.getRole()) &&
                                !"ADMIN".equalsIgnoreCase(updatedUser.getRole())) {

                        throw new IllegalArgumentException(
                                        "You cannot remove your own administrator role.");
                }

                // Prevent removing the last administrator.
                if ("ADMIN".equalsIgnoreCase(existingUser.getRole()) &&
                                !"ADMIN".equalsIgnoreCase(updatedUser.getRole()) &&
                                countAdministrators() <= 1) {

                        throw new IllegalArgumentException(
                                        "The last administrator cannot be changed to USER.");
                }

                // Preserve the existing hash if password is blank.
                if (updatedUser.getPassword() == null ||
                                updatedUser.getPassword().isBlank()) {

                        updatedUser.setPassword(
                                        existingUser.getPassword());

                } else {

                        // Hash the new password.
                        updatedUser.setPassword(
                                        hashPassword(updatedUser.getPassword()));
                }

                updatedUser.setId(id);

                User user = repository.update(id, updatedUser);

                return toResponse(user);
        }

        // DELETE USER
        public boolean deleteUser(
                        int id,
                        int currentUserId) {

                User existingUser = repository.findById(id);

                if (existingUser == null) {
                        return false;
                }

                if (id == currentUserId) {
                        throw new IllegalArgumentException(
                                        "You cannot delete your own account.");
                }

                if ("ADMIN".equalsIgnoreCase(existingUser.getRole()) &&
                                countAdministrators() <= 1) {

                        throw new IllegalArgumentException(
                                        "The last administrator cannot be deleted.");
                }

                return repository.delete(id);
        }

        // COUNT ADMINISTRATORS
        private long countAdministrators() {

                return repository.findAll()
                                .stream()
                                .filter(user -> "ADMIN".equalsIgnoreCase(user.getRole()))
                                .count();
        }

        // LOGIN
        public LoginResponse login(LoginRequest loginRequest) {

                if (loginRequest == null) {
                        throw new IllegalArgumentException(
                                        "Login data is required.");
                }

                if (loginRequest.username() == null ||
                                loginRequest.username().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Username is required.");
                }

                if (loginRequest.password() == null ||
                                loginRequest.password().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Password is required.");
                }

                User user = repository.findByUsername(
                                loginRequest.username().trim());

                if (user == null ||
                                !verifyPassword(
                                                loginRequest.password(),
                                                user.getPassword())) {

                        throw new IllegalArgumentException(
                                        "Invalid username or password.");
                }

                return new LoginResponse(
                                user.getId(),
                                user.getUsername(),
                                user.getRole());
        }

        // HASH PASSWORD
        private String hashPassword(String password) {

                return BCrypt.hashpw(
                                password,
                                BCrypt.gensalt(12));
        }

        // VERIFY PASSWORD
        private boolean verifyPassword(
                        String password,
                        String storedHash) {

                if (storedHash == null ||
                                !storedHash.startsWith("$2")) {

                        return false;
                }

                try {

                        return BCrypt.checkpw(
                                        password,
                                        storedHash);

                } catch (IllegalArgumentException exception) {

                        return false;
                }
        }

        // USER VALIDATIONS
        private void validateUser(
                        User user,
                        boolean passwordRequired) {

                if (user == null) {
                        throw new IllegalArgumentException(
                                        "User is required.");
                }

                if (user.getUsername() == null ||
                                user.getUsername().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Username is required.");
                }

                if (passwordRequired &&
                                (user.getPassword() == null ||
                                                user.getPassword().isBlank())) {

                        throw new IllegalArgumentException(
                                        "Password is required.");
                }

                if (user.getRole() == null ||
                                user.getRole().isBlank()) {

                        throw new IllegalArgumentException(
                                        "Role is required.");
                }

                if (!isValidRole(user.getRole())) {
                        throw new IllegalArgumentException(
                                        "Role must be ADMIN or USER.");
                }
        }

        // VALID ROLES
        private boolean isValidRole(String role) {

                return role.equalsIgnoreCase("ADMIN") ||
                                role.equalsIgnoreCase("USER");
        }

        // CONVERT USER TO USER RESPONSE
        private UserResponse toResponse(User user) {

                return new UserResponse(
                                user.getId(),
                                user.getUsername(),
                                user.getRole());
        }
}
