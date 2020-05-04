package com.rundeck.nvalenzuela.plugin.httpNotifications;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.descriptions.PluginDescription;
import com.dtolabs.rundeck.plugins.descriptions.PluginProperty;
import com.dtolabs.rundeck.plugins.descriptions.SelectValues;
import com.dtolabs.rundeck.plugins.notification.NotificationPlugin;

@Plugin(service="Notification",name="http-notification")
@PluginDescription(title="Notification Plugin", description="An plugin for Rundeck Notifications.")
public class HttpNotificationsPlugin implements NotificationPlugin{
	
	private static final String METHOD_POST = "POST";
	private static final String METHOD_PUT = "PUT";
	private static final String METHOD_DELETE = "DELETE";
	private static final String METHOD_GET = "GET";
	private static final int HTTP_OK = 200;
    private static final int REQUEST_TIME_OUT = 60000;
	private static final int SOCKET_TIME_OUT = 60000;
	private static final int CONNECT_TIME_OUT = 60000;
	

    @PluginProperty(name = "urlInput",title = "url",description = "Complete URL to which send the notification. example: http://machine1/notification")
    private String urlInput;
    
    @PluginProperty(name = "method",title = "http method",description = "The http method that should be used.")
    @SelectValues(freeSelect = false, values = {"POST", "PUT", "GET", "DELETE"})
    private String method;

    @PluginProperty(name = "body",title = "http body",description = "(optional) Content to be sent as body.")
    private String body;
    
    @PluginProperty(name = "contentType",title = "content type",description = "(optional) Indicates the content type of the body..")
    @SelectValues(freeSelect = false, values = {"JSON", "XML"})
    private String contentType;

    @PluginProperty(name = "debugFlg",title = "Activate log",description = "If you want to see the logs, please check the input")
    private boolean debugFlg;

    public HttpNotificationsPlugin(){

    }

    /**
     * Call a page depending on the type of method to call
     * 
     * @param trigger 
     * @param executionData
     * @param config
     * @return the flag with the answer successful or not  
     */
    public boolean postNotification(String trigger, Map executionData, Map config) {

        if(debugFlg)
    	    System.out.println("Calling postNotification method............");
    	
    	if(urlInput == null || urlInput.isEmpty()) {
    		if(debugFlg)
        	    System.out.println("the URL is null");
    		return false;
        	
    	} else if(urlInput != null && (!urlInput.toUpperCase().startsWith("HTTP://", 0) || urlInput.toUpperCase().startsWith("HTTPS://"))) {
    		urlInput = "https://" + urlInput;
    	}

        if(debugFlg)
            System.out.println("getting the information from the URL: " + urlInput);
    	
    	if(METHOD_POST.equals(method) || METHOD_PUT.equals(method)) {
        	return callPullorPost();
        	
        } else if(METHOD_GET.equals(method) || METHOD_DELETE.equals(method)) {
        	return callGetOrDelete();
        	
        } else {
        	if(debugFlg)
        	    System.out.println("Assigning the method GET as default");
        	
        	method = METHOD_GET;
        	return callGetOrDelete();
        	
        }
    }

    
    /**
     * call a page with the method GET or DELETE 
     * 
     * @return the flag with the answer successful or not
     */    
    public boolean callGetOrDelete(){
        if(debugFlg)
            System.out.println("Calling callGetOrDelete method");

        try{
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet getRequest = new HttpGet(urlInput);

            RequestConfig requestConfig = prepareRequestConfig();

            getRequest.setConfig(requestConfig);

            getRequest.addHeader("content-type", getContentType(contentType));

            HttpResponse response = httpClient.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();

            if( HTTP_OK == statusCode ) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }

        }catch (UnknownHostException ue){
            if(debugFlg) {
                System.out.println("ERROR: the host does not exist " + urlInput );
                System.out.println("ERROR MESSAGE: " + ue.getMessage());
            }

            return Boolean.FALSE;
            
        }catch ( MalformedURLException  me){
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + me.getMessage());
            }
            return Boolean.FALSE;
        
        } catch (ProtocolException pe) {
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + pe.getMessage());
            }

            return Boolean.FALSE;
        
        } catch (SocketTimeoutException ste) {
            if(debugFlg) {
                System.out.println("a TimeOut has occurred when send a notification" );
                System.out.println("ERROR MESSAGE: " + ste.getMessage());
            }

            return Boolean.FALSE;
        
        } catch (IOException ioe) {
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + ioe.getMessage());
            }

            return Boolean.FALSE;
		}

    }

    
    /**
     * call a page with the method PULL or POST
     * 
     * @return the flag with the answer successful or not
     */
    public boolean callPullorPost(){
        if(debugFlg)
            System.out.println("Calling callPullorPost method");

        try{
        	CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost postRequest = new HttpPost(urlInput);

            RequestConfig requestConfig = prepareRequestConfig();

            postRequest.setConfig(requestConfig);

            postRequest.addHeader("content-type", getContentType(contentType));

            postRequest.setEntity(new StringEntity(body));

            CloseableHttpResponse response = httpClient.execute(postRequest);

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();

	        if( HTTP_OK == statusCode ) {
            	return Boolean.TRUE;
            } else {
            	return Boolean.FALSE;
            }
        
        }catch (UnknownHostException ue){
            if(debugFlg) {
                System.out.println("ERROR: the host does not exist " + urlInput);
                System.out.println("ERROR MESSAGE: " + ue.getMessage());
            }

            return Boolean.FALSE;
            
        }catch ( MalformedURLException  me){
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + me.getMessage());
            }
            return Boolean.FALSE;
        
        } catch (ProtocolException pe) {
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + pe.getMessage());
            }
            return Boolean.FALSE;
        
        } catch (SocketTimeoutException ste) {

            if(debugFlg) {
                System.out.println("ERROR: a TimeOut has occurred when send a notification");
                System.out.println("ERROR MESSAGE: " + ste.getMessage());
            }
            return Boolean.FALSE;
       
        } catch (IOException ioe) {
            if(debugFlg) {
                System.out.println("ERROR MESSAGE: " + ioe.getMessage());
            }
            return Boolean.FALSE;
		}
        
    }

    /**
     * Prepare the Configuration for the Request like 
     */
	private RequestConfig prepareRequestConfig() {
		RequestConfig requestConfig = RequestConfig.custom()
		        .setConnectionRequestTimeout(REQUEST_TIME_OUT)
		        .setConnectTimeout(CONNECT_TIME_OUT)
		        .setSocketTimeout(SOCKET_TIME_OUT)
		        .build();
		return requestConfig;
	}

    private String getContentType(String contentType){
        if(contentType != null && contentType.equals("JSON")) {
            return "application/xml";
        } else if(contentType != null && contentType.equals("JSON")){
            return "application/xml";
        } else {
            return "text/plain";
        }
    }
        

}
