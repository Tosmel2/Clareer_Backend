package com.careergpt.service;

import com.careergpt.model.PasswordResetToken;
import com.careergpt.model.User;
import com.careergpt.repository.PasswordResetTokenRepository;
import com.careergpt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createPasswordResetToken(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return;

        // Remove any existing tokens for this user
        tokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, user);
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(email, token);
    }

    public boolean validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> prt = tokenRepository.findByToken(token);
        return prt.isPresent() && !prt.get().isExpired();
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> prtOpt = tokenRepository.findByToken(token);
        if (prtOpt.isEmpty() || prtOpt.get().isExpired()) return;

        PasswordResetToken prt = prtOpt.get();
        User user = prt.getUser();
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(prt);
    }

    @Transactional
    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void deleteExpiredTokens() {
        List<PasswordResetToken> expiredTokens = tokenRepository.findAll().stream()
                .filter(PasswordResetToken::isExpired)
                .toList();

        tokenRepository.deleteAll(expiredTokens);
    }
}