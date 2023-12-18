package edu.vgu.vn.hogwartsartifactsonline.wizard;

import edu.vgu.vn.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.annotation.processing.Generated;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String name;
    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE},mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }
    public void addArtifact(Artifact artifact){
        artifact.setOwner(this);
        artifacts.add(artifact);
    }
    public int getNumberOfArtifacts(){
        return artifacts.size();
    }
    public void deleteAssociatedArtifacts()
    {
        artifacts.stream().forEach(artifact -> {
            artifact.setOwner(null);
        });
        artifacts = null;
    }

    public void removeArtifact(Artifact associatedArtifact) {
        // Remove artifact owner
        associatedArtifact.setOwner(null);
        artifacts.remove(associatedArtifact);
    }
}
