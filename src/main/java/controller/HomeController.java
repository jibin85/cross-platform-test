package controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.io.InputStream;

@Path("/")
public class HomeController {
    @GET
    @Produces(MediaType.TEXT_HTML)
    public InputStream index() {
        return getClass().getResourceAsStream("/META-INF/resources/budgeting-home.html");
    }
}