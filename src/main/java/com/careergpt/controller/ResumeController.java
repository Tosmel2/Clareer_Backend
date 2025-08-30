package com.careergpt.controller;

import com.careergpt.model.User;
import com.careergpt.repository.UserRepository;
import com.careergpt.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public String uploadResume(@RequestParam("file") MultipartFile file, @RequestParam("email") String email) {
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user == null) return "User not found";
            resumeService.uploadResume(file, user);
            return "Resume uploaded successfully";
        } catch (Exception e) {
            return "Upload failed";
        }
    }
}