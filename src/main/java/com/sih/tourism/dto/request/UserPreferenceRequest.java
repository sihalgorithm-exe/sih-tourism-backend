package com.sih.tourism.dto.request;

import com.sih.tourism.entity.BudgetLevel;
import com.sih.tourism.entity.TravelingWith;

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

    private String mood;
    private TravelingWith travelingWith;

    public String getMood() { 
        return mood; 
    }
    
    public void setMood(String mood) { 
        this.mood = mood; 
    
    }
    
    public TravelingWith getTravelingWith() { 
        return travelingWith; 
    }
    
    public void setTravelingWith(TravelingWith travelingWith) { 
        this.travelingWith = travelingWith; 
    }
}
