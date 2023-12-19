package edu.vgu.vn.hogwartsartifactsonline.HogwartsUser;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HogwartsUserDtoToHogwartsUserConverter implements Converter<HogwartsUserDto,HogwartsUser> {
    @Override
    public HogwartsUser convert(HogwartsUserDto source) {
        HogwartsUser hogwartsUser = new HogwartsUser();
        hogwartsUser.setId(source.id());
        hogwartsUser.setUsername(source.username());
        hogwartsUser.setRoles(source.roles());
        hogwartsUser.setEnable(source.enable());
        return hogwartsUser;
    }
}
