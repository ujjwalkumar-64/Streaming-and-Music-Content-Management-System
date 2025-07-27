package com.example.registrationProject.response.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCountResponseDto {
    private Long totalUsers;
    private Long activeUsers;
    private Long inactiveUsers;

}
