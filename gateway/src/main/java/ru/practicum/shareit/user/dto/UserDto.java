package ru.practicum.shareit.user.dto;

import lombok.*;

import ru.practicum.shareit.user.Create;
import ru.practicum.shareit.user.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(groups = {Create.class})
    private String name;

    @NotEmpty(groups = {Create.class})
    @Email(groups = {Create.class, Update.class})
    private String email;
}
