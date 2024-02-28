package cat.iesesteveterradas.dbapi.endpoints;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.GenericDAO;
import cat.iesesteveterradas.dbapi.persistencia.Peticio;
import cat.iesesteveterradas.dbapi.persistencia.PeticioDAO;
import cat.iesesteveterradas.dbapi.persistencia.Usuari;
import cat.iesesteveterradas.dbapi.persistencia.UsuariDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/peticions")
public class PeticioResource {
    @GET
    @Path("/llistar_peticions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response llistarPeticions() {
        try {
            // Utilitza el mètode llistarPeticions de PeticioDAO per obtenir totes les
            // peticions
            List<?> peticions = (List<?>) GenericDAO.listCollection(Peticio.class);
            JSONArray jsonPeticions = new JSONArray();

            for (Object obj : peticions) {
                Peticio peticio = (Peticio) obj;
                JSONObject jsonPeticio = new JSONObject();
                jsonPeticio.put("id", peticio.getPeticioId());
                jsonPeticio.put("prompt", peticio.getPrompt());
                jsonPeticio.put("data", peticio.getData());
                jsonPeticio.put("model", peticio.getModel());
                jsonPeticio.put("imatge", peticio.getImatge());
                jsonPeticio.put("usuari", peticio.getUsuari().getUserId());
                jsonPeticions.put(jsonPeticio);
            }

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Llista de peticions");
            jsonResponse.put("data", jsonPeticions);

            // Converteix l'objecte JSON a una cadena amb pretty print (indentFactor > 0)
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al llistar les peticions");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();        }
    }

    @POST
    @Path("/crear_peticio")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPeticio(String jsonPeticio) {
        try {
            JSONObject peticio = new JSONObject(jsonPeticio);
            String prompt = peticio.getString("prompt");
            String model = peticio.getString("model");
            String imatge = peticio.getString("imatge");
            int usuariId = peticio.getInt("usuari");

            // Utilitza el mètode get de GenericDAO per obtenir l'usuari amb l'ID
            // especificat
            Usuari usuari = UsuariDAO.getUsuariPerId((long) usuariId);

            // Utilitza el mètode crearPeticio de PeticioDAO per crear una nova petició
            Peticio novaPeticio = PeticioDAO.crearPeticio(prompt, LocalDateTime.now(), model, imatge, usuari);

            if (novaPeticio != null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Petició creada");
                jsonResponse.put("data", novaPeticio.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Error al crear la petició");
                jsonResponse.put("data", "No s'ha pogut crear la petició");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al crear la petició");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }

    @GET
    @Path("/obtenir_peticions_per_usuari_id/{usuariId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenirPeticionsPerUsuariId(@PathParam("usuariId") long usuariId){
        try {
            // Utilitza el mètode obtenirPeticionsPerUsuariId de PeticioDAO per obtenir totes
            // les peticions d'un usuari
            Usuari usuari = UsuariDAO.getUsuariPerId(usuariId);
            List<?> peticions = (List<?>) PeticioDAO.llistarPeticionsPerUsuari(usuari);
            JSONArray jsonPeticions = new JSONArray();

            for (Object obj : peticions) {
                Peticio peticio = (Peticio) obj;
                JSONObject jsonPeticio = new JSONObject();
                jsonPeticio.put("id", peticio.getPeticioId());
                jsonPeticio.put("prompt", peticio.getPrompt());
                jsonPeticio.put("data", peticio.getData());
                jsonPeticio.put("model", peticio.getModel());
                jsonPeticio.put("imatge", peticio.getImatge());
                jsonPeticio.put("usuari", peticio.getUsuari().getUserId());
                jsonPeticions.put(jsonPeticio);
            }

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Llista de peticions per usuari");
            jsonResponse.put("data", jsonPeticions);

            // Converteix l'objecte JSON a una cadena amb pretty print (indentFactor > 0)
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build();
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al llistar les peticions per usuari");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }

    @DELETE
    @Path("/eliminar_peticio/{peticioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarPeticio(@PathParam("peticioId") long peticioId) {
        try {
            // Utilitza el mètode get de GenericDAO per obtenir la petició amb l'ID especificat
            Peticio peticio = PeticioDAO.getPeticioPerId(peticioId);

            if (peticio != null) {
                // Utilitza el mètode eliminar de GenericDAO per eliminar la petició
                PeticioDAO.eliminaPeticio(peticio);
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Petició eliminada");
                jsonResponse.put("data", peticio.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Error al eliminar la petició");
                jsonResponse.put("data", "No s'ha pogut eliminar la petició");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al eliminar la petició");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }

    @GET
    @Path("/obtenir_peticio_per_id/{peticioId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenirPeticioPerId(@PathParam("peticioId") long peticioId) {
        try {
            // Utilitza el mètode get de GenericDAO per obtenir la petició amb l'ID especificat
            Peticio peticio = PeticioDAO.getPeticioPerId(peticioId);

            if (peticio != null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Petició trobada");
                jsonResponse.put("data", peticio.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Error al trobar la petició");
                jsonResponse.put("data", "No s'ha trobat la petició");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al trobar la petició");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }



}
