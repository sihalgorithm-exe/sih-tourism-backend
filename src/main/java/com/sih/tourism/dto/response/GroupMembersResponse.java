package com.sih.tourism.dto.response;

import java.util.List;

public class GroupMembersResponse {

    private Long groupId;
    private List<GroupMemberResponse> members;

    public GroupMembersResponse(Long groupId, List<GroupMemberResponse> members) {
        this.groupId = groupId;
        this.members = members;
    }

    public Long getGroupId() {
        return groupId;
    }

    public List<GroupMemberResponse> getMembers() {
        return members;
    }
}