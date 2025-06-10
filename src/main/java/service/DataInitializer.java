package service;

import entity.user_management.Dashboard;
import entity.user_management.User;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.user_management.DashboardService;
import service.user_management.UserService;

import java.time.LocalDateTime;

@ApplicationScoped
public class DataInitializer {

    @Inject
    UserService userService;

    @Inject
    DashboardService dashboardService;

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    void onStart(@Observes StartupEvent ev) {
        logger.info("Initializing hardcoded data...");

        // Create user directly here
        User existingUser = userService.findByEmail("jibin.2.thomas1999@gmail.com");
        if (existingUser == null) {
            // Hash password
            String hashedPassword = BCrypt.hashpw("87654321", BCrypt.gensalt());

            // Create user 1
            User user1 = new User();
            user1.setEmail("jibin.2.thomas1999@gmail.com");
            user1.setPassword(hashedPassword);
            user1.setFirstName("Jibin");
            user1.setLastName("Thomas");
            user1.setCreationDate(LocalDateTime.now());
            user1.setActive(true);
            userService.persist(user1);

            // Create dashboard 1
            Dashboard dashboard1 = new Dashboard();
            dashboard1.setId(user1.getId());
            dashboard1.setEmail(user1.getEmail());
            dashboard1.setFirstName(user1.getFirstName());
            dashboard1.setLastName(user1.getLastName());
            dashboard1.setCreationDate(user1.getCreationDate());
            dashboardService.persist(dashboard1);

            // Create user 2
            User user2 = new User();
            user2.setEmail("jithinthomasjulie@gmail.com");
            user2.setPassword(hashedPassword);
            user2.setFirstName("Jithin");
            user2.setLastName("Thomas");
            user2.setCreationDate(LocalDateTime.now());
            user2.setActive(true);
            userService.persist(user2);

            // Create dashboard 2
            Dashboard dashboard2 = new Dashboard();
            dashboard2.setId(user2.getId());
            dashboard2.setEmail(user2.getEmail());
            dashboard2.setFirstName(user2.getFirstName());
            dashboard2.setLastName(user2.getLastName());
            dashboard2.setCreationDate(user2.getCreationDate());
            dashboardService.persist(dashboard2);

            // Create user 3
            User user3 = new User();
            user3.setEmail("jibin.thomas@imss.co.in");
            user3.setPassword(hashedPassword);
            user3.setFirstName("Jibin");
            user3.setLastName("NT");
            user3.setCreationDate(LocalDateTime.now());
            user3.setActive(true);
            userService.persist(user3);

            // Create dashboard 3
            Dashboard dashboard3 = new Dashboard();
            dashboard3.setId(user3.getId());
            dashboard3.setEmail(user3.getEmail());
            dashboard3.setFirstName(user3.getFirstName());
            dashboard3.setLastName(user3.getLastName());
            dashboard3.setCreationDate(user3.getCreationDate());
            dashboardService.persist(dashboard3);

            // Create user 4
            User user4 = new User();
            user4.setEmail("vargese.2.babu1969@gmail.com");
            user4.setPassword(hashedPassword);
            user4.setFirstName("Vargese");
            user4.setLastName("Babu");
            user4.setCreationDate(LocalDateTime.now());
            user4.setActive(true);
            userService.persist(user4);

            // Create dashboard 4
            Dashboard dashboard4 = new Dashboard();
            dashboard4.setId(user4.getId());
            dashboard4.setEmail(user4.getEmail());
            dashboard4.setFirstName(user4.getFirstName());
            dashboard4.setLastName(user4.getLastName());
            dashboard4.setCreationDate(user4.getCreationDate());
            dashboardService.persist(dashboard4);

            // Create user 5
            User user5 = new User();
            user5.setEmail("abc@xyz.com");
            user5.setPassword(hashedPassword);
            user5.setFirstName("abc");
            user5.setLastName("pqr");
            user5.setCreationDate(LocalDateTime.now());
            user5.setActive(true);
            userService.persist(user5);

            // Create dashboard 5
            Dashboard dashboard5 = new Dashboard();
            dashboard5.setId(user5.getId());
            dashboard5.setEmail(user5.getEmail());
            dashboard5.setFirstName(user5.getFirstName());
            dashboard5.setLastName(user5.getLastName());
            dashboard5.setCreationDate(user5.getCreationDate());
            dashboardService.persist(dashboard5);

            logger.info("Hardcoded data created successfully");
        }
    }
}