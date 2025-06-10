package processor.budgeting;

import entity.budgeting.StickyNotes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import model.budgeting.sticky_notes.NewStickyNoteRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.budgeting.StickyNotesService;

import java.util.Objects;

@ApplicationScoped
@Named("stickyNotesInsertProcessor")
@RequiredArgsConstructor
public class StickyNotesInsertProcessor implements Processor {

    private final StickyNotesService stickyNotesService;

    private static final Logger logger = LoggerFactory.getLogger(StickyNotesInsertProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Class: StickyNotesInsertProcessor, Method: Process-- STARTED");
        String userId = "5";
        NewStickyNoteRequest newStickyNoteRequest = exchange.getIn().getBody(NewStickyNoteRequest.class);
        if(Objects.nonNull(newStickyNoteRequest)) {
            StickyNotes stickyNote = stickyNotesService.findStickyNoteById(newStickyNoteRequest.getNoteId());
            if(Objects.nonNull(stickyNote)) {
                // Note exists - check if content is different
                if(!newStickyNoteRequest.getStickyNoteValue().equals(stickyNote.getStickyNotesValue())) {
                    logger.info("Note exists but content changed, updating sticky note");
                    stickyNotesService.updateStickyNoteContent(newStickyNoteRequest);
                } else {
                    logger.info("Note exists with same content, no action needed");
                    // Do nothing - content is the same
                }
            } else {
                // Note doesn't exist - create new one
                logger.info("Note doesn't exist, creating new sticky note");
                stickyNotesService.saveStickyNoteContent(userId, newStickyNoteRequest);
            }
        }
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        logger.info("Class: StickyNotesInsertProcessor, Method: Process-- EXECUTED");
    }
}