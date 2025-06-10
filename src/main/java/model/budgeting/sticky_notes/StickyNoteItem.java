package model.budgeting.sticky_notes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StickyNoteItem {
    private Long noteId;
    private String noteColor;
    private String stickyNotesValue;
    private Long timestamp;
}
