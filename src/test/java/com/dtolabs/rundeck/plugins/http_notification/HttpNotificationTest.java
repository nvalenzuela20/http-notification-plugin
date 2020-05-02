package com.dtolabs.rundeck.plugins.http_notification;

import static org.junit.Assert.assertEquals;

import java.net.UnknownHostException;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.dtolabs.rundeck.plugin.http_notifications.HttpNotificationsPlugin;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HttpNotificationsPlugin.class)
public class HttpNotificationTest {

    @Test
    public void testBasic() throws Exception {
    	
    	DefaultHttpClient mock = Mockito.mock(DefaultHttpClient.class);
    	PowerMockito.whenNew(DefaultHttpClient.class).withNoArguments().thenReturn(mock);
    	PowerMockito.when(mock.execute(Matchers.any(HttpPost.class))).thenThrow(new UnknownHostException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "http://noexiste.com");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }

}
