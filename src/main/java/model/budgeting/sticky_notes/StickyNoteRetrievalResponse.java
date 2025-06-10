package model.budgeting.sticky_notes;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StickyNoteRetrievalResponse {
    private List<StickyNoteItem> stickyNotes;
    private int totalCount;
    private String message;
}
