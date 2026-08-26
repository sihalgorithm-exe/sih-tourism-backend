package com.sih.tourism.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_preferences")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one, FK lives here (owning side). Unique enforces one preference row per user.
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Comma-separated simple tags for MVP, e.g. "adventure,heritage,nature"
    @Column(name = "interests")
    private String interests;

    @Column(name = "budget_level")
    @Enumerated(EnumType.STRING)
    private BudgetLevel budgetLevel;

    @Column(name = "preferred_category")
    private String preferredCategory;

    public UserPreference() {
    }

    public UserPreference(User user, String interests, BudgetLevel budgetLevel, String preferredCategory) {
        this.user = user;
        this.interests = interests;
        this.budgetLevel = budgetLevel;
        this.preferredCategory = preferredCategory;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public BudgetLevel getBudgetLevel() {
        return budgetLevel;
    }

    public void setBudgetLevel(BudgetLevel budgetLevel) {
        this.budgetLevel = budgetLevel;
    }

    public String getPreferredCategory() {
        return preferredCategory;
    }

    public void setPreferredCategory(String preferredCategory) {
        this.preferredCategory = preferredCategory;
    }
}
