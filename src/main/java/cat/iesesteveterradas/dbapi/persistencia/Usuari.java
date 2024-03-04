package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Set;

import org.json.JSONObject;

import jakarta.persistence.*;

@Entity
public class Usuari {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "telefon", nullable = false, unique = true)
    private String telefon;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "contrasenya", nullable = false)
    private String contrasenya;

    @Column(name = "codi_validacio")
    private String codiValidacio;

    @Lob
    @Column(name = "api_key", columnDefinition = "LONGTEXT")
    private String apiKey;

    @Column(name = "validat")
    private boolean validat;

    @Column(name = "tos")
    private boolean tos;

    @Column(name = "pla")
    private String pla;

    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Peticio> peticions;


    public Usuari() {
    }

    public Usuari(String nickname, String telefon, String email, String codiValidacio, String contrasenya, boolean validat, boolean tos, String pla, Set<Peticio> peticions, String apiKey) {
        this.nickname = nickname;
        this.telefon = telefon;
        this.email = email;
        this.codiValidacio = codiValidacio;
        this.contrasenya = contrasenya;
        this.validat = validat;
        this.tos = tos;
        this.pla = pla;
        this.peticions = peticions;
        this.apiKey = apiKey;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodiValidacio() {
        return codiValidacio;
    }

    public void setCodiValidacio(String codiValidacio) {
        this.codiValidacio = codiValidacio;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    public boolean isValidat() {
        return validat;
    }

    public void setValidat(boolean validat) {
        this.validat = validat;
    }

    public boolean isTos() {
        return tos;
    }

    public void setTos(boolean tos) {
        this.tos = tos;
    }

    public String getPla() {
        return pla;
    }

    public void setPla(String pla) {
        this.pla = pla;
    }

    public Set<Peticio> getPeticions() {
        return peticions;
    }

    public void setPeticions(Set<Peticio> peticions) {
        this.peticions = peticions;
    }

    @Override
    public String toString() {
        return "Usuari [userId=" + userId + ", nickname=" + nickname + ", telefon=" + telefon + ", email=" + email
                + ", codiValidacio=" + codiValidacio + ", contrasenya" + contrasenya + ", validat=" + validat + ", tos=" + tos + ", pla=" + pla
                + ", peticions=" + peticions + "]";
    }

    public JSONObject toJson() {
        JSONObject jsonUsuari = new JSONObject();
        jsonUsuari.put("id", userId);
        jsonUsuari.put("nickname", nickname);
        jsonUsuari.put("telefon", telefon);
        jsonUsuari.put("email", email);
        jsonUsuari.put("contrasenya", contrasenya);
        jsonUsuari.put("codi_validacio", codiValidacio);
        jsonUsuari.put("validat", validat);
        jsonUsuari.put("tos", tos);
        jsonUsuari.put("pla", pla);
        jsonUsuari.put("api_key", apiKey);
        return jsonUsuari;
    }


}
