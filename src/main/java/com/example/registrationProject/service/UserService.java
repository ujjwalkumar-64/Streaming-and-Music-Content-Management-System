package com.example.registrationProject.service;

import com.example.registrationProject.entity.Role;
import com.example.registrationProject.request.PermissionUpdate;
import com.example.registrationProject.request.UserRequest;
import com.example.registrationProject.response.DTO.UserCountResponseDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    UserResponse registration(UserRequest userRequest);
    UserDetails loadUserByUsername(String email);
    UserResponse getUserById(Long id);
    UserResponse getUserProfile();
    void validateOtp(String email,String otp);
    UserResponse updateProfile(UserRequest userRequest);
    void deleteAccount(UserRequest userRequest);
    void resendOtp(String email);
    void resetPassword(UserRequest userRequest);
    void updateRole(String email, String role);
    void updatePermission(PermissionUpdate permissionUpdate);

    List<UserDto> getAllUsers();

    UserCountResponseDto countAllUsers();

}
