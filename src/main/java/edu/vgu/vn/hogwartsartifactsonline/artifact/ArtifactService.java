package edu.vgu.vn.hogwartsartifactsonline.artifact;

import edu.vgu.vn.hogwartsartifactsonline.artifact.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtifactService {
    private final ArtifactRepository artifactRepository;
    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId)
    {
        return artifactRepository.findById(artifactId)
                .orElseThrow(()->new ArtifactNotFoundException(artifactId));
    }
    public List<Artifact> findAll()
    {
        return artifactRepository.findAll();
    }
    public Artifact save(Artifact newArtifact)
    {
        newArtifact.setId(idWorker.nextId()+"");
        return artifactRepository.save(newArtifact);
    }
    public Artifact updateArtifact(Artifact updateArtifact, String artifactId)
    {
        return artifactRepository.findById(artifactId)
                .map(oldArtifact->
                {
                    oldArtifact.setName(updateArtifact.getName());
                    oldArtifact.setDescription(updateArtifact.getDescription());
                    oldArtifact.setImageUrl(updateArtifact.getImageUrl());
                    oldArtifact.setOwner(updateArtifact.getOwner());
                    return artifactRepository.save(oldArtifact);
                })
                .orElseThrow(()->new ArtifactNotFoundException(artifactId));

    }
    public  Artifact deleteArtifact(String artifactId)
    {
        return artifactRepository.findById(artifactId)
                .map(deleteArtifact-> {
                    artifactRepository.delete(deleteArtifact);
                    return deleteArtifact ;
                })
                .orElseThrow(()->new ArtifactNotFoundException(artifactId));
    }
}
