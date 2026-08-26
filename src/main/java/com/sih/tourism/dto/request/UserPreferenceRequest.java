package com.sih.tourism.dto.request;

import com.sih.tourism.entity.BudgetLevel;

public class UserPreferenceRequest {

    private String interests; // comma-separated for MVP simplicity
    private BudgetLevel budgetLevel;
    private String preferredCategory;

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
