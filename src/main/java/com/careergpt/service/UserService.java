package com.careergpt.service;

import com.careergpt.model.User;
import com.careergpt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User updateProfile(String email, String name, String phone, String location, String address) {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setName(name);
        user.setPhone(phone);
        user.setLocation(location);
        user.setAddress(address);
        return userRepository.save(user);
    }

    public boolean changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow();
        if (passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void updateProfilePic(String email, MultipartFile file) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow();
        user.setProfilePic(file.getBytes());
        userRepository.save(user);
    }
}