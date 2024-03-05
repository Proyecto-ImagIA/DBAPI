package cat.iesesteveterradas.dbapi.endpoints;

import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.PeticioDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuari;
import cat.iesesteveterradas.dbapi.persistencia.UsuariDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/respostes")
public class RespostaResource {

    @POST
    @Path("/afegir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response afegirResposta(@HeaderParam("peticio_id") Long peticioId,
            @HeaderParam("Authorization") String apiKey, String jsonPeticio) {

        Usuari usuari = UsuariDAO.getUsuariPerPeticioId(peticioId);
        
        if (usuari == null || !usuari.getApiKey().equals(apiKey)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Usuari no trobat").build();
        }

        JSONObject peticio = new JSONObject(jsonPeticio);
        String resposta = peticio.getString("resposta");

        if (resposta == null || resposta.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Falta la resposta").build();
        }
        
        PeticioDAO.actualitzarResposta(peticioId, resposta);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "OK");
        jsonResponse.put("message", "resposta afegida amb èxit");
        jsonResponse.put("resposta", resposta);
        jsonResponse.put("peticio_id", peticioId);

        return Response.ok(jsonResponse.toString()).build();

    }

}
