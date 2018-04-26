package com.nglinx.pulse.models;

import java.util.List;

public class GroupModel {

    private String id;

    private String name;

    private String ownerUuid;

    private String groupIcon;

    private String status;

    private List<GroupMemberModel> members;

    public GroupModel() {
        super();
    }

    public GroupModel(String groupName, String groupIcon,
                      String status) {
        super();
        this.name = groupName;
        this.groupIcon = groupIcon;
        this.status = status;
    }

    public GroupModel(String groupName, String groupIcon,
                      String status, List<GroupMemberModel> members) {
        super();
        this.name = groupName;
        this.groupIcon = groupIcon;
        this.status = status;
        this.members = members;
    }


    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GroupMemberModel> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMemberModel> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUuid() {
        return ownerUuid;
    }

    public void setOwnerUuid(String ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

}
