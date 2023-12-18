package edu.vgu.vn.hogwartsartifactsonline.wizard.converter;

import edu.vgu.vn.hogwartsartifactsonline.wizard.Wizard;
import edu.vgu.vn.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WizardDtoToWizardConverter implements Converter<WizardDto, Wizard> {

    @Override
    public Wizard convert(WizardDto source) {
        Wizard convertedWizard = new Wizard();
        convertedWizard.setId(source.id());
        convertedWizard.setName(source.name());
        return convertedWizard;
    }
}
