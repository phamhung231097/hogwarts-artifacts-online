package edu.vgu.vn.hogwartsartifactsonline.user;

import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUser;
import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUserRepository;
import edu.vgu.vn.hogwartsartifactsonline.HogwartsUser.HogwartsUserService;
import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class HogwartsUserServiceTest {
    @Mock
    HogwartsUserRepository hogwartsUserRepository;
    @InjectMocks
    HogwartsUserService hogwartsUserService;
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


    @AfterAll
    static void afterAll() {

    }
    @Test
    void testFindAllUserSuccess()
    {
        //given
        given(hogwartsUserRepository.findAll()).willReturn(hogwartsUserList);
        //when
        List<HogwartsUser> hogwartsUsers = hogwartsUserService.findAllUsers();
        //then
        assertThat(hogwartsUsers.size()).isEqualTo(hogwartsUserList.size());
        assertThat(hogwartsUsers.get(0).getUsername()).isEqualTo(hogwartsUserList.get(0).getUsername());
        assertThat(hogwartsUsers.get(0).getId()).isEqualTo(hogwartsUserList.get(0).getId());

    }
    @Test
    void testFindUserByIdSuccess()
    {
        //given
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.of(hogwartsUserList.get(0)));
        //when
        HogwartsUser hogwartsUser = hogwartsUserService.findUserById(1);
        //then
        assertThat(hogwartsUser.getUsername()).isEqualTo(hogwartsUserList.get(0).getUsername());
        assertThat(hogwartsUser.getId()).isEqualTo(hogwartsUserList.get(0).getId());
        verify(hogwartsUserRepository,times(1)).findById(1);

    }
    @Test
    void testFindUserByIdNotFound()
    {
        //given
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->
        {
            hogwartsUserService.findUserById(1);
        });
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(hogwartsUserRepository,times(1)).findById(1);
    }
    @Test
    void testAddUserSuccess()
    {
        //given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setId(1);
        newUser.setUsername("username");
        newUser.setPassword("password");
        newUser.setRoles("user");
        newUser.setEnable(true);
        given(hogwartsUserRepository.save(newUser)).willReturn(newUser);
        //when
        HogwartsUser savedUser = hogwartsUserService.addUser(newUser);
        //then
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getId()).isEqualTo(newUser.getId());
        assertThat(savedUser.getRoles()).isEqualTo(newUser.getRoles());
        verify(hogwartsUserRepository,times(1)).save(newUser);
    }
    @Test
    void updateHogwartsUserSuccess()
    {
        //given
        HogwartsUser mockUser = new HogwartsUser();
        mockUser.setId(1);
        mockUser.setUsername("username");
        mockUser.setPassword("password");
        mockUser.setRoles("user");
        mockUser.setEnable(true);
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.of(mockUser));
//        given(hogwartsUserRepository.save(mockUser)).willReturn(mockUser);
        //when
        HogwartsUser userToBeSaved = new HogwartsUser();
        userToBeSaved.setId(1);
        userToBeSaved.setUsername("new username");
        userToBeSaved.setPassword("password");
        userToBeSaved.setEnable(false);
        userToBeSaved.setRoles("admin user");
        HogwartsUser savedUser = hogwartsUserService.updateUser(userToBeSaved,1);
        //then
        assertThat(savedUser.getUsername()).isEqualTo(userToBeSaved.getUsername());
        assertThat(savedUser.getId()).isEqualTo(userToBeSaved.getId());
        assertThat(savedUser.getRoles()).isEqualTo(userToBeSaved.getRoles());
        verify(hogwartsUserRepository,times(1)).findById(1);
    }
    @Test
    void testUpdateHogwartsUserNotFound() {
        //given
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.empty());
        //when
        HogwartsUser userToBeSaved = new HogwartsUser();
        userToBeSaved.setId(1);
        userToBeSaved.setUsername("new username");
        userToBeSaved.setPassword("password");
        userToBeSaved.setEnable(false);
        userToBeSaved.setRoles("admin user");
        Throwable thrown = catchThrowable(() -> hogwartsUserService.updateUser(userToBeSaved, 1));
        assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
        verify(hogwartsUserRepository, times(1)).findById(1);
    }
    @Test
    void testDeleteUserSuccess()
    {
        //given
        HogwartsUser mockUser = new HogwartsUser();
        mockUser.setId(1);
        mockUser.setUsername("username");
        mockUser.setPassword("password");
        mockUser.setRoles("user");
        mockUser.setEnable(true);
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.of(mockUser));
        //when
        hogwartsUserService.deleteUser(1);
        verify(hogwartsUserRepository,times(1)).findById(1);
        verify(hogwartsUserRepository,times(1)).delete(Mockito.any(HogwartsUser.class));
    }
    @Test
    void testDeleteUserNotFound()
    {
        //given
        given(hogwartsUserRepository.findById(1)).willReturn(Optional.empty());
        //when and then
        assertThrows(ObjectNotFoundException.class,()->{
           hogwartsUserService.deleteUser(1);
        });
    }
}
