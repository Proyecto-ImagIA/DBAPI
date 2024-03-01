package cat.iesesteveterradas.dbapi.endpoints;

import org.json.JSONObject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;

@Path("/")
public class IniciResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String inici() {
        return "Pàgina inicial de l'API (missatge en text pla)";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("api/user/login")
    public ResponseBuilder login() {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "OK");
        jsonResponse.put("message", "Pàgina de login (missatge en JSON)");
        return Response.status(200);
    }
}
