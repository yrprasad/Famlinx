package com.nglinx.pulse.utils.retrofit;


import com.nglinx.pulse.models.ChildUserModel;
import com.nglinx.pulse.models.DeviceActivateModel;
import com.nglinx.pulse.models.DeviceModel;
import com.nglinx.pulse.models.DeviceOrderModel;
import com.nglinx.pulse.models.DeviceType;
import com.nglinx.pulse.models.DeviceTypesModel;
import com.nglinx.pulse.models.FenceModel;
import com.nglinx.pulse.models.GroupMemberModel;
import com.nglinx.pulse.models.GroupModel;
import com.nglinx.pulse.models.InviteModel;
import com.nglinx.pulse.models.NotificationModel;
import com.nglinx.pulse.models.RegisterModel;
import com.nglinx.pulse.models.ResponseDto;
import com.nglinx.pulse.models.SettingsModel;
import com.nglinx.pulse.models.UserLoginModel;
import com.nglinx.pulse.models.UserModel;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

public interface ApiEndpointInterface {

    @POST("/authenticate")
    void authenticate(@Body UserLoginModel user, Callback<ResponseDto<UserModel>> cb);

    @PUT("/api/users/{groupOwnerId}/groups/{groupId}/member")
    void inviteMembers(@Path("groupOwnerId") String groupOwnerId, @Path("groupId") String groupId, @Body GroupMemberModel member, Callback<ResponseDto<GroupMemberModel>> cb);

    @DELETE("/api/users/{groupOwnerId}/groups/{groupId}/member/{memberId}")
    void deleteMemerFromGroup(@Path("groupOwnerId") String groupOwnerId, @Path("groupId") String groupId, @Path("memberId") String memberId, Callback<ResponseDto<GroupMemberModel>> cb);

    @GET("/api/users/id/{userId}/basic")
    void getUserInfoWithoutGroups(@Path("userId") String userId, Callback<ResponseDto<UserModel>> cb);

    @GET("/api/users/id/{userId}")
    void getUserDetailsIncludingGroups(@Path("userId") String userId, Callback<ResponseDto<UserModel>> cb);

    @PUT("/api/users/{ownerUuid}/groups")
    void createGroup(@Path("ownerUuid") String ownerUuid, @Body GroupModel groupModel, Callback<ResponseDto<GroupModel>> cb);

    @PUT("/api/register")
    void register(@Body RegisterModel registerModel, Callback<ResponseDto<RegisterModel>> cb);

    @GET("/api/users/name/{userName}")
    void getUserByName(@Path("userName") String userName, Callback<ResponseDto<UserModel>> cb);

    @GET("/api/users/{ownerUuid}/groups")
    void getGroups(@Path("ownerUuid") String ownerUuid, @Query("userType") String userType, Callback<ResponseDto<GroupModel>> cb);

    @GET("/api/users/{ownerUuid}/groups")
    void getGroups(@Path("ownerUuid") String ownerUuid, @Query("latitude") String latitude, @Query("longitude") String longitude, Callback<ResponseDto<GroupModel>> cb);

    @DELETE("/api/users/{ownerUuid}/groups/{groupId}")
    void deleteGroup(@Path("ownerUuid") String ownerUuid, @Path("groupId") String groupId, Callback<ResponseDto<GroupModel>> cb);

    @GET("/api/users/{ownerUuid}/invites")
    void getAllInvites(@Path("ownerUuid") String ownerUuid, Callback<ResponseDto<InviteModel>> cb);

    @GET("/api/users/{ownerUuid}/invite/{targetUserId}/accept")
    void accepInvite(@Path("ownerUuid") String ownerUuid, @Path("targetUserId") String targetUserId, Callback<ResponseDto<InviteModel>> cb);

    @GET("/api/users/{ownerUuid}/invite/{targetUserId}/suspend")
    void suspendInvite(@Path("ownerUuid") String ownerUuid, @Path("targetUserId") String targetUserId, Callback<ResponseDto<InviteModel>> cb);

    @GET("/api/users/{ownerUuid}/invite/{targetUserId}/reject")
    void rejectInvite(@Path("ownerUuid") String ownerUuid, @Path("targetUserId") String targetUserId, Callback<ResponseDto<InviteModel>> cb);

    @GET("/api/users/{ownerUuid}/members/{memberId}/settings")
    void getMemberSettings(@Path("ownerUuid") String ownerUuid, @Path("memberId") String memberId, Callback<ResponseDto<SettingsModel>> cb);

    @POST("/api/users/{ownerUuid}/members/{memberId}/settings")
    void updateMemberSettings(@Path("ownerUuid") String ownerUuid, @Path("memberId") String memberId, @Body SettingsModel settingsModel, Callback<ResponseDto<SettingsModel>> cb);

    @GET("/api/users/{ownerUuid}/fences")
    void listFence(@Path("ownerUuid") String ownerUuid, Callback<ResponseDto<FenceModel>> cb);

    @PUT("/api/users/{ownerUuid}/fences")
    void createFence(@Path("ownerUuid") String ownerUuid, @Body FenceModel fenceModel, Callback<ResponseDto<FenceModel>> cb);

    @GET("/api/users/{ownerUuid}/fences/{fenceId}")
    void readFence(@Path("ownerUuid") String ownerUuid, @Path("fenceId") String fenceId, Callback<ResponseDto<FenceModel>> cb);

    @POST("/api/users/{ownerUuid}/fences/{fenceId}")
    void updateFence(@Path("ownerUuid") String ownerUuid, @Path("fenceId") String fenceId, @Body FenceModel fenceModel, Callback<ResponseDto<FenceModel>> cb);

    @DELETE("/api/users/{ownerUuid}/fences/{fenceId}")
    void deleteFence(@Path("ownerUuid") String ownerUuid, @Path("fenceId") String fenceId, Callback<ResponseDto<FenceModel>> cb);

    @POST("/api/forgotpassword/{username}")
    void forgotPassword(@Path("username") String username, @Body UserModel userModel, Callback<ResponseDto<UserModel>> cb);

    @GET("/logout")
    void logout(Callback<ResponseDto<UserModel>> cb);

    @GET("/api/users/{userId}/groups/{groupId}/members/{memberId}/track")
    void trackMember(@Path("userId") String userId, @Path("groupId") String groupId, @Path("memberId") String memberId, Callback<ResponseDto<GroupMemberModel>> cb);

    @GET("/api/users/{userId}/notifications")
    void getAllNotifications(@Path("userId") String userId, Callback<ResponseDto<NotificationModel>> cb);

    @DELETE("/api/users/{userId}/notifications/{notificationId}")
    void deleteNotification(@Path("userId") String userId, @Path("notificationId") String notificationId, Callback<ResponseDto<NotificationModel>> cb);

    //Device APIs
    @PUT("/api/devices")
    void purchaseDevice(@Query("type") DeviceType type, @Body DeviceModel device, Callback<ResponseDto<DeviceModel>> cb);

    //GET Device Types APIs
    @GET("/api/devices/types")
    void getDeviceTypes(Callback<ResponseDto<DeviceTypesModel>> cb);

    @GET("/api/devices/{deviceId}")
    void getDevice(@Path("deviceId") String deviceId, Callback<ResponseDto<DeviceModel>> cb);

    @POST("/api/devices/{deviceId}")
    void updateDevice(@Path("deviceId") String deviceId, @Body DeviceModel device, Callback<ResponseDto<DeviceModel>> cb);

    @DELETE("/api/devices/{deviceId}")
    void returnDevice(@Path("deviceId") String deviceId, Callback<ResponseDto<DeviceModel>> cb);

    @GET("/api/devices")
    void listMyDevices(Callback<ResponseDto<DeviceModel>> cb);

    @POST("/api/devices/{deviceId}/activate")
    void activateDevice(@Path("deviceId") String deviceId, @Body DeviceActivateModel device, Callback<ResponseDto<DeviceModel>> cb);

    @POST("/api/devices/{deviceId}/deactivate")
    void deactivateDevice(@Path("deviceId") String deviceId, @Body DeviceModel device, Callback<ResponseDto<DeviceModel>> cb);

    @POST("/api/devices/{deviceId}/attach/{userName}")
    void attachDevice(@Path("deviceId") String deviceId, @Path("userName") String userName, @Body DeviceModel device, Callback<ResponseDto<DeviceModel>> cb);

    @POST("/api/devices/{deviceId}/detach")
    void detachDevice(@Path("deviceId") String deviceId, @Body DeviceModel device, Callback<ResponseDto<DeviceModel>> cb);

    //Child Profile related APIs
    @PUT("/api/users/{ownerUuid}/child")
    void createChildUser(@Path("ownerUuid") String ownerUuid, @Body ChildUserModel childUser, Callback<ResponseDto<ChildUserModel>> cb);

    @GET("/api/users/{ownerUuid}/child")
    void getAllChildUser(@Path("ownerUuid") String ownerUuid, Callback<ResponseDto<ChildUserModel>> cb);

    @GET("/api/users/{ownerUuid}/child/{childUserId}")
    void getChildUser(@Path("ownerUuid") String ownerUuid, @Path("childUserUuid") String childUserUuid, Callback<ResponseDto<ChildUserModel>> cb);

    @DELETE("/api/users/{ownerUuid}/child/{childUserId}")
    void deleteChildUser(@Path("ownerUuid") String ownerUuid, @Path("childUserId") String childUserId, Callback<ResponseDto<ChildUserModel>> cb);

    @POST("/api/users/{ownerUuid}/child/{childUserId}")
    void updateChildUser(@Path("ownerUuid") String ownerUuid, @Path("childUserId") String childUserId, @Body ChildUserModel childUserModel, Callback<ResponseDto<ChildUserModel>> cb);

    //Get Users
    @GET("/api/users")
    void getFilteredUsers(@Query("username") String username, Callback<ResponseDto<UserModel>> cb);

    //List All Devices
    @GET("/api/inventory/devices")
    void listAllDevices(Callback<ResponseDto<DeviceModel>> cb);

    //Place device order.
    @PUT("/api/devices/orders")
    void createDeviceOrder(@Query("type") DeviceType type, @Body DeviceOrderModel device, Callback<ResponseDto<DeviceOrderModel>> cb);

    @POST("/api/updatepassword/")
    void updatePassword(@Body UserLoginModel userLoginModel, Callback<ResponseDto<UserModel>> cb);
}