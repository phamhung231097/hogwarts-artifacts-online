package edu.vgu.vn.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vgu.vn.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ArtifactService artifactService;
    List<Artifact> artifacts;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();
        Artifact a1 = new Artifact();
        a1.setId("1250808601744904191");
        a1.setName("Deluminator");
        a1.setDescription("A Deluminator is a device invented" +
                " by Albus Dumbledore that resembles a cigarette lighter." +
                " It is used to remove or absorb (as well as return)" +
                " the light from any light source " +
                "to provide cover to the user.");
        a1.setImageUrl("ImageUrl");
        Artifact a2 = new Artifact();
        a2.setId("1250808601744904192");
        a2.setName("Invisibility Cloak");
        a2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a2.setImageUrl("ImageUrl");
        Artifact a3 = new Artifact();
        a3.setId("1250808601744904193");
        a3.setName("Elder Wand");
        a3.setDescription("The Elder Wand," +
                " known throughout history as the Deathstick or the Wand of Destiny," +
                " is an extremely powerful wand made of elder wood " +
                "with a core of Thestral tail hair.");
        a3.setImageUrl("ImageUrl");
        Artifact a4 = new Artifact();
        a4.setId("1250808601744904194");
        a4.setName("The Marauder's Map");
        a4.setDescription("A magical map of Hogwarts created by Remus Lupin," +
                " Peter Pettigrew," +
                " Sirius Black," +
                " and James Potter while they were students at Hogwarts.");
        a4.setImageUrl("ImageUrl");
        Artifact a5 = new Artifact();
        a5.setId("1250808601744904195");
        a5.setName("The Sword Of Gryffindor");
        a5.setDescription("A goblin-made sword adorned with large rubies on the pommel. " +
                "It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts..");
        a5.setImageUrl("ImageUrl");
        artifacts.add(a1);
        artifacts.add(a2);
        artifacts.add(a3);
        artifacts.add(a4);
        artifacts.add(a5);
    }
    @AfterEach
    void tearDown() {
    }
    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //given
        given(artifactService.
                findById("1250808601744904191")).willReturn(artifacts.get(0));
        //when and then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data.name").value("Deluminator"));

    }
    @Test
    void testFindArtifactByIdNotFound() throws Exception
    {
        //given
        given(artifactService.findById("1250808601744904191")).willThrow(new ArtifactNotFoundException("1250808601744904191"));
        //when and then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/1250808601744904191").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 1250808601744904191"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void findAllArtifactsSuccess() throws Exception {
        //Given
        given(artifactService.findAll()).willReturn(artifacts);

        //When and Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find all success"))
                .andExpect(jsonPath("$.data.size()", Matchers.hasSize(artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1250808601744904191"))
                .andExpect(jsonPath("$.data[0].name").value("Deluminator"));
    }
    @Test
    void testAddArtifactSuccess() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto(null,"Rememberall",
                "????????",
                "ImageURL",
                null);
        String json =objectMapper.writeValueAsString(artifactDto);
        //When and then

        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("1250808601744904197");
        savedArtifact.setName("Rememberall");
        savedArtifact.setDescription("??????");
        savedArtifact.setImageUrl("ImageUrl");

        //Given
        given(artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);
        //When and then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/artifacts")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()));

    }
    @Test
    void testUpdateArtifactSuccess() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904197","Rememberall",
                "Updated description",
                "ImageURL",
                null);
        String json =objectMapper.writeValueAsString(artifactDto);
        //When and then

        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1250808601744904197");
        updatedArtifact.setName("Rememberall");
        updatedArtifact.setDescription("Updated description");
        updatedArtifact.setImageUrl("ImageUrl");

        //Given
        given(artifactService.updateArtifact(Mockito.any(Artifact.class),eq("1250808601744904197"))).willReturn(updatedArtifact);
        //When and then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/1250808601744904197")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update success"))
                .andExpect(jsonPath("$.data.id").value("1250808601744904197"))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()));

    }
    @Test
    void testUpdateArtifactErrorWithNonExistenceId() throws Exception {
        //Given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904197","Rememberall",
                "Updated description",
                "ImageURL",
                null);
        String json =objectMapper.writeValueAsString(artifactDto);
        //When and then


        //Given
        given(artifactService.updateArtifact(Mockito.any(Artifact.class),eq("1250808601744904197"))).willThrow(new ArtifactNotFoundException("1250808601744904197"));
        //When and then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/1250808601744904197")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with id 1250808601744904197"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void testDeleteArtifactSuccess() throws Exception {
        //given
        ArtifactDto artifactDto = new ArtifactDto("1250808601744904197","Remberall","Description","ImageUrl",null);
        Artifact deleteArtifact = new Artifact();
        deleteArtifact.setId("1250808601744904197");
        deleteArtifact.setName("Rememberall");
        deleteArtifact.setDescription("Description");
        deleteArtifact.setImageUrl("ImageUrl");
        String json = objectMapper.writeValueAsString(artifactDto);
        //when
        given(artifactService.deleteArtifact(eq("1250808601744904197"))).willReturn(deleteArtifact);

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/1250808601744904197")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact deleted"))
                .andExpect(jsonPath("$.data.name").value(deleteArtifact.getName()));
    }

}