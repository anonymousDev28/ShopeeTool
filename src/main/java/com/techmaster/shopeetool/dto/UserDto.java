package com.techmaster.shopeetool.dto;
import com.techmaster.shopeetool.model.Role;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserDto {
    private Integer id;
    private String email;
//    private String password;
    private Set<Role> roles;
}
