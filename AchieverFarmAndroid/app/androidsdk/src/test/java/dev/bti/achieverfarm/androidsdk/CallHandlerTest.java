package dev.bti.achieverfarm.androidsdk;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import dev.bti.achieverfarm.androidsdk.client.CallHandler;
import dev.bti.achieverfarm.androidsdk.common.SDKHelpers;
import dev.bti.achieverfarm.androidsdk.helpers.UserHelper;
import dev.bti.achieverfarm.androidsdk.models.res.UserRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallHandlerTest {
    private final Call<UserRes> mockCall = mock(Call.class);
    private SDKHelpers sdkHelpers;

    @Before
    public void setUp() {
        sdkHelpers = SDKHelpers.GetInstance("testToken");
        UserHelper mockUserHelper = mock(UserHelper.class);

        when(mockUserHelper.getUser("hWtn311OFUiM1Oj7"))
                .thenReturn(new CallHandler<>(mockCall));
        sdkHelpers = spy(sdkHelpers);
        doReturn(mockUserHelper).when(sdkHelpers).getUserHelper();
    }

    @Test
    public void testGetUser_Success() {
        UserRes user = new UserRes();

        user.setId("hWtn311OFUiM1Oj7");
        user.setFullName("Brandon Chikandiwa");
        user.setEmail("brandon1@gmail.com");
        user.setPhone("0788519463");
        user.setVerified(false);
        user.setLogins(List.of(Instant.parse("2024-12-03T15:36:27.862+00:00")));
        user.setUpdates(List.of(Instant.parse("2024-12-03T15:36:27.862+00:00")));

        Response<UserRes> mockResponse = Response.success(user);
        doAnswer(invocation -> {
            Callback<UserRes> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, mockResponse);
            return null;
        }).when(mockCall).enqueue(any());

        sdkHelpers.getUserHelper().getUser("hWtn311OFUiM1Oj7")
                .addOnSuccessListener(data -> {
                    System.out.println("SUCCESS: " + data);
                })
                .addOnFailureListener(e -> {
                    System.err.println("ERROR: " + e.getMessage());
                }).execute();
    }


    @Test
    public void testGetUser_Failure() {
        doAnswer(invocation -> {
            Callback<UserRes> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new RuntimeException("Mocked error"));
            return null;
        }).when(mockCall).enqueue(any());

        sdkHelpers.getUserHelper().getUser("hWtn311OFUiM1Oj7")
                .addOnSuccessListener(data -> {
                    System.out.println("SUCCESS: " + data);
                })
                .addOnFailureListener(e -> {
                    System.err.println("ERROR: " + e.getMessage());
                }).execute();
    }

    @Test
    public void testGetUser_IncorrectToken() {
        SDKHelpers sdkHelpersIncorrectToken = SDKHelpers.GetInstance("incorrectToken");

        UserHelper mockUserHelper = mock(UserHelper.class);

        when(mockUserHelper.getUser("hWtn311OFUiM1Oj7"))
                .thenReturn(new CallHandler<>(mockCall));

        sdkHelpersIncorrectToken = spy(sdkHelpersIncorrectToken);

        doReturn(mockUserHelper).when(sdkHelpersIncorrectToken).getUserHelper();

        doAnswer(invocation -> {
            Callback<UserRes> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new RuntimeException("Mocked error: Incorrect token"));
            return null;
        }).when(mockCall).enqueue(any());

        sdkHelpersIncorrectToken.getUserHelper().getUser("hWtn311OFUiM1Oj7")
                .addOnSuccessListener(data -> {
                    System.out.println("SUCCESS: " + data);
                })
                .addOnFailureListener(e -> {
                    System.err.println("ERROR: " + e.getMessage());
                    assertEquals("Mocked error: Incorrect token", e.getMessage());
                }).execute();
    }
}

