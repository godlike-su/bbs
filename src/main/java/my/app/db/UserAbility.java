package my.app.db;

import java.util.Date; 

/** 本类由 POJO生成器 自动生成于 2020-03-19 16:34:00
    作者：阿发你好      官网: http://afanihao.cn 
*/ 

/** INSERT语句 ( 预处理方式 ) 
  INSERT INTO `user_ability`
        (`userId`, `banFlag`, `banDate`, `imageCount`, `imageMax`, `msgCount`, `msgMax`, `replyCount`, `replyMax`) 
  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) 
*/

/** INSERT语句 ( MyBatis方式 ) 
  INSERT INTO `user_ability`
        (`userId`, `banFlag`, `banDate`, `imageCount`, `imageMax`, `msgCount`, `msgMax`, `replyCount`, `replyMax`) 
  VALUES(#{userId}, #{banFlag}, #{banDate}, #{imageCount}, #{imageMax}, #{msgCount}, #{msgMax}, #{replyCount}, #{replyMax}) 

  自增主键: 无
*/ 

public class UserAbility 
{ 
 
	public Integer userId ; 
	public Integer banFlag ; 
	public Date banDate ; 
	public Integer imageCount ; 
	public Integer imageMax ; 
	public Integer msgCount ; 
	public Integer msgMax ; 
	public Integer replyCount ; 
	public Integer replyMax ; 


	public void setUserId(Integer userId)
	{
		this.userId=userId;
	}
	public Integer getUserId()
	{
		return this.userId;
	}
	public void setBanFlag(Integer banFlag)
	{
		this.banFlag=banFlag;
	}
	public Integer getBanFlag()
	{
		return this.banFlag;
	}
	public void setBanDate(Date banDate)
	{
		this.banDate=banDate;
	}
	public Date getBanDate()
	{
		return this.banDate;
	}
	public void setImageCount(Integer imageCount)
	{
		this.imageCount=imageCount;
	}
	public Integer getImageCount()
	{
		return this.imageCount;
	}
	public void setImageMax(Integer imageMax)
	{
		this.imageMax=imageMax;
	}
	public Integer getImageMax()
	{
		return this.imageMax;
	}
	public void setMsgCount(Integer msgCount)
	{
		this.msgCount=msgCount;
	}
	public Integer getMsgCount()
	{
		return this.msgCount;
	}
	public void setMsgMax(Integer msgMax)
	{
		this.msgMax=msgMax;
	}
	public Integer getMsgMax()
	{
		return this.msgMax;
	}
	public void setReplyCount(Integer replyCount)
	{
		this.replyCount=replyCount;
	}
	public Integer getReplyCount()
	{
		return this.replyCount;
	}
	public void setReplyMax(Integer replyMax)
	{
		this.replyMax=replyMax;
	}
	public Integer getReplyMax()
	{
		return this.replyMax;
	}

} 
 