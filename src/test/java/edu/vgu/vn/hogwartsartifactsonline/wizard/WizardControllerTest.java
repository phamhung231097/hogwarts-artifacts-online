package edu.vgu.vn.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.vgu.vn.hogwartsartifactsonline.exception.WizardNameCannotBeNullException;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import edu.vgu.vn.hogwartsartifactsonline.wizard.converter.WizardDtoToWizardConverter;
import edu.vgu.vn.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class WizardControllerTest {
    @MockBean
    WizardService wizardService;
    @Autowired
    WizardDtoToWizardConverter wizardDtoToWizardConverter;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${api.endpoint.base-url}")
    String baseUrl;
    List<Wizard> wizardList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        //given
        Wizard w1 = new Wizard();
        w1.setId(1);
        w1.setName("Naruto");


        Wizard w2 = new Wizard();
        w2.setId(2);
        w2.setName("Harry Portbeo");

        Wizard w3 = new Wizard();
        w3.setId(3);
        w3.setName("Sasuke");

        wizardList.add(w1);
        wizardList.add(w2);
        wizardList.add(w3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAllSucess() throws Exception {
        //given
        given(wizardService.findAll()).willReturn(wizardList);
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.size()").value(wizardList.size()));
    }
    @Test
    void testFindWizardByIdSuccess() throws Exception {
        //given
        given(wizardService.findById(1)).willReturn(wizardList.get(0));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(wizardList.get(0).getId()))
                .andExpect(jsonPath("$.data.name").value(wizardList.get(0).getName()));
    }
    @Test
    void testFindWizardByIdFail() throws Exception {
        //given
        given(wizardService.findById(1)).willThrow(new ObjectNotFoundException("Wizard",1));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/wizards/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 1"));
    }
    @Test
    void testUpdateWizardSuccess() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(4,"Nigga Wizard",null);
        String json = objectMapper.writeValueAsString(wizardDto);
        Wizard wizardToBeUpdated = wizardDtoToWizardConverter.convert(wizardDto);
        given(wizardService.updateWizard(eq(4), Mockito.any(Wizard.class))).willReturn(wizardToBeUpdated);
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/4")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.name").value(wizardToBeUpdated.getName()))
                .andExpect(jsonPath("$.data.id").value(wizardToBeUpdated.getId()));
    }
    @Test
    void testUpdateWizardNotFoundFail() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(4,"Nigga Wizard",null);
        String json = objectMapper.writeValueAsString(wizardDto);
        given(wizardService.updateWizard(eq(4),Mockito.any(Wizard.class))).willThrow(new ObjectNotFoundException("Wizard",4));

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/4")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 4"));
    }
    @Test
    void testUpdateWizardBadRequestFail() throws Exception {
        //given
        WizardDto wizardDto = new WizardDto(4,"",null);
        String json = objectMapper.writeValueAsString(wizardDto);
        given(wizardService.updateWizard(eq(4),Mockito.any(Wizard.class))).willThrow(new WizardNameCannotBeNullException());

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/4")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.INVALID_ARGUMENT))
                .andExpect(jsonPath("$.message").value("Provided arguments are invalid, see data for details."));
    }
    @Test
    void testDeleteWizardIdNotFoundFail() throws Exception {
        //given
        given(wizardService.deleteWizard(4)).willThrow(new ObjectNotFoundException("Wizard",4));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/wizards/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 4"));
    }
    @Test
    void deleteWizardSuccess() throws Exception {
        //given
        Wizard wizardToBeDeleted = new Wizard();
        wizardToBeDeleted.setName("wizard");
        wizardToBeDeleted.setId(4);
        given(wizardService.findById(4)).willReturn(wizardToBeDeleted);
        //when

        //then
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/wizards/4")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value("true"))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));

    }
    @Test
    void testAssignArtifactSuccess() throws Exception {
        //given
        doNothing().when(this.wizardService).assignArtifactToWizard(2,"1250808601744904191");
        //when and then
        //then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/2/artifacts/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value("true"))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Artifact Assignment Success"));
    }
    @Test
    void testAssignArtifactNonExistenceWizardId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("Wizard",5)).when(this.wizardService).assignArtifactToWizard(5,"1250808601744904191");
        //when and then
        //then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/5/artifacts/1250808601744904191")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Wizard with Id 5"));
    }
    @Test
    void testAssignArtifactNonExistenceArtifactId() throws Exception {
        //given
        doThrow(new ObjectNotFoundException("Artifact","1250808601744904199")).when(this.wizardService).assignArtifactToWizard(2,"1250808601744904199");
        //when and then
        //then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/wizards/2/artifacts/1250808601744904199")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Artifact with Id 1250808601744904199"));
    }

}