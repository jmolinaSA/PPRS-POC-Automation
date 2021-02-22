package poc.automation.backend.entity;

import org.dom4j.tree.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Study extends AbstractEntity implements Cloneable
{

    @Id

    @NotNull
    @NotEmpty
    private String ID = "";

    @NotNull
    @NotEmpty
    private String studyName = "";

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Client client;

    private LocalDate createdOn;

    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }

    public String getStudyName() { return studyName; }
    public void setStudyName(String studyName) { this.studyName = studyName; }

    public LocalDate getCreatedOn() { return createdOn; }
    public void setCreatedOn(LocalDate createdOn) { this.createdOn = createdOn; }

    public void setClient(Client client) { this.client = client; }
    public Client getClient () { return client; }

    @Override
    public String toString()
    {
        return studyName + " " + ID;
    }
}
