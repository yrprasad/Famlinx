package com.nglinx.pulse.models;

import java.util.List;

public class SettingsModel {

    public class Speed {
        boolean enabled;
        Integer limit;

        public Speed() {
            enabled = false;
            limit = 0;
        }

        public Speed(boolean enabled, Integer limit) {
            super();
            this.enabled = enabled;
            this.limit = limit;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getLimit() {
            return limit;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }
    }

    ;

    public class Fence {
        boolean enabled;
        String id;

        public Fence()
        {
            enabled = false;
            id = null;
        }

        public Fence(boolean enabled, String id) {
            super();
            this.enabled = enabled;
            this.id = id;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Fence [enabled=" + enabled + ", id=" + id + "]";
        }
    }

    ;

    // Can be owner of any group.
    private String group_owner_uuid;

    private String name;

    private String member_uuid;

    private Speed speed;

    private Fence fence;

    private boolean loop;

    private boolean tamper;

    private boolean lowBattery;

    private List<String> notifyUsers;

    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }

    public Fence getFence() {
        return fence;
    }

    public void setFence(Fence fence) {
        this.fence = fence;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public boolean isTamper() {
        return tamper;
    }

    public void setTamper(boolean tamper) {
        this.tamper = tamper;
    }

    public boolean isLowBattery() {
        return lowBattery;
    }

    public void setLowBattery(boolean lowbattery) {
        this.lowBattery = lowbattery;
    }

    public List<String> getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(List<String> notifyUsers) {
        this.notifyUsers = notifyUsers;
    }

    public String getGroup_owner_uuid() {
        return group_owner_uuid;
    }

    public void setGroup_owner_uuid(String group_owner_uuid) {
        this.group_owner_uuid = group_owner_uuid;
    }

    public String getMember_uuid() {
        return member_uuid;
    }

    public void setMember_uuid(String member_uuid) {
        this.member_uuid = member_uuid;
    }

    public SettingsModel() {
        super();
        speed = new Speed();
        fence = new Fence();
    }

    public SettingsModel(String group_owner_uuid, String member_uuid,
                         Speed speed, Fence fence, boolean loop, boolean tamper,
                         boolean lowbattery, List<String> notifyUsers) {
        super();
        this.group_owner_uuid = group_owner_uuid;
        this.member_uuid = member_uuid;
        this.speed = speed;
        this.fence = fence;
        this.loop = loop;
        this.tamper = tamper;
        this.lowBattery = lowbattery;
        this.notifyUsers = notifyUsers;
    }

    @Override
    public String toString() {
        return "SettingsModel [group_owner_uuid=" + group_owner_uuid
                + ", member_uuid=" + member_uuid + ", speed=" + speed
                + ", fence=" + fence + ", loop=" + loop + ", tamper=" + tamper
                + ", lowbattery=" + lowBattery + ", notifyUsers=" + notifyUsers
                + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
