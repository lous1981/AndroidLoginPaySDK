package com.yuan.shi.lonng.utils;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class LongDaHttpUtils {


	private static int result = 0;
	private static final int ConnectTimeout = 60 * 1000;

	static public String post(String address, String param) throws Exception {
		
		StringBuilder output = new StringBuilder();

		try {
			URL url = new URL(address);

			result=0;
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			if (conn != null) {
				conn.setConnectTimeout(ConnectTimeout);
				conn.setReadTimeout(ConnectTimeout);
				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setUseCaches(false);

				// 设置文件类型:
				conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
				// 设置接收类型否则返回415错误
				//conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
				conn.setRequestProperty("accept","application/json");

				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(param);
				wr.flush();
				wr.close();


				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader reader =
						new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while (true) {
						String line = reader.readLine();
						if (line == null) break;
						
						output.append(line + '\n');
					}
					reader.close();
					result=1;
				} else {
					throw new Exception(String.format("ErrorCode = %d", conn.getResponseCode()));
				}					
				conn.disconnect();
			}
		}
		catch ( SocketTimeoutException e ) {
			result=-1;			
			throw new Exception("TimeOut");
		}		
		catch (Exception E) {
			result=0;
			throw new Exception("Occour error");
		}
		
		return output.toString();
	}	
	
	public static String get(String urlAddr) {
		String result = null;
		try {
			StringBuilder output = new StringBuilder();
			//1：url对象
			URL url = new URL(urlAddr);
			//2;url.openconnection
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			//3
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(10 * 1000);
			//4
			int code = conn.getResponseCode();
			if (code == 200) {
				BufferedReader reader =
						new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while (true) {
					String line = reader.readLine();
					if (line == null) break;

					output.append(line + '\n');
				}
				reader.close();
				result = output.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
