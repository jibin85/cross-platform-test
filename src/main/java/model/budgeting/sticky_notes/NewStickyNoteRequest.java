package model.budgeting.sticky_notes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NewStickyNoteRequest {
    private Long noteId;
    private String noteColor;
    private Long timestamp;
    private String stickyNoteValue;
}