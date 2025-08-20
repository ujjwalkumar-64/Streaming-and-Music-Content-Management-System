package com.example.registrationProject.controller;

import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.request.PermissionUpdate;
import com.example.registrationProject.request.UserRequest;
import com.example.registrationProject.response.UserResponse;
import com.example.registrationProject.service.TrackService;
import com.example.registrationProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private TrackService trackService;



    @PostMapping(value = "/register")
    public ResponseEntity<Object> register(@RequestBody UserRequest userRequest){
        try{
            return ResponseEntity.ok(userService.registration(userRequest));
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable(value = "id") Long id){
        try{

            return ResponseEntity.ok(userService.getUserById(id));
        }
        catch(CustomException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/user/profile")
    public ResponseEntity<UserResponse> getUserByEmail(){
        try{
            return ResponseEntity.ok(userService.getUserProfile());
        }
        catch(CustomException e){
            return ResponseEntity.notFound().build();
        }

    }

    @GetMapping(value="/otp/validate")
    public ResponseEntity<Object> validateOtp(@RequestParam(value="email") String email,@RequestParam(value = "otp")  String otp){
        try{
            userService.validateOtp(email,otp);
            return ResponseEntity.ok().body("otp varified successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "/otp/resend")
    public ResponseEntity<Object> resendOtp(@RequestBody UserRequest userRequest){
        try{
            userService.resendOtp(userRequest.getEmail());
            return ResponseEntity.ok().body("otp resend successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/user/update/profile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> updateUserProfile(@ModelAttribute  UserRequest userRequest){
        try{
        return  ResponseEntity.ok(userService.updateProfile(userRequest));
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/user/update/password")
    public ResponseEntity<Object> updateUserPassword(@RequestBody UserRequest userRequest){
        try{
            userService.resetPassword(userRequest);
            return ResponseEntity.ok().body("password reset successfully");
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping(value = "/user/deleteAccount")
    public ResponseEntity<Object> deleteAccount(@RequestBody UserRequest userRequest){
        try{
            userService.deleteAccount(userRequest);
            return ResponseEntity.ok("User deleted successfully");

        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/user/update/role")
    public ResponseEntity<Object> updateRole(@RequestBody UserRequest statusUpdateRequest){
        try{
            userService.updateRole(statusUpdateRequest.getEmail(),statusUpdateRequest.getRole());
            return ResponseEntity.ok("Role updated successfully");
        }
        catch(CustomException e){
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/user/update/permission")

    public ResponseEntity<Object> updatePermission(@RequestBody PermissionUpdate permissionUpdate){
        try{
            userService.updatePermission(permissionUpdate);
            return ResponseEntity.ok("Permission updated successfully");
        }
        catch (CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/like/tracks")
    public ResponseEntity<Object> getUserLikedTracksByTrackId(){
        try{
            return ResponseEntity.ok(trackService.getAllLikedTracksByUserId());
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/count/like/track/{userId}")
    public ResponseEntity<Object> getUserLikedTracksCountsByTrackId(@PathVariable  Long userId){
        try {
            return ResponseEntity.ok(trackService.countLikedTracksByUserId(userId));
        }
        catch(CustomException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

