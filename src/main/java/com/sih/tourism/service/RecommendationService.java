package com.sih.tourism.service;

import com.sih.tourism.entity.Destination;
import com.sih.tourism.entity.UserPreference;
import com.sih.tourism.repository.DestinationRepository;
import com.sih.tourism.repository.UserPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
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
     * Rule-based recommendation: NOT machine learning.
     *
     * Filters destinations by whether any of the user's selected interests
     * appears in the destination's category/subcategory/interests/
     * experienceTypes/moodTags text, then ranks by popularity score.
     *
     * If the user has no preferences saved yet, returns all destinations
     * sorted by popularity score.
     */
    public List<Destination> recommendForUser(Long userId) {

        List<Destination> all = destinationRepository.findAll();

        UserPreference preference =
                userPreferenceRepository.findByUserId(userId).orElse(null);

        if (preference == null
                || preference.getInterests() == null
                || preference.getInterests().isBlank()) {
            return all.stream()
                    .sorted(Comparator.comparing(
                            Destination::getPopularityScore,
                            Comparator.nullsLast(Comparator.reverseOrder())
                    ))
                    .toList();
        }

        List<String> userInterests = Arrays.stream(preference.getInterests().split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toLowerCase)
                .toList();

        return all.stream()
                .filter(d -> matchesInterests(d, userInterests))
                .sorted(Comparator.comparing(
                        Destination::getPopularityScore,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
    }

    private boolean matchesInterests(Destination d, List<String> userInterests) {
        String haystack = String.join(" | ",
                safe(d.getCategory()), safe(d.getSubcategory()), safe(d.getInterests()),
                safe(d.getExperienceTypes()), safe(d.getMoodTags())
        ).toLowerCase();

        return userInterests.stream().anyMatch(haystack::contains);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}