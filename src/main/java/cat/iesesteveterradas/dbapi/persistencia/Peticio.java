package cat.iesesteveterradas.dbapi.persistencia;

import java.util.Date;

import jakarta.persistence.*;


@Entity
public class Peticio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peticio_id", unique = true, nullable = false)
    private Long peticioId;

    @Column(name = "prompt")
    private String prompt;

    @Column(name = "data")
    private Date data;

    @Column(name = "model")
    private String model;

    @Column(name = "imatge")
    private String imatge;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Usuari usuari;

    public Peticio() {
    }

    public Peticio(String prompt, Date data, String model, String imatge, Usuari usuari) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
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


}
