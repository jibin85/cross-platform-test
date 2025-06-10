package processor.budgeting;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import model.budgeting.sticky_notes.StickyNoteDeleteRequest;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.budgeting.StickyNotesService;

import java.util.Objects;

@ApplicationScoped
@Named("stickyNotesDeleteProcessor")
@RequiredArgsConstructor
public class StickyNotesDeleteProcessor implements Processor {

    private final StickyNotesService stickyNotesService;

    private static final Logger logger = LoggerFactory.getLogger(StickyNotesDeleteProcessor.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("Class: StickyNotesDeleteProcessor, Method: Process-- STARTED");
        StickyNoteDeleteRequest stickyNoteDeleteRequest = exchange.getIn().getBody(StickyNoteDeleteRequest.class);
        if(Objects.nonNull(stickyNoteDeleteRequest)) {
            stickyNotesService.deleteStickyNoteContent(
                    stickyNoteDeleteRequest
            );
        }
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
        logger.info("Class: StickyNotesDeleteProcessor, Method: Process-- EXECUTED");
    }
}