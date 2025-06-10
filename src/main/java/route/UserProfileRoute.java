package route;

import jakarta.enterprise.context.ApplicationScoped;
import model.budgeting.sticky_notes.NewStickyNoteRequest;
import model.budgeting.sticky_notes.StickyNoteDeleteRequest;
import model.budgeting.sticky_notes.StickyNoteRetrievalResponse;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

@ApplicationScoped
public class UserProfileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // Configure error handling
        onException(Exception.class)
            .routeId("General-Error-Route")
            .handled(true)
            .log(LoggingLevel.ERROR, "General error: ${exception.class} - ${exception.message}, Value Caught : ${body}")
            .setBody(constant("Error processing request"))
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500));

        restConfiguration()
            .bindingMode(RestBindingMode.json);

        //Rest Endpoints for User Profile
        rest("api/user")
            .post("/sticky-notes/insert")
                .type(NewStickyNoteRequest.class)
                    .to("direct:insertStickyNotes")
            .get("/sticky-notes/retrieve")
                .outType(StickyNoteRetrievalResponse.class)
                    .to("direct:retrieveStickyNotes")
            .delete("/sticky-notes/delete")
                .type(StickyNoteDeleteRequest.class)
                    .to("direct:deleteStickyNotes");

        // User Sticky Notes Insert Route
        from("direct:insertStickyNotes")
            .routeId("User-Sticky-Notes-Insert-Route")
            .process("stickyNotesInsertProcessor")
            .log(LoggingLevel.INFO, "Sticky Note value saved successfully in server.");

        // User Sticky Notes Retrieval Route
        from("direct:retrieveStickyNotes")
            .routeId("User-Sticky-Notes-Retrieval-Route")
            .process("stickyNotesRetrievalProcessor")
            .log(LoggingLevel.INFO, "Sticky Notes values retrieved successfully from server.");

        // User Sticky Notes Delete Route
        from("direct:deleteStickyNotes")
            .routeId("User-Sticky-Notes-Delete-Route")
            .process("stickyNotesDeleteProcessor")
            .log(LoggingLevel.INFO, "Sticky Note value deleted successfully from server.");
    }
}