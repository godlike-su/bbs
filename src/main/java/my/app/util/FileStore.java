package my.app.util;


import javax.servlet.http.HttpServletRequest;
import java.io.File;

/** 文件的本地存储与访问，path和url的换算
 * 
 */
public class FileStore
{
	public File rootDir = new File("c:/bbsfile/message/");
	public String urlPrefix = "/bbsfile/message/";

	public FileStore(){}

	public FileStore(String dir, String urlPrefix)
	{
		this(new File(dir), urlPrefix);
	}

	public FileStore(File dir, String urlPrefix)
	{
		this.rootDir = dir;
		this.urlPrefix = urlPrefix;
		this.rootDir.mkdirs();
	}

	//web下的默认路径
	public FileStore(HttpServletRequest request)
	{
		String rootPath = request.getServletContext().getRealPath(urlPrefix);
		File rootDir = new File(rootPath);
		this.rootDir = rootDir;
		this.rootDir.mkdirs();
	}

	// 传入相对路径path，返回File
	public File getFile(String path)
	{
		rootDir.mkdirs();
		return new File(rootDir, path);
	}
	public File getFile(File rootDir, String path)
	{
		rootDir.mkdirs();
		return new File(rootDir, path);
	}
	// 传入相对路径path，返回web目录下的File
	public File getFile(HttpServletRequest request, String fileUrl)
	{
		String rootPath = request.getServletContext().getRealPath(fileUrl);
		File rootFile = new File(rootPath);
		rootFile.mkdirs();
		return rootFile;
	}

	// 传入相对路径path，返回URL
	public String getUrl(String path)
	{
		return urlPrefix + path;
	}
	
	// 根据URL，找到相对路径
	public String pathOfUrl(String url)
	{
		return url.substring(urlPrefix.length());
	}
	
	// 根据URL，找到相应的文件
	public File fileOfUrl( String url)
	{
		return new File(rootDir, pathOfUrl(url));
	}
	public File fileOfUrl(File rootDir, String url)
	{
		return new File(rootDir, pathOfUrl(url));
	}
	//根据URL，找到当前web目录下的文件
	public File fileOfUrl(HttpServletRequest request)
	{
		String rootPath = request.getServletContext().getRealPath("/");
		return new File(rootPath);
	}
	public File fileOfUrl(HttpServletRequest request, String fileUrl)
	{
		String rootPath = request.getServletContext().getRealPath("/");
		return new File(rootDir, fileUrl);
	}

	//获取当前web目录下的绝对路径
	public String getAbsoultUrl(HttpServletRequest request)
	{
		String rootPath = request.getServletContext().getRealPath("/");
		return rootPath;
	}
	public String getAbsoultUrl(HttpServletRequest request, String fileUrl)
	{
		String rootPath = request.getServletContext().getRealPath(fileUrl);
		return rootPath;
	}



}
