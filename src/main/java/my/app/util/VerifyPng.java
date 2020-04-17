package my.app.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

/**
 * 绘制验证码
 * @author 粟金旺
 *
 */
public class VerifyPng
{
	//文本 和 输出流
	public void toPNG(String text, OutputStream out)
	{
		int width = 75;
		int height = 30;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics2D g2d = image.createGraphics();
		g2d.setPaint(new Color(0xB03060)); // 文本颜色
		g2d.setFont(new Font("微软雅黑", Font.PLAIN, height-6));

		FontMetrics fm = g2d.getFontMetrics(g2d.getFont());
		int fontSize = fm.getHeight(); // 字高
		int textWidth = fm.stringWidth(text);
		int leading = fm.getLeading();
		int ascent = fm.getAscent(); // top -> baseline 的高度
		int descent = fm.getDescent(); // bottom->baseline 的高度

		Rectangle rect = new Rectangle(width,height);
//		int x = rect.x + (rect.width - textWidth)/2; // 水平居中
		int x = 0; // 水平居左
		int y = rect.y + rect.height /2 + (fontSize-leading)/2 - descent; // 竖直居中
		g2d.drawString(text, x, y);
		
		//添加随机圆圈
		Random rand = new Random();
		for(int i=0;i<4;i++)
		{
		    int r = rand.nextInt(255);
		    int g = rand.nextInt(255);
		    int b = rand.nextInt(255);
		    int posX = rand.nextInt(width-10);
		    int posY = rand.nextInt(height-10);
		    g2d.setPaint(new Color(r,g,b, 60));
		    g2d.fillOval(posX, posY, 10, 10);
		}
		
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		 InputStream in = null;
		    try {
		        ImageIO.write(image, "png", os);
		        in = new ByteArrayInputStream(os.toByteArray());
		        MyUtil.copy(in, out);
		    } catch (Exception e) {
		        e.getMessage();
		    }finally {
		    	try
				{
					in.close();
					os.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		    }
	}
	
	//登录验证码
	public static String stateCode(HttpSession session)
	{
	    Random rand = new Random();
	
	    String op1="";
	    int result=0;
	    int op = rand.nextInt(3);
	    int num1 = rand.nextInt(10);
	    int num2 = rand.nextInt(10);
	    switch (op)
	    {
	        case 0 : result=num1+num2;op1="+";break;
	        case 1 : result=num1-num2;op1="-";break;
	        case 2 : result=num1*num2;op1="*";break;
	    }
	    session.setAttribute("stateCode", result);
	    return String.valueOf(num1)+op1+String.valueOf(num2)+"=?";
	}
}
