package af.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AfRestData implements AfRestView
{	
	Object data;
	
	public AfRestData(Object data)
	{
		this.data = data;
	}
		
	@Override
	public void render(Map<String, ?> model
			, HttpServletRequest request
			, HttpServletResponse response) throws Exception
	{
		JSONObject json = new JSONObject(true);
		json.put("error", 0);
		json.put("reason", "OK");
		if(data != null)
		{
			if(data instanceof JSON) // 本身就是 JSONObject 或 JSONArray
				json.put("data", data);
			else
				json.put("data", JSON.toJSON(data));
		}

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain");
		response.getWriter().print( JSON.toJSONString(json,SerializerFeature.PrettyFormat) );
	}

}
