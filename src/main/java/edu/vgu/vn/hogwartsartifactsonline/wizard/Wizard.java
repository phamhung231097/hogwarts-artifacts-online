package edu.vgu.vn.hogwartsartifactsonline.wizard;

import edu.vgu.vn.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {
    @Id
    @GeneratedValue
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
}
