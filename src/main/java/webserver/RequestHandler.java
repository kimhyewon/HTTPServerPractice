//package webserver;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.nio.file.Files;
//import java.util.Map;
//
//import model.User;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import util.HttpRequestUtils;
//import util.IOUtils;
//import util.SplitLine;
//
//public class RequestHandler extends Thread {
//	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
//	
//	private Socket connection;
//
//	public RequestHandler(Socket connectionSocket) {
//		this.connection = connectionSocket;
//	}
//
//	public void run() {
//		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
//		
//		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
//			
//	        InputStreamReader inputStreamReader = new InputStreamReader(in);
//	        BufferedReader br = new BufferedReader(inputStreamReader);
//	        
//	        String line = br.readLine();
//	        
//	        if (line == null){ return; }
//	        
//	        String url = SplitLine.extractURL(line);
//	        log.debug("url : {}", url);	
//	        
//	        while (!"".equals(line)) {
//		        log.debug(line);
//		        line = br.readLine();
//		    }
//	       
//
//	        //get
////	        int index = url.indexOf("?");
////	        if(index > -1) {
////	        	String requestPath = url.substring(0, index); 
////		        String params = url.substring(index+1);
////		        Map<String, String> userInfo= HttpRequestUtils.parseQueryString(params);
////		    	User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
////		    	System.out.println(user);
////	        }
//	        
//
//	        
//	        DataOutputStream dos = new DataOutputStream(out);
//	        byte[] body = Files.readAllBytes(new File("./webapp/" + url).toPath());
////			byte[] body = "Hello World".getBytes();
//			response200Header(dos, body.length);
//			responseBody(dos, body);
//			
//			//post
//			
//			//59부분을 바꿔야 한다. : 기반으로 split해서 공백 제거 위해 trim해주고 결과적으로 length부분의 숫자를 받아와야 한다. 
//			
//			Map<String, String> userInfo= HttpRequestUtils.parseQueryString(url);
//			IOUtils.readData(br, Integer.parseInt(userInfo.get("Content-Length")));
//		    User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
//		   	System.out.println(user);
//	        
//			
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//		
//        
//		
//	}
//
//	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//		try {
//			dos.writeBytes("HTTP/1.1 200 Document Follows \r\n");
//			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//			dos.writeBytes("\r\n");
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//	
//	private void responseBody(DataOutputStream dos, byte[] body) {
//		try {
//			dos.write(body, 0, body.length);
//			dos.writeBytes("\r\n");
//			dos.flush();
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//}

package webserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.SplitLine;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			
			InputStreamReader inputStreamReader = new InputStreamReader(in);
	        BufferedReader br = new BufferedReader(inputStreamReader);
	        
	        String line = br.readLine();
	        
	        if (line == null){ return; }
	        
	        String url = SplitLine.extractURL(line);
	        log.debug("url : {}", url);	

			BufferedOutputStream bos = new BufferedOutputStream(out);
			bos.write("HTTP/1.1 200 OK \r\n".getBytes());
			bos.write("Content-Type: application/zip;charset=utf-8\r\n".getBytes());
			bos.write("\r\n".getBytes());
			
			FileInputStream fis = new FileInputStream((new File("./webapp/" + url)));
			BufferedInputStream bis = new BufferedInputStream(fis);
			int n = -1;
			byte[] buffer = new byte[8192];
			while((n=bis.read(buffer)) > -1) {
				bos.write(buffer, 0, n);
			}
			bos.flush();
			bos.close();
			
//			dos.write(Files.readAllBytes(new File("./webapp/" + url).toPath()));
//			dos.flush();
//			byte[] body = Files.readAllBytes(new File("./webapp/" + url).toPath());
//			byte[] body = Files.
//			byte[] body = "Hello World".getBytes();
//			response200Header(dos, body.length);
//			responseBody(dos, body);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: application/zip;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
