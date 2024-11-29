package resource;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import model.AdditionRequest;
import model.AdditionResponse;

@Path("/api")
public class AdditionResource {
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AdditionResponse add(AdditionRequest request) {
        AdditionResponse response = new AdditionResponse();
        response.setSum(request.getNum1() + request.getNum2());
        return response;
    }
}