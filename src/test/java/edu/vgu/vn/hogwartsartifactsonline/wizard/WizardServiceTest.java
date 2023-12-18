package edu.vgu.vn.hogwartsartifactsonline.wizard;

import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import edu.vgu.vn.hogwartsartifactsonline.exception.WizardNameCannotBeNullException;
import edu.vgu.vn.hogwartsartifactsonline.wizard.utils.WizardIdWorker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    WizardReposistory wizardReposistory;
    @Mock
    WizardIdWorker wizardIdWorker;
    @InjectMocks
    WizardService wizardService;
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
    void findAllSuccess() {
        given(wizardReposistory.findAll()).willReturn(wizardList);
        //when
        List<Wizard> allWizard = wizardService.findAll();
        //then
        assertThat(allWizard.size()).isEqualTo(wizardList.size());
        assertThat(allWizard.get(0).getId()).isEqualTo(wizardList.get(0).getId());
        assertThat(allWizard.get(0).getName()).isEqualTo(wizardList.get(0).getName());
        verify(wizardReposistory,times(1)).findAll();
    }
    @Test
    void testFindWizardByIdSuccess()
    {
        //given
        given(wizardReposistory.findById(1)).willReturn(Optional.of(wizardList.get(0)));
        //when
        Wizard returnedWizard = wizardService.findById(1);
        //then
        assertThat(returnedWizard.getId()).isEqualTo(wizardList.get(0).getId());
        assertThat(returnedWizard.getName()).isEqualTo(wizardList.get(0).getName());
        verify(wizardReposistory,times(1)).findById(1);
    }
    @Test
    void testFindWizardByIdFail()
    {
        //given
        given(wizardReposistory.findById(1)).willReturn(Optional.empty());
        //when and then
        assertThrows(ObjectNotFoundException.class,()->{
            wizardService.findById(1);
        });
    }
    @Test
    void testAddWizardSuccess()
    {
        //given
        Wizard newWizard = new Wizard();
        newWizard.setName("Hermoine");
//        newWizard.setId(4);
        given(wizardReposistory.save(newWizard)).willReturn(newWizard);
        //when
        Wizard createdWizard = wizardService.addWizard(newWizard);
        //then
        assertThat(createdWizard.getId()).isEqualTo(newWizard.getId());
        assertThat(createdWizard.getName()).isEqualTo(newWizard.getName());
        verify(wizardReposistory,times(1)).save(newWizard);
    }
    @Test
    void testUpdateWizardSuccess()
    {
        //given
        Wizard oldWizard = new Wizard();
        oldWizard.setName("Old Wizard");
        oldWizard.setId(4);
        Wizard editedWizard = new Wizard();
        editedWizard.setName("New Wizard");
        editedWizard.setId(4);

        given(wizardReposistory.save(oldWizard)).willReturn(oldWizard);
        given(wizardReposistory.findById(4)).willReturn(Optional.of(oldWizard));
        Wizard updatedWizard = wizardService.updateWizard(4,editedWizard);
        //when

        //then
        assertThat(updatedWizard.getId()).isEqualTo(editedWizard.getId());
        assertThat(updatedWizard.getName()).isEqualTo(editedWizard.getName());
        verify(wizardReposistory,times(1)).save(oldWizard);
        verify(wizardReposistory,times(1)).findById(4);
    }
    @Test
    void testUpdateWizardFailBadRequest()
    {
        //given
        Wizard wizardToBeUpdated = new Wizard();
        wizardToBeUpdated.setId(4);
        wizardToBeUpdated.setName("Wizzard of Ox");
//        given(wizardReposistory.save(wizardToBeUpdated)).willReturn(wizardToBeUpdated);
//        given(wizardReposistory.findById(4)).willReturn(Optional.of(wizardToBeUpdated));
        Wizard nullWizard = new Wizard();
        nullWizard.setName("");

        assertThrows(WizardNameCannotBeNullException.class,()->wizardService.updateWizard(4,nullWizard));
    }
    @Test
    void testWizardUpdateFailNotFound()
    {
        //given
        given(wizardReposistory.findById(4)).willReturn(Optional.empty());
        Wizard newWizard = new Wizard();
        newWizard.setName("Tom Riddle");
        assertThrows(ObjectNotFoundException.class,()->{
           wizardService.updateWizard(4,newWizard);
        });
    }
    @Test
    void testWizardDeleteSuccess()
    {
        Wizard wizardToBeDeleted = new Wizard();
        wizardToBeDeleted.setId(4);
        wizardToBeDeleted.setName("Delete wizard");
        //given
        given(wizardReposistory.findById(4)).willReturn(Optional.of(wizardToBeDeleted));
        //when
        Wizard deletedWizard = wizardService.deleteWizard(4);
        //then
        assertThat(deletedWizard.getId()).isEqualTo(wizardToBeDeleted.getId());
        assertThat(deletedWizard.getName()).isEqualTo(wizardToBeDeleted.getName());
        verify(wizardReposistory,times(1)).findById(4);
    }
    @Test
    void testDeleteWizardIdNotFound()
    {
        //given
        given(wizardReposistory.findById(4)).willThrow(new ObjectNotFoundException("Wizard",4));
        //when and then
        assertThrows(ObjectNotFoundException.class,()->
        {
            wizardService.deleteWizard(4);
        });
    }
}