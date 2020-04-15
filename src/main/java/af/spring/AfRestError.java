package af.spring;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class AfRestError implements AfRestView
{	
	public int error = -1;
	public String reason = "";
	
	public AfRestError()
	{		
	}
	public AfRestError(int error)
	{
		this.error = error;
	}
	public AfRestError(String reason)
	{
		this.reason = reason;
	}
	public AfRestError(int error, String reason)
	{
		this.error = error;
		this.reason = reason;
	}
	public AfRestError(Exception e)
	{
		if(e instanceof AfSpringException)
		{
			AfSpringException e2 = (AfSpringException)e;
			this.error = e2.error;
			this.reason = e2.reason;
		}
		else
		{
			this.error = -1;
			this.reason = e.getMessage();
		}
		
		if(reason == null)
			reason = e.getClass().getName();
	}
	
	@Override
	public void render(Map<String, ?> model
			, HttpServletRequest request
			, HttpServletResponse response) throws Exception
	{
		JSONObject json = new JSONObject(true);
		json.put("error", error);
		json.put("reason", reason);
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print( JSON.toJSONString(json,SerializerFeature.PrettyFormat) );
	}

}
