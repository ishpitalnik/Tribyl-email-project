package com.trybyl.emailproject.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;

@Service
public class GraphApiService {
	
	@Value("${graphapi.AUTHORITY}")
	private String authority;
	
	@Value("${graphapi.CLIENT_ID}")
	private String clientId;
	
	@Value("${graphapi.SECRET}")
	private String secret;
	
	@Value("${graphapi.SCOPE}")
	private String scope;
	
	private String accessToken;
	
	@PostConstruct
    private void postConstruct() throws Exception {
	        ConfidentialClientApplication app = ConfidentialClientApplication.builder(
	                clientId,
	                ClientCredentialFactory.createFromSecret(secret))
	                .authority(authority)
	                .build();

	        // With client credentials flows the scope is ALWAYS of the shape "resource/.default", as the
	        // application permissions need to be set statically (in the portal), and then granted by a tenant administrator
	        ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
	                Collections.singleton(scope))
	                .build();

	        CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
	        IAuthenticationResult result =  future.get();
	        accessToken = result.accessToken();
    }

    public String fetchEmails(String user) throws Exception {
    	URL url = new URL("https://graph.microsoft.com/v1.0/users/"+user+"/mailFolders/Inbox/Messages");
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();

         conn.setRequestMethod("GET");
         conn.setRequestProperty("Authorization", "Bearer " + this.accessToken);
         conn.setRequestProperty("Accept","application/json");

         int httpResponseCode = conn.getResponseCode();
         if(httpResponseCode == HTTPResponse.SC_OK) {

             StringBuilder response;
             try(BufferedReader in = new BufferedReader(
                     new InputStreamReader(conn.getInputStream()))){

                 String inputLine;
                 response = new StringBuilder();
                 while (( inputLine = in.readLine()) != null) {
                     response.append(inputLine);
                 }
             }
             return response.toString();
         } else {
             return String.format("Connection returned HTTP code: %s with message: %s",
                     httpResponseCode, conn.getResponseMessage());
         }
    }
}
