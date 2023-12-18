package edu.vgu.vn.hogwartsartifactsonline.artifact;

import edu.vgu.vn.hogwartsartifactsonline.artifact.converter.ArtifactDtoToArtifactConverter;
import edu.vgu.vn.hogwartsartifactsonline.artifact.converter.ArtifactToArtifactDtoConverter;
import edu.vgu.vn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.vgu.vn.hogwartsartifactsonline.system.Result;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
public class ArtifactController {
    private final ArtifactService artifactService;

    private final ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter;
    private final ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter;


    public ArtifactController(ArtifactService artifactService,
                              ArtifactToArtifactDtoConverter artifactToArtifactDtoConverter,
                              ArtifactDtoToArtifactConverter artifactDtoToArtifactConverter) {
        this.artifactService = artifactService;
        this.artifactToArtifactDtoConverter = artifactToArtifactDtoConverter;
        this.artifactDtoToArtifactConverter = artifactDtoToArtifactConverter;
    }

    @GetMapping("/{artifactId}")
    public Result findArtifactById(@PathVariable String artifactId)
    {
        Artifact foundArtifact = artifactService.findById(artifactId);
        ArtifactDto artifactDto = artifactToArtifactDtoConverter.convert(foundArtifact);
        return new Result(true, StatusCode.SUCCESS,"Find One Success",artifactDto);
    }
    @GetMapping
    public Result findAllArtifact()
    {
        List<Artifact> foundArtifacts = artifactService.findAll();
        //Convert found artifacts to a list of artifactDto
        List<ArtifactDto> artifactDtos = foundArtifacts.stream()
                .map(artifactToArtifactDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true,StatusCode.SUCCESS,"Find all success",artifactDtos);
    }
    @PostMapping
    public Result addArtifact(@RequestBody ArtifactDto artifactDto)
    {

        Artifact newArtifact = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact savedArtifact = artifactService.save(newArtifact);
        ArtifactDto savedArtifactDto = artifactToArtifactDtoConverter.convert(savedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Add success",savedArtifactDto);
    }
    @PutMapping("/{artifactId}")
    public Result updateArtifact(@RequestBody ArtifactDto artifactDto,@PathVariable String artifactId)
    {
        Artifact update = artifactDtoToArtifactConverter.convert(artifactDto);
        Artifact updatedArtifact = artifactService.updateArtifact(update,artifactId);
        ArtifactDto updatedArtifactDto = artifactToArtifactDtoConverter.convert(updatedArtifact);
        return new Result(true,StatusCode.SUCCESS,"Update success",updatedArtifactDto);
    }
    @DeleteMapping("/{artifactId}")
    public Result deleteArtifact(@PathVariable String artifactId)
    {
        Artifact deletedArtifact = artifactService.deleteArtifact(artifactId);
        return new Result(true,StatusCode.SUCCESS,"Artifact deleted",deletedArtifact);
    }
}
