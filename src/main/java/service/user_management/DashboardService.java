package service.user_management;

import entity.user_management.Dashboard;
import entity.user_management.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.GenericRepository;

import java.util.Objects;
import java.util.function.Consumer;

@ApplicationScoped
@Transactional
@RequiredArgsConstructor
public class DashboardService implements GenericRepository<Dashboard> {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    // Dashboard Retrieval Method
    public Dashboard getDashboardData(String userId){
        logger.info("Class: DashboardService, Method: returnDashboardData -- STARTED");
        User userInstance = userService.findById(userId);
        Dashboard dashboardInstance = null;
        if(Objects.nonNull(userInstance)) {
            dashboardInstance = findById(userInstance.getId());
            if (Objects.isNull(dashboardInstance)) {
                // Create a new Dashboard instance for user upon first login
                dashboardInstance = new Dashboard();
                dashboardInstance.setId(userInstance.getId());
                dashboardInstance.setEmail(userInstance.getEmail());
                dashboardInstance.setFirstName(userInstance.getFirstName());
                dashboardInstance.setLastName(userInstance.getLastName());
                dashboardInstance.setCreationDate(userInstance.getCreationDate());
                persist(dashboardInstance);
            }
        }
        logger.info("Class: DashboardService, Method: returnDashboardData -- EXECUTED");
        return dashboardInstance;
    }

    // Profile Update Method
    /*public void updateUserProfile(String userId, ProfileUpdateRequest jsonValue) {
        logger.info("Class: DashboardService, Method: updateUserProfile -- STARTED");
        Dashboard dashboardInstance = findById(userId);
        if (Objects.isNull(dashboardInstance)) {
            logger.error("Dashboard instance not found for userId: {}" ,userId);
            return;
        }
        updateIfChanged(dashboardInstance.getFirstName(), jsonValue.getFirstName(), dashboardInstance::setFirstName);
        updateIfChanged(dashboardInstance.getLastName(), jsonValue.getLastName(), dashboardInstance::setLastName);
        updateIfChanged(dashboardInstance.getMobNo(), jsonValue.getPhone(), dashboardInstance::setMobNo);
        updateIfChanged(dashboardInstance.getBio(), jsonValue.getBio(), dashboardInstance::setBio);
        persist(dashboardInstance);
        logger.info("Class: DashboardService, Method: updateUserProfile -- EXECUTED");
    }*/

    private <T> void updateIfChanged(T existingValue, T newValue, Consumer<T> setter) {
        if (!Objects.equals(existingValue, newValue)) {
            setter.accept(newValue);
        }
    }

    public void updateLatestEmail(String userId, String newEmail){
        logger.info("Class: DashboardService, Method: updateLatestEmail -- STARTED");
        if(Objects.nonNull(findById(userId))) {
            update("email = ?1 where id = ?2", newEmail, userId);
        }
        logger.info("Class: DashboardService, Method: updateLatestEmail -- EXECUTED");
    }

    public void deleteUser(String userId){
        logger.info("Class: DashboardService, Method: deleteUser -- STARTED");
        Dashboard userInDashboard = findById(userId);
        if(Objects.nonNull(userInDashboard)) {
            delete(userInDashboard);
            logger.info("User from Dashboard with ID: {} has been deleted.", userId);
        }
        logger.info("Class: DashboardService, Method: deleteUser -- EXECUTED");
    }
}