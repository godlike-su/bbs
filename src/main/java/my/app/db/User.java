package my.app.db;

import java.util.Date; 

/** 本类由 POJO生成器 自动生成于 2020-03-19 16:34:00
    作者：阿发你好      官网: http://afanihao.cn 
*/ 

/** INSERT语句 ( 预处理方式 ) 
  INSERT INTO `user`
        (`id`, `name`, `password`, `sex`, `qqid`, `qq`, `qqFlag`, `qqName`, `email`, `emailFlag`, `phone`, `phoneFlag`, `thumb`, `level`, `vip`, `vipName`, `isAdmin`, `timeCreate`, `timeUpdate`, `timeLogin`)
  VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
*/

/** INSERT语句 ( MyBatis方式 ) 
  INSERT INTO `user`
        (`id`, `name`, `password`, `sex`, `qqid`, `qq`, `qqFlag`, `qqName`, `email`, `emailFlag`, `phone`, `phoneFlag`, `thumb`, `experience`, `level`, `vip`, `vipName`, `isAdmin`, `timeCreate`, `timeUpdate`, `timeLogin`)
  VALUES(#{id}, #{name}, #{password}, #{sex}, #{qqid}, #{qq}, #{qqFlag}, #{qqName}, #{email}, #{emailFlag}, #{phone}, #{phoneFlag}, #{thumb}, #{experience}, #{level}, #{vip}, #{vipName}, #{isAdmin}, #{timeCreate}, #{timeUpdate}, #{timeLogin})

  自增主键: id
*/ 

public class User 
{ 
 
	public Integer id ; 
	public String name ; 
	public String password ;
	public Boolean sex;
	public String qqid ; 
	public String qq ; 
	public Boolean qqFlag ; 
	public String qqName ; 
	public String email ; 
	public Boolean emailFlag ; 
	public String phone ; 
	public Boolean phoneFlag ; 
	public String thumb ;
	public Integer experience;
	public Integer level ; 
	public Byte vip ; 
	public String vipName ; 
	public Boolean isAdmin ; 
	public Date timeCreate ; 
	public Date timeUpdate ; 
	public Date timeLogin ; 


	public void setId(Integer id)
	{
		this.id=id;
	}
	public Integer getId()
	{
		return this.id;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getName()
	{
		return this.name;
	}
	public void setPassword(String password)
	{
		this.password=password;
	}
	public String getPassword()
	{
		return this.password;
	}
	public void setQqid(String qqid)
	{
		this.qqid=qqid;
	}
	public String getQqid()
	{
		return this.qqid;
	}
	public void setQq(String qq)
	{
		this.qq=qq;
	}
	public String getQq()
	{
		return this.qq;
	}
	public void setQqFlag(Boolean qqFlag)
	{
		this.qqFlag=qqFlag;
	}
	public Boolean getQqFlag()
	{
		return this.qqFlag;
	}
	public void setQqName(String qqName)
	{
		this.qqName=qqName;
	}
	public String getQqName()
	{
		return this.qqName;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getEmail()
	{
		return this.email;
	}
	public void setEmailFlag(Boolean emailFlag)
	{
		this.emailFlag=emailFlag;
	}
	public Boolean getEmailFlag()
	{
		return this.emailFlag;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getPhone()
	{
		return this.phone;
	}
	public void setPhoneFlag(Boolean phoneFlag)
	{
		this.phoneFlag=phoneFlag;
	}
	public Boolean getPhoneFlag()
	{
		return this.phoneFlag;
	}
	public void setThumb(String thumb)
	{
		this.thumb=thumb;
	}
	public String getThumb()
	{
		return this.thumb;
	}
	public void setLevel(Integer level)
	{
		this.level=level;
	}
	public Integer getLevel()
	{
		return this.level;
	}
	public void setVip(Byte vip)
	{
		this.vip=vip;
	}
	public Byte getVip()
	{
		return this.vip;
	}
	public void setVipName(String vipName)
	{
		this.vipName=vipName;
	}
	public String getVipName()
	{
		return this.vipName;
	}
	public void setIsAdmin(Boolean isAdmin)
	{
		this.isAdmin=isAdmin;
	}
	public Boolean getIsAdmin()
	{
		return this.isAdmin;
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
	public void setTimeLogin(Date timeLogin)
	{
		this.timeLogin=timeLogin;
	}
	public Date getTimeLogin()
	{
		return this.timeLogin;
	}

} 
 