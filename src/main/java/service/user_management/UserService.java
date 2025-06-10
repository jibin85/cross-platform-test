package service.user_management;

import entity.user_management.User;
import exceptions.UserAlreadyExistException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GenericRepository;

import java.time.LocalDateTime;
import java.util.Objects;


@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class UserService implements GenericRepository<User> {

    private final DashboardService dashboardService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    // New User Registration Process
    public User createUser(String email, String password, String firstName, String lastName) {
        logger.info("Class: UserService, Method: createUser -- STARTED");
        // Check if user already exists
        if (Objects.nonNull(findByEmail(email))) {
            throw new UserAlreadyExistException("User with this email already exists");
        }

        // Hash the password before storing
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Create and persist the user
        User user = new User();
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setCreationDate(LocalDateTime.now());
        user.setActive(true);
        persist(user);

        logger.info("Class: UserService, Method: createUser -- EXECUTED");
        return user;
    }

    // User Login Process
    public User authenticate(String email, String password) {
        logger.info("Class: UserService, Method: authenticate -- STARTED");
        User user = findByEmail(email);
        if (Objects.nonNull(user) && BCrypt.checkpw(password, user.getPassword())) {
            // Update last login timestamp
            updateLastLogin(user);
            logger.info("Class: UserService, Method: authenticate -- EXECUTED with success");
            return user;
        }
        logger.info("Class: UserService, Method: authenticate -- EXECUTED with failure");
        return null;
    }

    public boolean validateCurrentPassword(String userId, String currentPassword){
        logger.info("Class: UserService, Method: validateCurrentPassword -- STARTED");
        boolean isCurrentPasswordValid = Boolean.FALSE;
        User user = findById(userId);
        if (BCrypt.checkpw(currentPassword, user.getPassword())) {
            isCurrentPasswordValid = Boolean.TRUE;
        }
        logger.info("Class: UserService, Method: validateCurrentPassword -- EXECUTED");
        return isCurrentPasswordValid;
    }

    public void updateLatestPassword(String userId, String newPassword){
        logger.info("Class: UserService, Method: updateLatestPassword -- STARTED");
        User user = findById(userId);
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        persist(user);
        logger.info("Class: UserService, Method: updateLatestPassword -- EXECUTED");
    }

    public void updateLatestEmail(String userId, String newEmail){
        logger.info("Class: UserService, Method: updateLatestEmail -- STARTED");
        if(Objects.nonNull(findById(userId))) {
            update("email = ?1 where id = ?2", newEmail, userId);
        }
        dashboardService.updateLatestEmail(userId, newEmail);
        logger.info("Class: UserService, Method: updateLatestEmail -- EXECUTED");
    }

    public void deleteUser(String userId){
        logger.info("Class: UserService, Method: deleteUser -- STARTED");
        User user = findById(userId);
        if(Objects.nonNull(user)) {
            delete(user);
            dashboardService.deleteUser(userId);
            logger.info("User with ID: {} has been deleted.", userId);
        }
        logger.info("Class: UserService, Method: deleteUser -- EXECUTED");
    }

    @Override
    public User findById(String userId){
        return find("id", userId).firstResult();
    }

    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }

    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        persist(user);
    }

    /**
     * Checks if a user with the given email exists in the system
     * @param email The email to check
     * @return true if a user with the email exists, false otherwise
     */
    public boolean emailExists(String email) {
        logger.info("Class: UserService, Method: emailExists -- STARTED");
        User user = findByEmail(email);
        boolean exists = Objects.nonNull(user.getEmail());
        logger.info("Class: UserService, Method: emailExists -- EXECUTED, Result: {}", exists);
        return exists;
    }
}