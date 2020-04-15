package my.app.util;

import java.io.*;
import java.util.List;


import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/* HTTP
 * 此工具类封装了 get / post / upload /download 等常用功能 
 * 
 */
public class HTTP
{
	CloseableHttpClient httpClient;
	int timeout = 3000;	
	
	public HTTP()
	{
		CookieStore cookieStore = new BasicCookieStore();
		
//		DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(null);
//
//		
//		SSLContext sslContext = SSLContexts.createSystemDefault();
//		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//		 sslContext,
//		 hostnameVerifier);
		
		httpClient = HttpClients.custom()
				.setDefaultCookieStore(cookieStore)
				//.setSSLSocketFactory(sslsf)
				.build();


	
	}
	
	public HTTP(CloseableHttpClient httpClient)
	{
		this.httpClient = httpClient;
	}
	
	public void close()
	{
		try
		{
			httpClient.close();
		} catch (IOException e)
		{
		}
	}
	
	public String get (String url, List <NameValuePair> nvps) throws Exception
	{
		if(nvps!=null && nvps.size()>0)
		{
			String query = URLEncodedUtils.format(nvps, "UTF-8");		
			if(url.indexOf('?') >=0)
				url = url + "&" + query;
			else
				url = url + "?" + query;
		}
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout) 
				.setConnectTimeout(timeout) 
				.build();		
		
		HttpGet httpget = new HttpGet(url);			
		httpget.setConfig(requestConfig);
		
		// 执行HTTP请求，得到应答
		CloseableHttpResponse response = httpClient.execute(httpget);
				
		try {			
			// 查看应答结果
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != 200)
			{
				System.out.println("HTTP Error! Status=" + status);
			}
			else
			{
				HttpEntity dataRecv = response.getEntity();
				String msgRecv = EntityUtils.toString(dataRecv);
				return msgRecv;
			}						
		}finally
		{
			response.close(); // 当调用close后，内部的网络连接才被关闭
		}
		return null;
	}
	
	
	public String post (String url, List <NameValuePair> nvps) throws Exception
	{
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout) 
				.setConnectTimeout(timeout) 
				.build();		
		
		HttpPost httppost = new HttpPost(url);			
		httppost.setConfig(requestConfig);
		
		// 表单方式, ContentType应为 "application/x-www-form-urlencoded"
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nvps, Consts.UTF_8);
		//entity.setContentType("application/x-www-form-urlencoded");
		httppost.setEntity(entity);			
		
		// 执行HTTP请求，得到应答
		CloseableHttpResponse response = httpClient.execute(httppost);
				
		try {			
			// 查看应答结果
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != 200)
			{
				System.out.println("HTTP Error! Status=" + status);
			}
			else
			{
				HttpEntity dataRecv = response.getEntity();
				String msgRecv = EntityUtils.toString(dataRecv);
				return msgRecv;
			}						
		}finally
		{
			response.close(); // 当调用close后，内部的网络连接才被关闭
		}
		return null;
	}
	
	// 自定义的上传 ( 适用于 RESTful 形式的接口 )
	public String post (String url, String msgSend) throws Exception
	{
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout) 
				.setConnectTimeout(timeout) 
				.build();		
		
		HttpPost httppost = new HttpPost(url);			
		httppost.setConfig(requestConfig);
		
		// JSON / RESTful 方式, Content Type 可以为 text/plain
		StringEntity entity = new StringEntity(msgSend, Consts.UTF_8);		
		entity.setContentType("text/plain");
		httppost.setEntity(entity);	
		
		// 执行HTTP请求，得到应答
		CloseableHttpResponse response = httpClient.execute(httppost);
				
		try {			
			// 查看应答结果
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != 200)
			{
				System.out.println("HTTP Error! Status=" + status);
			}
			else
			{
				HttpEntity dataRecv = response.getEntity();
				String msgRecv = EntityUtils.toString(dataRecv);
				return msgRecv;
			}						
		}finally
		{
			response.close(); // 当调用close后，内部的网络连接才被关闭
		}
		return null;
	}
	
	// URL : 上传服务的接口地址
	// file ： 要上传的本地文件
	// nvps : 附加参数，可以为null
	public String upload (String url, File file, List <NameValuePair> nvps) throws Exception
	{
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout) 
				.setConnectTimeout(timeout) 
				.build();		
		HttpPost httppost = new HttpPost(url);			
		httppost.setConfig(requestConfig);
		
		// 添加要上传的内容
		MultipartEntityBuilder builder = MultipartEntityBuilder
				.create()
				.setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
				.setCharset(Consts.UTF_8);
		
		// 普通表单域
		if(nvps != null)
		{			
			ContentType TEXT_PLAIN = ContentType.create("text/plain", Consts.UTF_8);
			for(NameValuePair e: nvps)
			{
				builder.addTextBody(e.getName(), e.getValue(), TEXT_PLAIN);
			}
		}
		
		// 文件域
		FileBody filepart = new FileBody(file);
		builder.addPart("file", filepart);
		
		// 
		HttpEntity entity = builder.build();
		httppost.setEntity(entity);
		
		// 3 执行HTTP请求
		CloseableHttpResponse response = httpClient.execute(httppost);// 发起HTTP
		try {			
			// 查看应答结果
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != 200)
			{
				System.out.println("HTTP Error! Status=" + status);
			}
			else
			{
				HttpEntity dataRecv = response.getEntity();
				String msgRecv = EntityUtils.toString(dataRecv);
				return msgRecv;
			}						
		}finally
		{
			response.close(); // 当调用close后，内部的网络连接才被关闭
		}
		
		return null;
	}
	
	
	
	// url ：要下载的地址
	// dir : 本地下载目录
	// fileName: 文件名, 如果为null，则自动生成文件名
	// maxSize: 文件大小限制
	public File download (String url, File dir, String fileName, long maxSize) throws Exception
	{
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout) 
				.setConnectTimeout(timeout) 
				.build();		
		
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		
		// 3 执行HTTP请求
		CloseableHttpResponse response = httpClient.execute(httpget);// 发起HTTP
		
		try {			
			// 查看应答结果
			StatusLine statusLine = response.getStatusLine();
			int status = statusLine.getStatusCode();
			if (status != 200)
			{
				System.out.println("HTTP Error! Status=" + status);
			}
			else
			{
				// 确定文件名称
				if(fileName == null)
				{
					int p1 = url.lastIndexOf('/');
					fileName = url.substring(p1+1);
				}
				
				// 存到文件
				File tmpFile = new File(dir, fileName);				
				OutputStream outputStream  = new FileOutputStream(tmpFile);
						
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				try {
					copy(inputStream, outputStream, maxSize);
				}finally
				{
					// 确保文件被关闭
					try {outputStream.close();} catch(Exception e) {}
				}	
				return tmpFile;
			}						
		}finally
		{
			response.close(); // 当调用close后，内部的网络连接才被关闭
		}
		
		return null;
	}
	
	// 如果文件较大, 应该在下载的时候注意sleep，以控制下载速度 
	private void copy(InputStream inputStream, OutputStream outputStream, long maxSize) throws Exception
	{
		// 下载文件到本地
		byte[] buf = new byte[4096];
		long total = 0;
		while (true)
		{
			int n = inputStream.read(buf);
			if (n <= 0)
				break;
			outputStream.write(buf, 0, n);
			total += n;
			
			if (maxSize >0 && total >= maxSize)
				throw new Exception("文件大小超过限制( " + maxSize	+ "), 放弃下载!");
		}
	}

}
