package com.careergpt.controller;

import com.careergpt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {
    @Autowired
    private UserService userService;

    @PutMapping("/update")
    public String updateProfile(@RequestParam String email,
                                @RequestParam String name,
                                @RequestParam String phone,
                                @RequestParam String location,
                                @RequestParam String address) {
        userService.updateProfile(email, name, phone, location, address);
        return "Profile updated";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String email,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword) {
        boolean success = userService.changePassword(email, currentPassword, newPassword);
        return success ? "Password changed" : "Current password incorrect";
    }

    @PostMapping("/profile-pic")
    public String uploadProfilePic(@RequestParam String email, @RequestParam("file") MultipartFile file) {
        try {
            userService.updateProfilePic(email, file);
            return "Profile picture updated";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update profile picture";
        }
    }
}