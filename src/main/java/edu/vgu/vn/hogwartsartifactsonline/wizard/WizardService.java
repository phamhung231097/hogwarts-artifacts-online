package edu.vgu.vn.hogwartsartifactsonline.wizard;

import edu.vgu.vn.hogwartsartifactsonline.artifact.Artifact;
import edu.vgu.vn.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.vgu.vn.hogwartsartifactsonline.exception.WizardNameCannotBeNullException;
import edu.vgu.vn.hogwartsartifactsonline.wizard.utils.WizardIdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class WizardService {
    private final WizardReposistory wizardReposistory;
    private final WizardIdWorker wizardIdWorker;
    private final ArtifactRepository artifactRepository;

    public WizardService(WizardReposistory wizardReposistory, WizardIdWorker wizardIdWorker, ArtifactRepository artifactRepository) {
        this.wizardReposistory = wizardReposistory;
        this.wizardIdWorker = wizardIdWorker;
        this.artifactRepository = artifactRepository;
    }

    public List<Wizard> findAll()
    {
        return wizardReposistory.findAll();
    }
    public Wizard findById(Integer id)
    {
        return  wizardReposistory.findById(id).orElseThrow(()->{
            return new ObjectNotFoundException("Wizard",id);
        });

    }
    public Wizard addWizard(Wizard wizardToBeCreated)
    {
        if(wizardToBeCreated.getName().isEmpty())
        {
           throw new WizardNameCannotBeNullException();
        }
//        wizardToBeCreated.setId(wizardIdWorker.nextId());
        return wizardReposistory.save(wizardToBeCreated);
    }
    public Wizard updateWizard(Integer wizardId,Wizard wizardToBeUpdated)
    {
        if(wizardToBeUpdated.getName().isEmpty())
        {
            throw new WizardNameCannotBeNullException();
        }
        return wizardReposistory.findById(wizardId)
                .map(updatingWizard->{
                    updatingWizard.setName(wizardToBeUpdated.getName());
                    return wizardReposistory.save(updatingWizard);
                })
                .orElseThrow(
                ()-> new ObjectNotFoundException("Wizard",wizardId)
        );
    }
    public Wizard deleteWizard(Integer wizardId)
    {
        Wizard foundWizard =  wizardReposistory.findById(wizardId).orElseThrow(
                ()->{return new ObjectNotFoundException("Wizard",wizardId);}
        );
        foundWizard.deleteAssociatedArtifacts();
        wizardReposistory.delete(foundWizard);
        return foundWizard;
    }
    public void assignArtifactToWizard(Integer wizardId, String artifactId)
    {
        Artifact associatedArtifact = artifactRepository.findById(artifactId).orElseThrow(
                ()->{ return new ObjectNotFoundException("Artifact",artifactId);}
        );
        Wizard associatedWizard = wizardReposistory.findById(wizardId).orElseThrow(
                ()->{
                    return new ObjectNotFoundException("Wizard",wizardId);
                }
        );
//        if(associatedArtifact.getOwner() != null)
//        {
//            throw new ArtifactAlreadyAssignedException(associatedArtifact.getOwner().getName());
//        }
        //find Artifact assignment
        //we need to see if artifact already assigned to any owner or not
        if(associatedArtifact.getOwner() !=null)
        {
            associatedArtifact.getOwner().removeArtifact(associatedArtifact);
        }
        associatedWizard.addArtifact(associatedArtifact);
//        artifactRepository.save(associatedArtifact);
//        wizardReposistory.save(associatedWizard);
    }
}
