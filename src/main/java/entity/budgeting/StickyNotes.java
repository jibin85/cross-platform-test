package entity.budgeting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USER_STICKY_NOTES")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StickyNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer sno;

    private String userId;
    private Long noteId;
    private String noteColor;
    private Long timestamp;
    private String stickyNotesValue;
}