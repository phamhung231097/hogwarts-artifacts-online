package edu.vgu.vn.hogwartsartifactsonline.HogwartsUser;

import edu.vgu.vn.hogwartsartifactsonline.system.Result;
import edu.vgu.vn.hogwartsartifactsonline.system.StatusCode;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class HogwartsUserController {
    private final HogwartsUserService hogwartsUserService;
    private final HogwartsUserDtoToHogwartsUserConverter hogwartsUserDtoToHogwartsUserConverter;
    private final HogwartsUserToHogwartsUserDtoConverter hogwartsUserToHogwartsUserDtoConverter;

    public HogwartsUserController(HogwartsUserService hogwartsUserService,
                                  HogwartsUserDtoToHogwartsUserConverter hogwartsUserDtoToHogwartsUserConverter,
                                  HogwartsUserToHogwartsUserDtoConverter hogwartsUserToHogwartsUserDtoConverter) {
        this.hogwartsUserService = hogwartsUserService;
        this.hogwartsUserDtoToHogwartsUserConverter = hogwartsUserDtoToHogwartsUserConverter;
        this.hogwartsUserToHogwartsUserDtoConverter = hogwartsUserToHogwartsUserDtoConverter;
    }

    @GetMapping
    public Result findAllUsers()
    {

        List<HogwartsUserDto> hogwartsUserDtos =  hogwartsUserService.findAllUsers()
                .stream().map(hogwartsUserToHogwartsUserDtoConverter::convert)
                .collect(Collectors.toList());
        return new Result(true, StatusCode.SUCCESS,"Find All Success",hogwartsUserDtos);
    }
    @GetMapping("/{userId}")
    public Result findUserById(@PathVariable @Valid Integer userId)
    {
        HogwartsUser foundUser = hogwartsUserService.findUserById(userId);
        HogwartsUserDto foundUserDto = hogwartsUserToHogwartsUserDtoConverter.convert(foundUser);
        return new Result(true,StatusCode.SUCCESS,"Find One Success",foundUserDto);
    }
    @PostMapping
    public Result createUser(@RequestBody HogwartsUser userToBeCreated)
    {
        HogwartsUser hogwartsUser = hogwartsUserService.addUser(userToBeCreated);
        HogwartsUserDto createdUserDto = hogwartsUserToHogwartsUserDtoConverter.convert(hogwartsUser);
        return new Result(true,StatusCode.SUCCESS,"Add Success",createdUserDto);
    }
    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable Integer userId, @RequestBody HogwartsUserDto hogwartsUserDto)
    {
        System.out.println(hogwartsUserDto);
        HogwartsUser userToBeUpdated = hogwartsUserDtoToHogwartsUserConverter.convert(hogwartsUserDto);
        System.out.println(userToBeUpdated.getId()+" "+userToBeUpdated.getUsername());
        HogwartsUser updatedUser = hogwartsUserService.updateUser(userToBeUpdated,userId);
        System.out.println(updatedUser.getId()+" "+updatedUser.getUsername());
        HogwartsUserDto updatedUserDto = hogwartsUserToHogwartsUserDtoConverter.convert(updatedUser);
        return new Result(true,StatusCode.SUCCESS,"Update Success",updatedUserDto);
    }
    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable Integer userId)
    {
        hogwartsUserService.deleteUser(userId);
        return new Result(true,StatusCode.SUCCESS,"Delete Success");
    }
}
