package edu.vgu.vn.hogwartsartifactsonline.artifact.dto;

import edu.vgu.vn.hogwartsartifactsonline.wizard.dto.WizardDto;

public record ArtifactDto(String id,
                          String name,
                          String description,
                          String imageUrl,
                          WizardDto owner)
{

}
