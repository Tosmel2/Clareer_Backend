package com.careergpt.service;

import com.careergpt.model.Resume;
import com.careergpt.model.User;
import com.careergpt.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeService {
    @Autowired
    private ResumeRepository resumeRepository;

    public Resume uploadResume(MultipartFile file, User user) throws Exception {
        Resume resume = new Resume();
        resume.setFilename(file.getOriginalFilename());
        resume.setData(file.getBytes());
        resume.setUser(user);
        return resumeRepository.save(resume);
    }
}