package route;

import jakarta.enterprise.context.ApplicationScoped;
import model.AdditionRequest;
import model.AdditionResponse;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

@ApplicationScoped
public class AdditionContextRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/api")
                .post("/add")
                .to("direct:addition");

        from("direct:addition")
                .routeId("addition-route")
                .unmarshal().json(JsonLibrary.Jackson, AdditionRequest.class)
                .process("additionProcessor")
                .marshal().json(JsonLibrary.Jackson, AdditionResponse.class)
                .log(LoggingLevel.INFO,"Successfully processed addition-route");
    }
}
