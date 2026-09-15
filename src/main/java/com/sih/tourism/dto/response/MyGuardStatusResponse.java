package com.sih.tourism.dto.response;

public class MyGuardStatusResponse {

    private String guardStatus;
    private Double myLatitude;
    private Double myLongitude;
    private Double leaderLatitude;
    private Double leaderLongitude;

    public MyGuardStatusResponse(String guardStatus, Double myLatitude, Double myLongitude,
                                  Double leaderLatitude, Double leaderLongitude) {
        this.guardStatus = guardStatus;
        this.myLatitude = myLatitude;
        this.myLongitude = myLongitude;
        this.leaderLatitude = leaderLatitude;
        this.leaderLongitude = leaderLongitude;
    }

    public String getGuardStatus() { return guardStatus; }
    public Double getMyLatitude() { return myLatitude; }
    public Double getMyLongitude() { return myLongitude; }
    public Double getLeaderLatitude() { return leaderLatitude; }
    public Double getLeaderLongitude() { return leaderLongitude; }
}