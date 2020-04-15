package af.spring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.springframework.ui.Model;

/** 提供 Spring 的一些工具方法
 * 
 */
public class AfSpring
{
	// Object -> Model
	public static void putModel (Model model, Object pojo)
	{
		Class pojoClass = pojo.getClass();
		Field[] fields = pojoClass.getDeclaredFields();
	
		// 从 obj 中取出所有的属性值，放到 json 里
		for(Field f : fields)
		{
			// 属性名
			String name = f.getName();	
								
			if( Modifier.isPublic( f.getModifiers())) 
			{
				// public的属性直接  get
				try{
					Object value = f.get( pojo);
					model.addAttribute( name,  value );	
				}
				catch (Exception e){
				} 									
			}
			else
			{
				// private的属性，通过 Getter来获取
				String getterName = getterName(name);					
				try{
					Method getter = pojoClass.getDeclaredMethod(getterName);				
					Object value = getter.invoke(pojo);
					model.addAttribute( name,  value );
				}
				catch(Exception e){				
				}		
			}			
		}				
	}
	
	
	// 字段名  -> Getter名
	public static String getterName(String field)
	{
		// "name" -> "getName()"
		char firstChar = Character.toUpperCase(field.charAt(0));
		StringBuffer strbuf = new StringBuffer("get" + field);
		strbuf.setCharAt(3, firstChar);
		return strbuf.toString();
	}
	
	// 字段名 -> Setter名
	public static String setterName(String field)
	{
		// "name" -> "setName()"
		char firstChar = Character.toUpperCase(field.charAt(0));
		StringBuffer strbuf = new StringBuffer("set" + field);
		strbuf.setCharAt(3, firstChar);
		return strbuf.toString();
	}	
}
