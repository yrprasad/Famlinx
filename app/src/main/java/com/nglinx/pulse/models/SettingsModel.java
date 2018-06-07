package com.nglinx.pulse.models;

public class SettingsModel {

    public class NotificationsEnablers {
        boolean speedNotification;
        boolean fenceNotification;
        boolean batteryNotification;

        public NotificationsEnablers() {
            speedNotification = true;
            fenceNotification = true;
            batteryNotification = true;
        }

        public boolean isSpeedNotification() {
            return speedNotification;
        }

        public void setSpeedNotification(boolean speedNotification) {
            this.speedNotification = speedNotification;
        }

        public boolean isFenceNotification() {
            return fenceNotification;
        }

        public void setFenceNotification(boolean fenceNotification) {
            this.fenceNotification = fenceNotification;
        }

        public boolean isBatteryNotification() {
            return batteryNotification;
        }

        public void setBatteryNotification(boolean batteryNotification) {
            this.batteryNotification = batteryNotification;
        }
    }

    public class MinMax {
        boolean enabled;
        Integer min;
        Integer max;

        public MinMax() {
            super();
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getMin() {
            return min;
        }

        public void setMin(Integer min) {
            this.min = min;
        }

        public Integer getMax() {
            return max;
        }

        public void setMax(Integer max) {
            this.max = max;
        }

        @Override
        public String toString() {
            return "MinMax [enabled=" + enabled + ", min=" + min + ", max=" + max + "]";
        }
    }

    public class Speed {
        boolean enabled;
        Integer limit;

        public Speed() {
            super();
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

        @Override
        public String toString() {
            return "Speed [enabled=" + enabled + ", limit=" + limit + "]";
        }
    }

    ;

    public class Fence {
        boolean enabled;
        String id;

        public Fence() {
            super();
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

    private MinMax level;

    private MinMax flow;

    private Speed speed;

    private Fence fence;

    private boolean loop;

    private boolean tamper;

    private boolean lowBattery;

    private String notifyUsers;

    private NotificationsEnablers emailNotifications;

    private NotificationsEnablers phoneNotifications;

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

    public String getNotifyUsers() {
        return notifyUsers;
    }

    public void setNotifyUsers(String notifyUsers) {
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

    public MinMax getLevel() {
        return level;
    }

    public void setLevel(MinMax level) {
        this.level = level;
    }

    public MinMax getFlow() {
        return flow;
    }

    public void setFlow(MinMax flow) {
        this.flow = flow;
    }

    public SettingsModel() {
        super();
        level = new MinMax();
        flow = new MinMax();
        speed = new Speed();
        fence = new Fence();
        emailNotifications = new NotificationsEnablers();
        phoneNotifications = new NotificationsEnablers();
    }

    public SettingsModel(String group_owner_uuid, String member_uuid, Speed speed, Fence fence, boolean loop,
                         boolean tamper, boolean lowbattery, String notifyUsers) {
        super();
        level = new MinMax();
        flow = new MinMax();
        this.group_owner_uuid = group_owner_uuid;
        this.member_uuid = member_uuid;
        this.speed = speed;
        this.fence = fence;
        this.loop = loop;
        this.tamper = tamper;
        this.lowBattery = lowbattery;
        this.notifyUsers = notifyUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NotificationsEnablers getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(NotificationsEnablers emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public NotificationsEnablers getPhoneNotifications() {
        return phoneNotifications;
    }

    public void setPhoneNotifications(NotificationsEnablers phoneNotifications) {
        this.phoneNotifications = phoneNotifications;
    }

    @Override
    public String toString() {
        return "SettingsModel [group_owner_uuid=" + group_owner_uuid + ", name=" + name + ", member_uuid="
                + member_uuid + ", level=" + level + ", flow=" + flow + ", speed=" + speed + ", fence=" + fence
                + ", loop=" + loop + ", tamper=" + tamper + ", lowBattery=" + lowBattery + ", notifyUsers="
                + notifyUsers + "]";
    }
}
