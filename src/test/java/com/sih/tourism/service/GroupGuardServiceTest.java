package com.sih.tourism.service;

import com.sih.tourism.dto.request.LocationUpdateRequest;
import com.sih.tourism.entity.*;
import com.sih.tourism.repository.GroupMemberRepository;
import com.sih.tourism.repository.LocationUpdateRepository;
import com.sih.tourism.repository.SafetyAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupGuardServiceTest {

    @Mock
    private GroupService groupService;

    @Mock
    private GroupMemberRepository groupMemberRepository;

    @Mock
    private LocationUpdateRepository locationUpdateRepository;

    @Mock
    private SafetyAlertRepository safetyAlertRepository;

    @InjectMocks
    private GroupGuardService groupGuardService;

    private User leaderUser;
    private User memberUser;
    private TravelGroup group;
    private GroupMember leaderMembership;
    private GroupMember memberMembership;

    @BeforeEach
    void setUp() {
        leaderUser = new User("Leader", "leader@test.com", "hash", Role.TOURIST);
        ReflectionTestUtils.setField(leaderUser, "id", 1L);

        memberUser = new User("Member", "member@test.com", "hash", Role.TOURIST);
        ReflectionTestUtils.setField(memberUser, "id", 2L);

        group = new TravelGroup("Family Trip", leaderUser, 500.0);
        ReflectionTestUtils.setField(group, "id", 100L);

        leaderMembership = new GroupMember(leaderUser, group);
        ReflectionTestUtils.setField(leaderMembership, "id", 10L);

        memberMembership = new GroupMember(memberUser, group);
        ReflectionTestUtils.setField(memberMembership, "id", 11L);
        memberMembership.setOutOfBounds(false);
    }

    @Test
    void memberExitingRadiusCreatesExactlyOneAlert() {
        when(groupService.getGroupOrThrow(100L)).thenReturn(group);
        when(groupService.getMembershipOrThrow(2L, 100L)).thenReturn(memberMembership);
        when(groupService.isLeader(group, 2L)).thenReturn(false);
        when(groupMemberRepository.findByUserIdAndGroupId(1L, 100L)).thenReturn(Optional.of(leaderMembership));

        // Leader is at (0,0); member submits a location far enough to exceed the 500m radius.
        LocationUpdate leaderLoc = new LocationUpdate(leaderMembership, 0.0, 0.0);
        when(locationUpdateRepository.findFirstByGroupMemberIdOrderByRecordedAtDesc(10L))
                .thenReturn(Optional.of(leaderLoc));
        when(locationUpdateRepository.save(any(LocationUpdate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocationUpdateRequest farRequest = new LocationUpdateRequest();
        farRequest.setLatitude(0.01); // ~1.1km away, outside 500m radius
        farRequest.setLongitude(0.0);

        groupGuardService.submitLocation(100L, 2L, farRequest);

        assertTrue(memberMembership.isOutOfBounds());
        verify(safetyAlertRepository, times(1)).save(any(SafetyAlert.class));
        verify(groupMemberRepository, times(1)).save(memberMembership);
    }

    @Test
    void memberStayingOutsideDoesNotCreateDuplicateAlert() {
        memberMembership.setOutOfBounds(true); // already flagged from a previous exit

        when(groupService.getGroupOrThrow(100L)).thenReturn(group);
        when(groupService.getMembershipOrThrow(2L, 100L)).thenReturn(memberMembership);
        when(groupService.isLeader(group, 2L)).thenReturn(false);
        when(groupMemberRepository.findByUserIdAndGroupId(1L, 100L)).thenReturn(Optional.of(leaderMembership));

        LocationUpdate leaderLoc = new LocationUpdate(leaderMembership, 0.0, 0.0);
        when(locationUpdateRepository.findFirstByGroupMemberIdOrderByRecordedAtDesc(10L))
                .thenReturn(Optional.of(leaderLoc));
        when(locationUpdateRepository.save(any(LocationUpdate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocationUpdateRequest stillFarRequest = new LocationUpdateRequest();
        stillFarRequest.setLatitude(0.02); // still far outside radius
        stillFarRequest.setLongitude(0.0);

        groupGuardService.submitLocation(100L, 2L, stillFarRequest);

        verify(safetyAlertRepository, never()).save(any(SafetyAlert.class));
    }

    @Test
    void memberReturningInsideRadiusResetsFlagWithoutAlert() {
        memberMembership.setOutOfBounds(true);

        when(groupService.getGroupOrThrow(100L)).thenReturn(group);
        when(groupService.getMembershipOrThrow(2L, 100L)).thenReturn(memberMembership);
        when(groupService.isLeader(group, 2L)).thenReturn(false);
        when(groupMemberRepository.findByUserIdAndGroupId(1L, 100L)).thenReturn(Optional.of(leaderMembership));

        LocationUpdate leaderLoc = new LocationUpdate(leaderMembership, 0.0, 0.0);
        when(locationUpdateRepository.findFirstByGroupMemberIdOrderByRecordedAtDesc(10L))
                .thenReturn(Optional.of(leaderLoc));
        when(locationUpdateRepository.save(any(LocationUpdate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocationUpdateRequest closeRequest = new LocationUpdateRequest();
        closeRequest.setLatitude(0.0001); // well within 500m
        closeRequest.setLongitude(0.0);

        groupGuardService.submitLocation(100L, 2L, closeRequest);

        assertFalse(memberMembership.isOutOfBounds());
        verify(safetyAlertRepository, never()).save(any(SafetyAlert.class));
        verify(groupMemberRepository, times(1)).save(memberMembership);
    }

    @Test
    void leaderLocationUpdateNeverTriggersAlertEvaluation() {
        when(groupService.getGroupOrThrow(100L)).thenReturn(group);
        when(groupService.getMembershipOrThrow(1L, 100L)).thenReturn(leaderMembership);
        when(groupService.isLeader(group, 1L)).thenReturn(true);
        when(locationUpdateRepository.save(any(LocationUpdate.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        LocationUpdateRequest leaderRequest = new LocationUpdateRequest();
        leaderRequest.setLatitude(10.0);
        leaderRequest.setLongitude(10.0);

        groupGuardService.submitLocation(100L, 1L, leaderRequest);

        verify(safetyAlertRepository, never()).save(any(SafetyAlert.class));
        verify(groupMemberRepository, never()).findByUserIdAndGroupId(any(), any());
    }
}
