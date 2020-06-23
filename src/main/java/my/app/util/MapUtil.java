package my.app.util;

import java.util.Map;

public class MapUtil
{
	Map map ;
	
	public MapUtil(Map map)
	{
		this.map = map;
	}
	
	public void asInt(String key, Integer defValue)
	{
		Integer value = defValue;
		try {
			String str = (String)map.get(key);
			if(str != null)
				value = Integer.valueOf(str);
			
		}catch(Exception e) {}
		
		map.put(key, value);
	}
	
	public void asLong(String key, Long defValue)
	{
		Long value = defValue;
		try {
			String str = (String)map.get(key);
			if(str != null)
				value = Long.valueOf(str);
			
		}catch(Exception e) {}
		
		map.put(key, value);
	}

	public void asString(String key, String defValue)
	{
		String str = (String)map.get(key);
		if(str == null)
			map.put(key, defValue);		
	}
	
	public void asBoolean(String key, Boolean defValue)
	{
		Boolean value = defValue;
		try {
			String str = (String)map.get(key);
			if(str != null) value = (! str.equals("0"));
			
		}catch(Exception e) {}
		
		map.put(key, value);
	}
}
