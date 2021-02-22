package poc.automation.backend.entity;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
public class Client extends AbstractEntity {
    private String name;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Study> studies = new LinkedList<>();

    public Client() {
    }

    public Client(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Study> getStudies() {
        return studies;
    }
}
