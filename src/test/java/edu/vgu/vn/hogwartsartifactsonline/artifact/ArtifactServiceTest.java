package edu.vgu.vn.hogwartsartifactsonline.artifact;

import edu.vgu.vn.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.vgu.vn.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts = new ArrayList<>();

    @BeforeEach
    void setUp() {
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
    void testFindByIdSuccess() {
        //Given. Arrange inputs and targets. Define the behaviour of Mock object artifactRepository
        /*
          "id": "1250808601744904192",
          "name": "Invisibility Cloak",
          "description": "An invisibility cloak is used to make the wearer invisible.",
          "imageUrl": "ImageUrl",
         */
        Artifact a = new Artifact();
        a.setId("1250808601744904192");
        a.setName("Invisibility Cloak");
        a.setDescription("An invisibility cloak is used to make the wearer invisible.");
        a.setImageUrl("ImageUrl");

        Wizard n = new Wizard();
        n.setId(1);
        n.setName("Naruto");



        a.setOwner(n);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(a));

        //When. Act on the target behavior. When steps should cover the method to be tested.


        Artifact returnedArtifact = artifactService.findById("1250808601744904192");

        //Then. Assert expected outcomes.

        assertThat(returnedArtifact.getId()).isEqualTo(a.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(a.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(a.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(a.getImageUrl());
        verify(artifactRepository,times(1)).findById("1250808601744904192");
    }
    @Test
    void testFindByIdNotFound()
    {
        //Given

        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        //When
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        //Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with id 1250808601744904192");
        verify(artifactRepository,times(1)).findById("1250808601744904192");


    }
    @Test
    void testFindAllSuccess()
    {
        //given
        given(artifactRepository.findAll()).willReturn(artifacts);

        //when
        List<Artifact> actualArtifacts = artifactService.findAll();

        //then
        assertThat(actualArtifacts.size()).isEqualTo(artifacts.size());
        verify(artifactRepository,times(1)).findAll();
    }


    @Test
    void testSaveSuccess()
    {
        //Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Nigga Artifact");
        newArtifact.setDescription("Wearer get a pass to call some other people 'nigga' ");
        newArtifact.setImageUrl("ImageUrl...");
        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);
        //When
        Artifact savedArtifact = artifactService.save(newArtifact);

        //Then
        assertThat(savedArtifact.getId()).isEqualTo(newArtifact.getId());
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository,times(1)).save(newArtifact);
    }
    @Test
    void testUpdateSuccess()
    {
        //Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("1250808601744904192");
        oldArtifact.setName("Nigga Artifact");
        oldArtifact.setDescription("Wearer get a pass to call some other people 'nigga' ");
        oldArtifact.setImageUrl("ImageUrl...");


        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Nigga Artifact");
        update.setDescription("New description");
        update.setImageUrl("ImageUrl...");


        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
        //When
        Artifact updatedArtifact = artifactService.updateArtifact(update,"1250808601744904192");

        //Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());
        verify(artifactRepository,times(1)).findById("1250808601744904192");
        verify(artifactRepository,times(1)).save(oldArtifact);

    }
    @Test
    void testUpdateNotFound()
    {
        //Given
        Artifact update = new Artifact();
        update.setId("1250808601744904192");
        update.setName("Nigga Artifact");
        update.setDescription("New description");
        update.setImageUrl("ImageUrl...");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
        //When
        assertThrows(ArtifactNotFoundException.class,()->
        {
            artifactService.updateArtifact(update,"1250808601744904192");
        });

        //Then
    }
    @Test
    void testDeleteArtifactSuccess()
    {
        //given
        Artifact currentArtifact = new Artifact();
        currentArtifact.setId("1250808601744904192");
        currentArtifact.setName("Nigga Artifact");
        currentArtifact.setDescription("New description");
        currentArtifact.setImageUrl("ImageUrl...");

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(currentArtifact));
        //when
        Artifact deletedArtifact = artifactService.deleteArtifact("1250808601744904192");
        //then
        assertThat(deletedArtifact.getId()).isEqualTo(currentArtifact.getId());
        assertThat(deletedArtifact.getName()).isEqualTo(currentArtifact.getName());
        assertThat(deletedArtifact.getDescription()).isEqualTo(currentArtifact.getDescription());
    }
    @Test
    void testDeleteArtifactFail()
    {
        //given
        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.empty());
        //when
        Throwable thrown = catchThrowable(()->{
           artifactService.deleteArtifact("1250808601744904192");
        });
        //then
        assertThat(thrown).isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with id 1250808601744904192");
    }
}