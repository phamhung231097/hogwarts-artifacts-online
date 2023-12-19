package edu.vgu.vn.hogwartsartifactsonline.HogwartsUser;

import edu.vgu.vn.hogwartsartifactsonline.exception.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HogwartsUserService {
    private final HogwartsUserRepository hogwartsUserRepository;


    public HogwartsUserService(HogwartsUserRepository hogwartsUserRepository) {
        this.hogwartsUserRepository = hogwartsUserRepository;
    }
    public HogwartsUser addUser(HogwartsUser hogwartsUser)
    {
        return hogwartsUserRepository.save(hogwartsUser);
    }
    public List<HogwartsUser> findAllUsers()
    {
        return hogwartsUserRepository.findAll();
    }
    public HogwartsUser findUserById(Integer userId)
    {
        return hogwartsUserRepository.findById(userId)
                .orElseThrow(()->
                        new ObjectNotFoundException("Hogwarts User",userId));
    }
    public HogwartsUser updateUser(HogwartsUser userToBeUpdated,Integer userId)
    {
         return hogwartsUserRepository.findById(userId)
                .map(userNeedToBeUpdated->{
                    userNeedToBeUpdated.setId(userId);
                    userNeedToBeUpdated.setUsername(userToBeUpdated.getUsername());
                    userNeedToBeUpdated.setRoles(userToBeUpdated.getRoles());
                    userNeedToBeUpdated.setEnable(userToBeUpdated.isEnable());
                    return userNeedToBeUpdated;
                })
                .orElseThrow(
                () -> new ObjectNotFoundException("Hogwarts User",userId)
        );
    }
    public void deleteUser(Integer userId)
    {
         HogwartsUser userToBeDeleted = hogwartsUserRepository.findById(userId).orElseThrow(
                 () -> new ObjectNotFoundException("Hogwarts User",userId)
         );
         hogwartsUserRepository.delete(userToBeDeleted);
    }
}
