package es.um.sisdist.models;

import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@XmlRootElement
public class DatabaseDTO {
    private String id;
    private String name;
    private HashMap<String, String> pares;

    public DatabaseDTO(String id, String name, HashMap<String, String> pares) {
        this.id = id;
        this.name = name;
        this.pares = pares;
    }

    public DatabaseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getPares() {
        return pares;
    }

    public void setPares(HashMap<String, String> pares) {
        this.pares = pares;
    }
}
