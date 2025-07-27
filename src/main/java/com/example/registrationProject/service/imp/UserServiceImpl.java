package com.example.registrationProject.service.imp;

import com.example.registrationProject.entity.*;
import com.example.registrationProject.exception.CustomException;
import com.example.registrationProject.repository.*;
import com.example.registrationProject.request.PermissionUpdate;
import com.example.registrationProject.request.UserRequest;
import com.example.registrationProject.response.DTO.UserCountResponseDto;
import com.example.registrationProject.response.DTO.UserDto;
import com.example.registrationProject.response.UserResponse;
import com.example.registrationProject.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TempUserRepository tempUserRepository;


    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private EmailServiceImpl emailService;
    
    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PodcasterRepository podcasterRepository;


    private String generateOTP(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder otp = new StringBuilder();
        for(int i=0;i<6;i++){
            int index= random.nextInt(str.length());
            otp.append(str.charAt(index));
        }

        return otp.toString();
    }

    private void expireOtp(String email,int time){
        OTP otpDetail = otpRepository.findByEmailAndStatus(email,Status.active).orElseThrow(()->new CustomException("User not found or already expired"));
        Timer timer = new Timer();
        TimerTask task= new TimerTask(){
            @Override
            public void run(){
                otpDetail.setStatus(Status.inactive);
                otpRepository.save(otpDetail);
                timer.cancel();
            }
        };

        timer.schedule(task,time);
    }
    
    private void sendOtp(String email){
        String otp= generateOTP();
        OTP oldDetail=otpRepository.findByEmailAndStatus(email,Status.active).orElse(null);
        if(oldDetail != null){
           oldDetail.setStatus(Status.inactive);
           otpRepository.save(oldDetail);
        }

        OTP otpDetail= new OTP();

        otpDetail.setEmail(email);
        otpDetail.setOtp(otp);
        otpDetail.setStatus(Status.active);
        otpRepository.save(otpDetail);
        sendMail(email,otp);
        expireOtp(email,5*60*1000);

    }

    private void sendMail(String email,String otp){
        EmailDetail emailDetail = new EmailDetail();

        emailDetail.setRecipientEmail(email);
        emailDetail.setSubject("Registration Successful");
        emailDetail.setMessage("You Register Successfully!\n OTP: "+otp+" \n OTP Valid only for 5min\n Verify Otp By clicking Link: http://localhost:8080/otp/validate?email=" +email+ "&otp=" +otp);
        emailService.sendSimpleMail(emailDetail);
    }

    @Override
    public UserResponse registration(UserRequest userRequest){
        String hashPassword =new BCryptPasswordEncoder().encode(userRequest.getPassword());
        userRequest.setPassword(hashPassword);

        TempUser user = new TempUser();

        user.setEmail(userRequest.getEmail());
        user.setPassword(hashPassword);
        user.setFullName(userRequest.getFullName());
        user.setGender(userRequest.getGender());
        user.setDob(userRequest.getDob());

       
        try{
            TempUser savedUser = tempUserRepository.save(user);
            sendOtp(userRequest.getEmail());
            return new UserResponse(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getStatus(),
                    savedUser.getGender(),
                    savedUser.getDob(),
                    savedUser.getCreatedAt(),
                    savedUser.getUpdatedAt()

            );

        }
        catch(Exception e){
            throw new CustomException(e.getMessage());
        }

    }

    @Override
    public void validateOtp(String email, String otp) {

        OTP otpDetail = otpRepository.findByEmailAndStatus(email,Status.active).orElseThrow(()->new CustomException("User not found or otp already expired"));

        if(!otpDetail.getOtp().equals(otp)){
            throw new CustomException("Invalid OTP");
        }
        else{
            otpDetail.setStatus(Status.inactive);
            otpRepository.save(otpDetail);
            TempUser tempUser= tempUserRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found"));

            if(tempUser.getStatus() == (Status.inactive))
                tempUser.setStatus(Status.active);
            else
                throw new CustomException("User already active");

            User user= new User();
            user.setEmail(tempUser.getEmail());
            user.setPassword(tempUser.getPassword());
            user.setFullName(tempUser.getFullName());
            user.setGender(tempUser.getGender());
            user.setDob(tempUser.getDob());
            user.setStatus(Status.active);

            Role role = roleRepository.findbyRole("ROLE_USER");
            user.setRole(role);


            try{
                userRepository.save(user);
                tempUserRepository.save(tempUser);
            }
            catch(Exception e){
                throw new CustomException(e.getMessage());
            }

        }

    }

    @Override
    public void resendOtp(String email) throws CustomException{
       sendOtp(email);
    }


    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = userRepository.findByEmail(email).orElseThrow(()->new CustomException("User not found"));
         if(user.getStatus()== Status.inactive){
             throw new CustomException("User is not varified");
         }else if(user.getRole().getStatus()== Status.inactive){
             throw new CustomException("User role is not active");
         }
         return user;

    }

    @Override
    public  UserResponse getUserById(Long id){
        User user= userRepository.findById(id).orElse(null);
        if(user==null){
            throw new CustomException("Invalid id");
        }
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus(),
                user.getGender(),
                user.getDob(),
                user.getCreatedAt(),
                user.getUpdatedAt()

        );
    }

    @Override
    public UserResponse getUserProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail = (UserDetails) auth.getPrincipal();
        String email = userDetail.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));


        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus(),
                user.getGender(),
                user.getImageUrl(),
                user.getRole().getRole(),
                user.getDob(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

    }

    public String uploadPhoto(MultipartFile file) throws IOException {
        String filePath= System.getProperty("user.dir")+"\\pictures"+File.separator+ UUID.randomUUID().toString()+file.getOriginalFilename();
        FileOutputStream fout= new FileOutputStream(filePath);
        fout.write(file.getBytes());
        fout.flush();
        fout.close();
        return filePath;
    }

    @Override
    public UserResponse updateProfile(UserRequest userRequest){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetail=(UserDetails) auth.getPrincipal();
        User user= userRepository.findByEmail(userDetail.getUsername()).orElseThrow(()->new CustomException("User not found"));

        try{
            String image_url= userRequest.getFile() !=null ? uploadPhoto(userRequest.getFile()) : user.getImageUrl();
            user.setImageUrl(image_url);
            user.setFullName(userRequest.getFullName()!= null ? userRequest.getFullName(): user.getFullName());
            user.setGender(userRequest.getGender() != null ? userRequest.getGender(): user.getGender());
            user.setDob(userRequest.getDob() != null ? userRequest.getDob(): user.getDob());
            
            User savedUser=userRepository.save(user);
            return new UserResponse(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getStatus(),
                    savedUser.getGender(),
                    savedUser.getImageUrl(),

                    savedUser.getDob(),
                    savedUser.getCreatedAt(),
                    savedUser.getUpdatedAt()
            );
        }
        catch(Exception e){
            throw new CustomException("Error while updating profile: "+e.getMessage());
        }
        
        
    }

    @Override
    public  void deleteAccount(UserRequest userRequest){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails=(UserDetails) auth.getPrincipal();

        String email=userDetails.getUsername();
        User user= userRepository.findByEmail(email).orElseThrow(()->new CustomException("User not found"));
         PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
       boolean validatePassword= passwordEncoder.matches(userRequest.getPassword(),user.getPassword());
        if(validatePassword){
            userRepository.deleteByEmail(user.getEmail());
            tempUserRepository.deleteByEmail(user.getEmail());
        }
        else{
            throw new CustomException("invalid password");
        }

    }

    @Override
    public void resetPassword(UserRequest userRequest){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) auth.getPrincipal();

        String email= userDetails.getUsername();

        String oldPassword=userRequest.getPassword();
        String newPassword=userRequest.getNewPassword();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String hashedNewPassword= passwordEncoder.encode(newPassword);

        User user= userRepository.findByEmail(email).orElseThrow(()->new CustomException("User not found or invalid email"));

        boolean validatePassword= passwordEncoder.matches(oldPassword,user.getPassword());

        if(validatePassword){
            user.setPassword(hashedNewPassword);
            userRepository.save(user);
        }
        else{
            throw new CustomException("invalid password");
        }

    }

    @Override
    public void updateRole(String email,String newRole){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) auth.getPrincipal();
        if(userDetails.getUsername().equals(email)){
            throw new CustomException("SuperAdmin can't change their role");
        }
        User user= userRepository.findByEmail(email).orElseThrow(()->new CustomException("User not found"));
        Role role= roleRepository.findbyRole(newRole);
        user.setRole(role);

        User savedUser=userRepository.save(user);


        switch (newRole) {
            case "ROLE_ARTIST" -> {
                Artist artist = new Artist();

                artist.setArtistName(savedUser.getFullName());
                artist.setProfilePic(savedUser.getImageUrl());
                artist.setStatus(savedUser.getStatus());
                artist.setUser(savedUser);
                artistRepository.save(artist);
            }
            case "ROLE_LABEL" -> {
                Label label = new Label();
                label.setName(savedUser.getFullName());
                label.setOwnerUser(savedUser);
                label.setStatus(savedUser.getStatus());
                labelRepository.save(label);
            }
            case "ROLE_PODCASTER" -> {
                Podcaster pod = new Podcaster();
                pod.setStatus(savedUser.getStatus());
                pod.setUser(savedUser);
                pod.setPodcasterName(savedUser.getFullName());
                pod.setProfilePic(savedUser.getImageUrl());

                podcasterRepository.save(pod);
            }
        }




    };

    @Override
    public  void updatePermission(PermissionUpdate permissionUpdate){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails= (UserDetails) auth.getPrincipal();
        if(userDetails.getUsername().equals(permissionUpdate.getEmail())){
            throw new CustomException("SuperAdmin can't change their permission");
        }

        User user= userRepository.findByEmail(permissionUpdate.getEmail()).orElseThrow(()->new CustomException("User not found"));
        List<Permission> permissions = permissionUpdate.getPermissionIds().stream()
                .map(permissionId -> permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new CustomException("Permission not found")))
                .collect(Collectors.toList());
        user.setUserPermissions(permissions);
        userRepository.save(user);

    }

    @Override
    public List<UserDto> getAllUsers(){
        List<User> users= userRepository.findAll();
        List<UserDto> userDtos= new ArrayList<>();
        users.forEach(user->{
           userDtos.add( new UserDto().builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .fullName(user.getFullName())
                    .gender(user.getGender())
                    .userRole(user.getRole().getRole())
                    .status(user.getStatus())
                   .joiningDate(user.getCreatedAt())
                    .build());
        });

        return userDtos;
    }

    @Override
    public UserCountResponseDto countAllUsers(){
        Long totalUsers= userRepository.countAllUsers();
        Long totalActiveUser= userRepository.countActiveUsers(Status.active);
        Long totalInActiveUser= userRepository.countInActiveUsers(Status.inactive);

         return new UserCountResponseDto().builder()
                 .totalUsers(totalUsers)
                 .activeUsers(totalActiveUser)
                 .inactiveUsers(totalInActiveUser)
                 .build();

    }


}
