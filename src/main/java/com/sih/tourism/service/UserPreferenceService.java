package com.sih.tourism.service;

import com.sih.tourism.dto.request.UserPreferenceRequest;
import com.sih.tourism.entity.User;
import com.sih.tourism.entity.UserPreference;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.repository.UserPreferenceRepository;
import com.sih.tourism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserPreferenceService(UserPreferenceRepository userPreferenceRepository, UserRepository userRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
        this.userRepository = userRepository;
    }

    public UserPreference getForUser(Long userId) {
        return userPreferenceRepository.findByUserId(userId)
                .orElse(null); // no preferences set yet is a valid state, not an error
    }

    public UserPreference upsert(Long userId, UserPreferenceRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElse(new UserPreference());

        preference.setUser(user);
        preference.setInterests(request.getInterests());
        preference.setBudgetLevel(request.getBudgetLevel());
        preference.setPreferredCategory(request.getPreferredCategory());

        return userPreferenceRepository.save(preference);
    }
}
