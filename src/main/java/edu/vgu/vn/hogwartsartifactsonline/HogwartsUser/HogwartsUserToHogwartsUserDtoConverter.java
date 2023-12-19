package edu.vgu.vn.hogwartsartifactsonline.HogwartsUser;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class HogwartsUserToHogwartsUserDtoConverter implements Converter<HogwartsUser,HogwartsUserDto> {

    @Override
    public HogwartsUserDto convert(HogwartsUser source) {
        return new HogwartsUserDto(source.getId(), source.getUsername(), source.isEnable(),source.getRoles());
    }
}
