package cat.iesesteveterradas.dbapi.persistencia;

import java.time.LocalDateTime;

import org.json.JSONObject;

import jakarta.persistence.*;


@Entity
public class Peticio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peticio_id", unique = true, nullable = false)
    private Long peticioId;

    @Lob
    @Column(name = "prompt", columnDefinition = "LONGTEXT")
    private String prompt;

    @Column(name = "data")
    private LocalDateTime data;

    @Column(name = "model")
    private String model;

    @Lob
    @Column(name = "imatge", columnDefinition = "LONGTEXT")
    private String imatge;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private Usuari usuari;

    public Peticio() {
    }

    public Peticio(String prompt, LocalDateTime data, String model, String imatge, Usuari usuari) {
        this.prompt = prompt;
        this.data = data;
        this.model = model;
        this.imatge = imatge;
        this.usuari = usuari;
    }

    public Long getPeticioId() {
        return peticioId;
    }

    public void setPeticioId(Long peticioId) {
        this.peticioId = peticioId;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    @Override
    public String toString() {
        return "Peticio [peticioId=" + peticioId + ", prompt=" + prompt + ", data=" + data + ", model=" + model + ", imatge=" + imatge + ", usuari=" + usuari.getNickname() + "]";
    }

    public JSONObject toJson() {
        JSONObject jsonPeticio = new JSONObject();
        jsonPeticio.put("id", peticioId);
        jsonPeticio.put("prompt", prompt);
        jsonPeticio.put("data", data);
        jsonPeticio.put("model", model);
        jsonPeticio.put("imatge", imatge);
        jsonPeticio.put("usuari", usuari.getUserId());
        return jsonPeticio;
    }


}
