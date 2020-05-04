package com.rundeck.nvalenzuela.plugins.test.httpNotification;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.collections.map.HashedMap;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.rundeck.nvalenzuela.plugin.httpNotifications.HttpNotificationsPlugin;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpNotificationsPlugin.class, HttpClients.class})
@PowerMockIgnore({"javax.net.ssl.*", "javax.security.*", "javax.security.*"})
public class HttpNotificationTest {
	
	
	 @Test
	 public void callWithoutUrlTest() throws Exception {
	    	
		CloseableHttpClient mockHttpClient = getMockHttpClient();
	    prepareMockRespose(mockHttpClient, 200);
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
	}
	 
	@Test
	public void callWithoutMethodTest() throws Exception {
	    	
		CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
	}
	
	@Test
	public void callResult404Test() throws Exception {
	    
		CloseableHttpClient mockHttpClient = getMockHttpClient();
    	
        prepareMockRespose(mockHttpClient, 404);

    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
	}

	private CloseableHttpClient getMockHttpClient() {
		PowerMockito.mockStatic(HttpClients.class);
		CloseableHttpClient mockHttpClient = PowerMockito.mock(CloseableHttpClient.class);
    	PowerMockito.when(HttpClients.createDefault()).thenReturn(mockHttpClient);
		return mockHttpClient;
	}
	
	/**/
    @Test
    public void callPostUnknownHostExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
        
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpPost.class))).thenThrow(new UnknownHostException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }

	private void prepareMockRespose(CloseableHttpClient mockHttpClient, int errorCode) throws IOException, ClientProtocolException {
		CloseableHttpResponse httpResponse = PowerMockito.mock(CloseableHttpResponse.class);
        StatusLine statusLine = PowerMockito.mock(StatusLine.class);

        PowerMockito.doReturn(errorCode).when(statusLine).getStatusCode();
        PowerMockito.doReturn(statusLine).when(httpResponse).getStatusLine();
        PowerMockito.doReturn(httpResponse).when(mockHttpClient).execute(Matchers.any(HttpUriRequest.class));

	}
    
    @Test
    public void callPostMalformedURLExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpPost.class))).thenThrow(new MalformedURLException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callPostProtocolException() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpPost.class))).thenThrow(new ProtocolException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callPostSocketTimeoutExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpPost.class))).thenThrow(new SocketTimeoutException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callPostIOExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpPost.class))).thenThrow(new IOException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "POST");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    /**  **/
    @Test
    public void callGetUnknownHostExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpGet.class))).thenThrow(new UnknownHostException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "GET");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callGetMalformedURLExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpGet.class))).thenThrow(new MalformedURLException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "GET");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }

    @Test
    public void callGetProtocolExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpGet.class))).thenThrow(new ProtocolException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "GET");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callGetSocketTimeoutExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpGet.class))).thenThrow(new SocketTimeoutException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "GET");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
    
    @Test
    public void callGetIOExceptionTest() throws Exception {
    	
    	CloseableHttpClient mockHttpClient = getMockHttpClient();
        prepareMockRespose(mockHttpClient, 200);
    	PowerMockito.when(mockHttpClient.execute(Matchers.any(HttpGet.class))).thenThrow(new IOException());
    	
    	HttpNotificationsPlugin notification = new HttpNotificationsPlugin();
    	
    	Whitebox.setInternalState(notification, "urlInput", "localhost:1234/fakepage");
    	Whitebox.setInternalState(notification, "method", "GET");
    	Whitebox.setInternalState(notification, "body", "");
    	Whitebox.setInternalState(notification, "contentType", "");
    	Whitebox.setInternalState(notification, "debugFlg", true);
    	
    	boolean postNotification = notification.postNotification("success", new HashedMap(), null);
    	
    	assertEquals(false, postNotification);
    }
}
