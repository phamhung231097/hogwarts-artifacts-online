package edu.vgu.vn.hogwartsartifactsonline.wizard;

import edu.vgu.vn.hogwartsartifactsonline.system.Result;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import edu.vgu.vn.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.vgu.vn.hogwartsartifactsonline.wizard.converter.WizardToWizardDtoConverter;
import edu.vgu.vn.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
public class WizardController {

    WizardService wizardService;

    WizardToWizardDtoConverter wizardToWizardDtoConverter;
    WizardDtoToWizardConverter wizardDtoToWizardConverter;


    public WizardController(WizardService wizardService, WizardToWizardDtoConverter wizardToWizardDtoConverter,
                            WizardDtoToWizardConverter wizardDtoToWizardConverter) {
        this.wizardService = wizardService;
        this.wizardToWizardDtoConverter = wizardToWizardDtoConverter;
        this.wizardDtoToWizardConverter = wizardDtoToWizardConverter;
    }

    @GetMapping
    public Result findAllWizard()
    {
        List<WizardDto> wizardDtoList =
                wizardService.findAll()
                .stream().map(
                        wizard -> wizardToWizardDtoConverter.convert(wizard)
                        )
                        .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"Find All Success",wizardDtoList);
    }
    @GetMapping("/{wizardId}")
    public Result findWizardById(@PathVariable Integer wizardId)
    {
        Wizard returnedWizard = wizardService.findById(wizardId);
        WizardDto returnedWizardDto = wizardToWizardDtoConverter.convert(returnedWizard);
        return new Result(true,StatusCode.SUCCESS,"Find One Success",returnedWizardDto);
    }
    @PostMapping
    public Result addWizard(@RequestBody WizardDto wizardToBeCreatedDto)
    {
        Wizard wizardToBeCreated = wizardDtoToWizardConverter.convert(wizardToBeCreatedDto);
        Wizard addedWizard = wizardService.addWizard(wizardToBeCreated);
        WizardDto successAddedWizard = wizardToWizardDtoConverter.convert(addedWizard);
        return new Result(true,StatusCode.SUCCESS,"Add Success",successAddedWizard);
    }
    @PutMapping("/{wizardId}")
    public Result updateWizard(@RequestBody WizardDto wizardDto, @PathVariable Integer wizardId )
    {
        Wizard wizardToBeUpdated = wizardDtoToWizardConverter.convert(wizardDto);
        Wizard updatedWizard = wizardService.updateWizard(wizardId,wizardToBeUpdated);
        updatedWizard.setId(wizardId);
        WizardDto updatedWizardDto = wizardToWizardDtoConverter.convert(updatedWizard);
        return new Result(true,StatusCode.SUCCESS,"Update Success",updatedWizardDto);
    }
    @DeleteMapping("{wizardId}")
    public Result deleteWizard(@PathVariable Integer wizardId)
    {
        wizardService.deleteWizard(wizardId);
        return new Result(true,StatusCode.SUCCESS,"Delete Success");
    }
    @PutMapping("{wizardId}/artifacts/{artifactId}")
    public Result assignAritfact(@PathVariable Integer wizardId,@PathVariable String artifactId)
    {
        wizardService.assignArtifactToWizard(wizardId,artifactId);
        return new Result(true,StatusCode.SUCCESS,"Artifact Assignment Success");
    }
}
