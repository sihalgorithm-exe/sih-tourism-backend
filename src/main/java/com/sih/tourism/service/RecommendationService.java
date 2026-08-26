package com.sih.tourism.service;

import com.sih.tourism.entity.Destination;
import com.sih.tourism.entity.UserPreference;
import com.sih.tourism.repository.DestinationRepository;
import com.sih.tourism.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RecommendationService {

    private final DestinationRepository destinationRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public RecommendationService(DestinationRepository destinationRepository,
                                  UserPreferenceRepository userPreferenceRepository) {
        this.destinationRepository = destinationRepository;
        this.userPreferenceRepository = userPreferenceRepository;
    }

    /**
     * Rule-based recommendation: NOT machine learning. Filters destinations by the
     * user's stored budget level and preferred category (when set), and ranks the
     * rest by popularity. If the user has no preferences saved yet, returns all
     * destinations sorted by popularity as a sensible default.
     */
    public List<Destination> recommendForUser(Long userId) {
        List<Destination> all = destinationRepository.findAll();

        UserPreference preference = userPreferenceRepository.findByUserId(userId).orElse(null);

        if (preference == null) {
            return all.stream()
                    .sorted(Comparator.comparing(Destination::getPopularityScore,
                            Comparator.nullsLast(Comparator.reverseOrder())))
                    .toList();
        }

        return all.stream()
                .filter(d -> preference.getBudgetLevel() == null || d.getBudgetLevel() == preference.getBudgetLevel())
                .filter(d -> preference.getPreferredCategory() == null || preference.getPreferredCategory().isBlank()
                        || (d.getCategory() != null
                            && d.getCategory().equalsIgnoreCase(preference.getPreferredCategory())))
                .sorted(Comparator.comparing(Destination::getPopularityScore,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }
}
