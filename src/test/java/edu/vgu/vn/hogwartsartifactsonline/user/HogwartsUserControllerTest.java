package edu.vgu.vn.hogwartsartifactsonline.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUser;
import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUserDto;
import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUserService;
import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class HogwartsUserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    HogwartsUserService hogwartsUserService;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${api.endpoint.base-url}")
    String baseUrl;
    List<HogwartsUser> hogwartsUserList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        HogwartsUser user1 = new HogwartsUser();
        user1.setId(1);
        user1.setUsername("niggauser");
        user1.setPassword("mypassword");
        user1.setRoles("user admin");
        user1.setEnable(true);


        HogwartsUser user2 = new HogwartsUser();
        user2.setId(2);
        user2.setUsername("niggauser1");
        user2.setPassword("mypassword1");
        user2.setRoles("user");
        user2.setEnable(true);

        HogwartsUser user3 = new HogwartsUser();
        user3.setId(3);
        user3.setUsername("niggauser2");
        user3.setPassword("mypassword2");
        user3.setRoles("user");
        user3.setEnable(true);
        hogwartsUserList.add(user1);
        hogwartsUserList.add(user2);
        hogwartsUserList.add(user3);
    }

    @AfterEach
    void tearDown() {

    }
    @Test
    void testFindAllArtifactSuccess() throws Exception {
        //given
        given(hogwartsUserService.findAllUsers()).willReturn(hogwartsUserList);
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(hogwartsUserList.get(0).getId()))
                .andExpect(jsonPath("$.data[0].username").value(hogwartsUserList.get(0).getUsername()));
    }
    @Test
    void findUserByIdSuccess() throws Exception {
        //given
        given(hogwartsUserService.findUserById(1)).willReturn(hogwartsUserList.get(0));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(hogwartsUserList.get(0).getId()))
                .andExpect(jsonPath("$.data.username").value(hogwartsUserList.get(0).getUsername()));
    }
    @Test
    void findUserByIdNotFound() throws Exception {
        //given
        given(hogwartsUserService.findUserById(1)).willThrow(new ObjectNotFoundException("Hogwarts User",1));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl+"/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Hogwarts User with Id 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void addUserSuccess() throws Exception {
        //given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setId(4);
        newUser.setUsername("niggauser1");
        newUser.setPassword("password1");
        newUser.setEnable(true);
        newUser.setRoles("admin user");
        String json = objectMapper.writeValueAsString(newUser);
        //when
        given(hogwartsUserService.addUser(newUser)).willReturn(newUser);
        //then
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl+"/users")
                .contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.username").value(newUser.getUsername()));
    }
    @Test
    void testUpdateUserSuccess() throws Exception {
        //given
        HogwartsUserDto updatedUserDto = new HogwartsUserDto(4,"niggauser2",true,"admin user");
        String json = objectMapper.writeValueAsString(updatedUserDto);
        HogwartsUser newUser = new HogwartsUser();
        newUser.setId(4);
        newUser.setUsername("niggauser2");
        newUser.setPassword("password");
        newUser.setEnable(true);
        newUser.setRoles("admin user");
        //when
        given(hogwartsUserService.updateUser(Mockito.any(HogwartsUser.class),eq(4))).willReturn(newUser);
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/users/4")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.username").value(updatedUserDto.username()))
                .andExpect(jsonPath("$.data.roles").value(updatedUserDto.roles()));

    }
    @Test
    void testUpdateUserNotFoundFail() throws Exception {
        //given
        HogwartsUserDto updatedUserDto = new HogwartsUserDto(4,"niggauser2",true,"admin user");
        String json = objectMapper.writeValueAsString(updatedUserDto);
        given(hogwartsUserService.updateUser(Mockito.any(HogwartsUser.class),eq(4))).willThrow(new ObjectNotFoundException("Hogwarts User",4));
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl+"/users/4")
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Hogwarts User with Id 4"));
    }
    @Test
    void testDeleteUserSuccess() throws Exception {
        HogwartsUser newUser = new HogwartsUser();
        newUser.setId(4);
        newUser.setUsername("niggauser2");
        newUser.setPassword("password");
        newUser.setEnable(true);
        newUser.setRoles("admin user");
        //given
        given(hogwartsUserService.findUserById(4)).willReturn(newUser);
        //when and then
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"));
    }
    @Test
    void testDeleteUserNotFound() throws Exception {
        HogwartsUser newUser = new HogwartsUser();
        newUser.setId(4);
        newUser.setUsername("niggauser2");
        newUser.setPassword("password");
        newUser.setEnable(true);
        newUser.setRoles("admin user");
        //given

        //when and then
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl+"/users/4").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find Hogwarts User with Id 4"));
    }
}
