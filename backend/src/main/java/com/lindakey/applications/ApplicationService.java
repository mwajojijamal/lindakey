package com.lindakey.applications;

import com.lindakey.users.User;
import com.lindakey.users.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public Application registerApplication(Long userId, String name, String environment, String upstreamTargetUrl) {
        // Locate the developer in the database first
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User ID not found."));

        Application newApp = new Application(owner, name, environment, upstreamTargetUrl);
        return applicationRepository.save(newApp);
    }
}