package my.app.db; 

import java.util.Date; 

/** 本类由 POJO生成器 自动生成于 2020-04-02 00:58:43
    作者：阿发你好      官网: http://afanihao.cn 
*/ 

/** INSERT语句 ( 预处理方式 ) 
  INSERT INTO `message`
        (`id`, `creator`, `title`, `content`, `cat1`, `cat2`, `cat3`, `ref1`, `ref2`, `refId`, `refstr`, `timeCreate`, `timeUpdate`, `niceFlag`, `topFlag`, `banFlag`, `delFlag`, `closeFlag`, `numReply`, `numLike`, `storePath`, `imgCount`, `img1`, `img2`, `img3`, `replyUser`, `replyName`, `replyTime`, `replyText`) 
  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
*/ 

/** INSERT语句 ( MyBatis方式 ) 
  INSERT INTO `message`
        (`id`, `creator`, `title`, `content`, `cat1`, `cat2`, `cat3`, `ref1`, `ref2`, `refId`, `refstr`, `timeCreate`, `timeUpdate`, `niceFlag`, `topFlag`, `banFlag`, `delFlag`, `closeFlag`, `numReply`, `numLike`, `storePath`, `imgCount`, `img1`, `img2`, `img3`, `replyUser`, `replyName`, `replyTime`, `replyText`) 
  VALUES(#{id}, #{creator}, #{title}, #{content}, #{cat1}, #{cat2}, #{cat3}, #{ref1}, #{ref2}, #{refId}, #{refstr}, #{timeCreate}, #{timeUpdate}, #{niceFlag}, #{topFlag}, #{banFlag}, #{delFlag}, #{closeFlag}, #{numReply}, #{numLike}, #{storePath}, #{imgCount}, #{img1}, #{img2}, #{img3}, #{replyUser}, #{replyName}, #{replyTime}, #{replyText}) 

  自增主键: id
*/ 

public class Message 
{ 
 
	public Long id ; 
	public Integer creator ; 
	public String title ; 
	public String content ; 
	public Integer cat1 ; 
	public Integer cat2 ; 
	public Integer cat3 ; 
	public Long ref1 ; 
	public Long ref2 ; 
	public Integer refId ; 
	public String refstr ; 
	public Date timeCreate ; 
	public Date timeUpdate ; 
	public Integer niceFlag ; 
	public Integer topFlag ; 
	public Boolean banFlag ; 
	public Boolean delFlag ; 
	public Boolean closeFlag ; 
	public Integer numReply ; 
	public Integer numLike ; 
	public String storePath ; 
	public Integer imgCount ; 
	public String img1 ; 
	public String img2 ; 
	public String img3 ; 
	public Integer replyUser ; 
	public String replyName ; 
	public Date replyTime ; 
	public String replyText ; 


	public void setId(Long id)
	{
		this.id=id;
	}
	public Long getId()
	{
		return this.id;
	}
	public void setCreator(Integer creator)
	{
		this.creator=creator;
	}
	public Integer getCreator()
	{
		return this.creator;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setCat1(Integer cat1)
	{
		this.cat1=cat1;
	}
	public Integer getCat1()
	{
		return this.cat1;
	}
	public void setCat2(Integer cat2)
	{
		this.cat2=cat2;
	}
	public Integer getCat2()
	{
		return this.cat2;
	}
	public void setCat3(Integer cat3)
	{
		this.cat3=cat3;
	}
	public Integer getCat3()
	{
		return this.cat3;
	}
	public void setRef1(Long ref1)
	{
		this.ref1=ref1;
	}
	public Long getRef1()
	{
		return this.ref1;
	}
	public void setRef2(Long ref2)
	{
		this.ref2=ref2;
	}
	public Long getRef2()
	{
		return this.ref2;
	}
	public void setRefId(Integer refId)
	{
		this.refId=refId;
	}
	public Integer getRefId()
	{
		return this.refId;
	}
	public void setRefstr(String refstr)
	{
		this.refstr=refstr;
	}
	public String getRefstr()
	{
		return this.refstr;
	}
	public void setTimeCreate(Date timeCreate)
	{
		this.timeCreate=timeCreate;
	}
	public Date getTimeCreate()
	{
		return this.timeCreate;
	}
	public void setTimeUpdate(Date timeUpdate)
	{
		this.timeUpdate=timeUpdate;
	}
	public Date getTimeUpdate()
	{
		return this.timeUpdate;
	}
	public void setNiceFlag(Integer niceFlag)
	{
		this.niceFlag=niceFlag;
	}
	public Integer getNiceFlag()
	{
		return this.niceFlag;
	}
	public void setTopFlag(Integer topFlag)
	{
		this.topFlag=topFlag;
	}
	public Integer getTopFlag()
	{
		return this.topFlag;
	}
	public void setBanFlag(Boolean banFlag)
	{
		this.banFlag=banFlag;
	}
	public Boolean getBanFlag()
	{
		return this.banFlag;
	}
	public void setDelFlag(Boolean delFlag)
	{
		this.delFlag=delFlag;
	}
	public Boolean getDelFlag()
	{
		return this.delFlag;
	}
	public void setCloseFlag(Boolean closeFlag)
	{
		this.closeFlag=closeFlag;
	}
	public Boolean getCloseFlag()
	{
		return this.closeFlag;
	}
	public void setNumReply(Integer numReply)
	{
		this.numReply=numReply;
	}
	public Integer getNumReply()
	{
		return this.numReply;
	}
	public void setNumLike(Integer numLike)
	{
		this.numLike=numLike;
	}
	public Integer getNumLike()
	{
		return this.numLike;
	}
	public void setStorePath(String storePath)
	{
		this.storePath=storePath;
	}
	public String getStorePath()
	{
		return this.storePath;
	}
	public void setImgCount(Integer imgCount)
	{
		this.imgCount=imgCount;
	}
	public Integer getImgCount()
	{
		return this.imgCount;
	}
	public void setImg1(String img1)
	{
		this.img1=img1;
	}
	public String getImg1()
	{
		return this.img1;
	}
	public void setImg2(String img2)
	{
		this.img2=img2;
	}
	public String getImg2()
	{
		return this.img2;
	}
	public void setImg3(String img3)
	{
		this.img3=img3;
	}
	public String getImg3()
	{
		return this.img3;
	}
	public void setReplyUser(Integer replyUser)
	{
		this.replyUser=replyUser;
	}
	public Integer getReplyUser()
	{
		return this.replyUser;
	}
	public void setReplyName(String replyName)
	{
		this.replyName=replyName;
	}
	public String getReplyName()
	{
		return this.replyName;
	}
	public void setReplyTime(Date replyTime)
	{
		this.replyTime=replyTime;
	}
	public Date getReplyTime()
	{
		return this.replyTime;
	}
	public void setReplyText(String replyText)
	{
		this.replyText=replyText;
	}
	public String getReplyText()
	{
		return this.replyText;
	}

} 
 