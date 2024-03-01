package cat.iesesteveterradas.dbapi.endpoints;

import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import cat.iesesteveterradas.dbapi.persistencia.GenericDAO;
import cat.iesesteveterradas.dbapi.persistencia.Peticio;
import cat.iesesteveterradas.dbapi.persistencia.Usuari;
import cat.iesesteveterradas.dbapi.persistencia.UsuariDAO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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

    @GET
    @Path("/obtenir_usuari_per_id/{usuariId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenirUsuariPerId(@PathParam("usuariId") long usuariId){
        try {
            // Utilitza el mètode get de GenericDAO per obtenir l'usuari amb l'ID especificat
            Usuari usuari = UsuariDAO.getUsuariPerId(usuariId);

            if (usuari != null) {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Usuari trobat");
                jsonResponse.put("data", usuari.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Usuari no trobat");
                jsonResponse.put("data", "No s'ha pogut trobar l'usuari");
                return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al obtenir l'usuari");
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
            String telefon = jsonObject.getString("telefon");
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


    @DELETE
    @Path("/eliminar_usuari/{usuariId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuari(@PathParam("usuariId") Long usuariId){
        try {
            // Utilitza el mètode get de GenericDAO per obtenir l'usuari amb l'ID especificat
            Usuari usuari = UsuariDAO.getUsuariPerId(usuariId);

            if (usuari != null) {
                // Utilitza el mètode delete de GenericDAO per eliminar l'usuari
                UsuariDAO.eliminaUsuari(usuari);

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Usuari eliminat amb èxit");
                jsonResponse.put("data", usuari.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Usuari no trobat");
                jsonResponse.put("data", "No s'ha pogut eliminar l'usuari");
                return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al eliminar l'usuari");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }

    @PUT
    @Path("/actualitzar_usuari/{usuariId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualitzarUsuari(String jsonUsuari, @PathParam("usuariId") Long usuariId){
        try {
            // Utilitza el mètode get de GenericDAO per obtenir l'usuari amb l'ID especificat
            Usuari usuari = UsuariDAO.getUsuariPerId(usuariId);

            if (usuari != null) {
                JSONObject jsonObject = new JSONObject(jsonUsuari);
                String nickname = jsonObject.getString("nickname");
                String telefon = jsonObject.getString("telefon");
                String email = jsonObject.getString("email");
                String contrasenya = jsonObject.getString("contrasenya");
                String pla = jsonObject.getString("pla");
                Boolean tos = jsonObject.getBoolean("tos");
                Boolean validat = jsonObject.getBoolean("validat");

                usuari.setNickname(nickname);
                usuari.setTelefon(telefon);
                usuari.setEmail(email);
                usuari.setContrasenya(contrasenya);
                usuari.setPla(pla);
                usuari.setTos(tos);
                usuari.setValidat(validat);

                // Utilitza el mètode update de GenericDAO per actualitzar l'usuari
                UsuariDAO.actualitzaUsuari(usuari);

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "OK");
                jsonResponse.put("message", "Usuari actualitzat amb èxit");
                jsonResponse.put("data", usuari.toJson());
                return Response.ok(jsonResponse.toString(4)).build();
            } else {
                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("status", "ERROR");
                jsonResponse.put("message", "Usuari no trobat");
                jsonResponse.put("data", "No s'ha pogut actualitzar l'usuari");
                return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse.toString(4)).build();
            }
        } catch (Exception e) {
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("status", "ERROR");
            jsonResponse.put("message", "Error al actualitzar l'usuari");
            jsonResponse.put("data", e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonResponse.toString(4)).build();
        }
    }
        public static String generarCodigo() {
        Random random = new Random();
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            codigo.append(random.nextInt(10)); // Genera un número aleatorio entre 0 y 9
        }
        return codigo.toString();
    }
}
