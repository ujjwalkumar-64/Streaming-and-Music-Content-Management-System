package com.example.registrationProject.request;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PermissionUpdate {
    String email;
    List<Integer> permissionIds;
}
