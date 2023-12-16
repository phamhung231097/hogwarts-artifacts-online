package edu.vgu.vn.hogwartsartifactsonline.artifact.converter;

import edu.vgu.vn.hogwartsartifactsonline.artifact.Artifact;
import edu.vgu.vn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.vgu.vn.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConverter implements Converter<Artifact, ArtifactDto> {
    private final WizardToWizardDtoConverter wizardToWizardDtoConverter;

    public ArtifactToArtifactDtoConverter(WizardToWizardDtoConverter wizardToWizardDtoConverter) {
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        ArtifactDto artifactDto = new ArtifactDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner()!= null ? wizardToWizardDtoConverter.convert(source.getOwner()) : null );
        return artifactDto;
    }
}
