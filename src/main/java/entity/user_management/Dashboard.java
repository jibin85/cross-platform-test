package entity.user_management;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_DASHBOARD")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Dashboard {
    @Id
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime creationDate;
    private String mobNo;
    private String bio;
}