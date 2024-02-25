package cat.iesesteveterradas.dbapi.endpoints;

import java.util.List;

import org.hibernate.query.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.GenericDAO;
import cat.iesesteveterradas.dbapi.persistencia.Peticio;
import cat.iesesteveterradas.dbapi.persistencia.SessionFactoryManager;
import cat.iesesteveterradas.dbapi.persistencia.Usuari;
import cat.iesesteveterradas.dbapi.persistencia.UsuariDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuaris")
public class UsuariResource {

    @GET
    @Path("/llistar_usuaris")
    @Produces(MediaType.APPLICATION_JSON)
    public Response llistarUsuaris() {
        try {
            // Utilitza el mètode listCollection de Manager per obtenir tots els usuaris
            List<?> usuaris = (List<?>) GenericDAO.listCollection(Usuari.class);
            JSONArray jsonUsuaris = new JSONArray();

            for (Object obj : usuaris) {
                Usuari usuari = (Usuari) obj;
                JSONObject jsonUsuari = new JSONObject();
                jsonUsuari.put("id", usuari.getUserId());
                jsonUsuari.put("nickname", usuari.getNickname());
                jsonUsuari.put("telefon", usuari.getTelefon());
                jsonUsuari.put("email", usuari.getEmail());
                jsonUsuari.put("codi_validacio", usuari.getCodiValidacio());
                jsonUsuari.put("contrasenya", usuari.getContrasenya());
                jsonUsuari.put("validat", usuari.isValidat());
                jsonUsuari.put("tos", usuari.isTos());
                jsonUsuari.put("pla", usuari.getPla());

                JSONArray jsonPeticions = new JSONArray();
                if (!usuari.getPeticions().isEmpty()) {
                    for (Peticio peticio : usuari.getPeticions()) {
                        JSONObject jsonPeticio = new JSONObject();
                        jsonPeticio.put("id", peticio.getPeticioId());
                        jsonPeticio.put("prompt", peticio.getPrompt());
                        jsonPeticio.put("data", peticio.getData());
                        jsonPeticio.put("model", peticio.getModel());

                        jsonPeticions.put(jsonPeticio);
                    }
                    jsonUsuari.put("peticions", jsonPeticions);
                }
                else {
                    jsonUsuari.put("peticions", "[]");
                }

                jsonUsuaris.put(jsonUsuari);
            }

            // Crea l'objecte JSON principal que inclou la llista d'usuaris
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "OK");
            jsonResponse.put("message", "Llista d'usuaris");
            jsonResponse.put("data", jsonUsuaris);

            // Converteix l'objecte JSON a una cadena amb pretty print (indentFactor > 0)
            String prettyJsonResponse = jsonResponse.toString(4); // 4 espais per indentar
            return Response.ok(prettyJsonResponse).build(); // Retorna l'objecte JSON com a resposta
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al llistar els usuaris");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }

    @POST
    @Path("/registrar_usuari")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuari(String jsonUsuari) {

        try {
            JSONObject jsonObject = new JSONObject(jsonUsuari);
            String nickname = jsonObject.getString("nickname");
            int telefon = jsonObject.getInt("telefon");
            String email = jsonObject.getString("email");
            String contrasenya = jsonObject.getString("contrasenya");
            Usuari usuari = UsuariDAO.crearUsuari(nickname, telefon, email, contrasenya);

            if (usuari != null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Usuari registrat amb èxit");
                jsonResponse.put("data", usuari.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Error al registrar l'usuari");

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al registrar l'usuari");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }
}
