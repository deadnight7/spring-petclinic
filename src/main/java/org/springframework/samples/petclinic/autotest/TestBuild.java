/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.autotest;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.DataOutputStream;

@Controller
class TestBuild {

	@GetMapping("/build")
	public String startTestBuild () {
		System.out.println("Triggering Tests...");
		return "welcome";
	}

	@GetMapping("/")
	public String getApplication () {
		System.out.println("Serving Home Page...");
		return "layout";
	}

	@GetMapping ("/buildWithParams_Test")
	public String buildWithParams_Test (
			@RequestParam("instance") String instance,
			@RequestParam("browserCount") String browserCount,
			@RequestParam("mailReportTo") String mailReportTo
			)
	{
		try {

			System.out.println("Starting to send Jenkins build using : "
					+ "\n instance - "+instance
					+ "\n browserCount - "+browserCount
					+ "\n mailReportTo - "+mailReportTo
					);
			String strJenkinsBuildURL = "";
			if (instance != null && instance.toLowerCase().startsWith("prod")){
				strJenkinsBuildURL = strJenkinsURL_Prod;
			}
			else if  (instance != null && instance.toLowerCase().startsWith("test")){
				strJenkinsBuildURL = strJenkinsURL_Test;
			}
			else if  (instance != null && instance.toLowerCase().startsWith("demo")){
				strJenkinsBuildURL = strJenkinsURL_Demo;
			}

			URL url = new URL (strJenkinsBuildURL); // Jenkins URL localhost:8080, job named 'test'
			String user = strUserName; // username
			String pass = strUserToken; // password or API token
			String authStr = user +":"+  pass;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);

			String urlParams=
					"ParallelMode=Y"
							+ "&NumberofParallelBrowsers="+browserCount
							+"&SendMailReportTo="+mailReportTo
							+"&testsToExecute=ALL";
			byte[] postData = urlParams.getBytes("utf-8");
			try(DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.write(postData);
			}
			System.out.println(postData);
			InputStream content = connection.getInputStream();
			BufferedReader in   =
					new BufferedReader (new InputStreamReader (content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		System.out.println("Completed invoking Jenkins API...");
		return "welcome";
	}
	
	public String strJenkinsURL_Test = "http://10.124.92.45:8080/job/LoginextMile/buildWithParameters";
	public String strJenkinsURL_Demo = "http://10.124.92.45:8080/job/LoginextMile_Demo/buildWithParameters";
	public String strJenkinsURL_Prod = "http://10.124.92.45:8080/job/LoginextMile_Prod/buildWithParameters";
	
	public String strUserName = "chirag";
	public String strUserToken = "09066518b1897dcf57789b7125ba68cf";

	@GetMapping ("/buildWithParams")
	public String buildWithParams ()
	{
		try {

			URL url = new URL (strJenkinsURL_Prod); // Jenkins URL localhost:8080, job named 'test'
			String user = strUserName; // username
			String pass = strUserToken; // password or API token
			String authStr = user +":"+  pass;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			
			String urlParams=
					"paramA=TEST:ALL "
					+ "&paramB=test@loiinextsolutions.com";
			byte[] postData = urlParams.getBytes("utf-8");
			try(DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
				wr.write(postData);
			}
			System.out.println(postData);
			InputStream content = connection.getInputStream();
			BufferedReader in   =
					new BufferedReader (new InputStreamReader (content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		System.out.println("Completed invoking Jenkins API...");
		return "welcome";
	}


	@GetMapping ("/buildWithoutParams")
	public static String buildWithoutParams ()
	{
		try {
			URL url = new URL ("http://10.124.92.45:8080/job/Test_JenkinsShellScript//build"); // Jenkins URL localhost:8080, job named 'test'
			String user = "chirag"; // username
			String pass = "09066518b1897dcf57789b7125ba68cf"; // password or API token
			String authStr = user +":"+  pass;
			String encoding = DatatypeConverter.printBase64Binary(authStr.getBytes("utf-8"));

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Authorization", "Basic " + encoding);
			System.out.println("encoding: "+encoding);
			InputStream content = connection.getInputStream();
			BufferedReader in =
					new BufferedReader (new InputStreamReader (content));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch(Exception e) {
			e.printStackTrace();
			return "error";
		}
		System.out.println("Sent post request to Job - Successfully");
		return "welcome";
	}

}
