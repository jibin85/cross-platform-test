package processor.budgeting;

import entity.budgeting.StickyNotes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import model.budgeting.sticky_notes.StickyNoteItem;
import model.budgeting.sticky_notes.StickyNoteRetrievalResponse;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.budgeting.StickyNotesService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
@Named("stickyNotesRetrievalProcessor")
@RequiredArgsConstructor
public class StickyNotesRetrievalProcessor implements Processor {

    private final StickyNotesService stickyNotesService;

    private static final Logger logger = LoggerFactory.getLogger(StickyNotesRetrievalProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Class: StickyNotesRetrievalProcessor, Method: Process-- STARTED");

        String userId = "5";

        try {
            // Retrieve sticky notes from database
            List<StickyNotes> userStickyNotes = stickyNotesService.findStickyNotesByUserId(userId);

            StickyNoteRetrievalResponse response = new StickyNoteRetrievalResponse();

            if (Objects.nonNull(userStickyNotes) && !userStickyNotes.isEmpty()) {
                logger.info("Found {} sticky notes for user: {}", userStickyNotes.size(), userId);

                // Convert entity list to response items
                List<StickyNoteItem> noteItems = userStickyNotes.stream()
                        .map(this::convertToStickyNoteItem)
                        .collect(Collectors.toList());

                response.setStickyNotes(noteItems);
                response.setTotalCount(noteItems.size());
                response.setMessage("Sticky notes retrieved successfully");

                logger.info("Successfully converted {} sticky notes to response format", noteItems.size());
            } else {
                logger.info("No sticky notes found for user: {}", userId);
                response.setStickyNotes(List.of()); // Empty list
                response.setTotalCount(0);
                response.setMessage("No sticky notes found");
            }

            // Set response body
            exchange.getIn().setBody(response);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);

        } catch (Exception e) {
            logger.error("Error retrieving sticky notes for user: {}", userId, e);

            // Create error response
            StickyNoteRetrievalResponse errorResponse = new StickyNoteRetrievalResponse();
            errorResponse.setStickyNotes(List.of());
            errorResponse.setTotalCount(0);
            errorResponse.setMessage("Error retrieving sticky notes: " + e.getMessage());

            exchange.getIn().setBody(errorResponse);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        }

        logger.info("Class: StickyNotesRetrievalProcessor, Method: Process-- EXECUTED");
    }

    private StickyNoteItem convertToStickyNoteItem(StickyNotes stickyNote) {
        StickyNoteItem item = new StickyNoteItem();
        item.setNoteId(stickyNote.getNoteId());
        item.setNoteColor(stickyNote.getNoteColor());
        item.setStickyNotesValue(stickyNote.getStickyNotesValue());
        item.setTimestamp(stickyNote.getTimestamp());
        return item;
    }
}