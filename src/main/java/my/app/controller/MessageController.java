package my.app.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.db.Message;
import my.app.db.UserAbility;
import my.app.support.MyBatis;
import my.app.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import af.spring.AfRestData;
import my.app.db.User;

@Controller
public class MessageController
{
	// 图片的存储位置
	public static FileStore store 
		= new FileStore("c:/bbsfile/message/", "/bbsfile/message/");
	
    //发表主贴
    @GetMapping("/u/message/add")
    public String add(Model model,
                     @SessionAttribute User user,
                      @SessionAttribute UserAbility userAbility) throws Exception
    {
        return "message/add";
    }

    //保存帖子
    /*
    发表主贴，插入数据库
    sqlsession.insert("message.insertMes", row);
     */
    @PostMapping("/u/message/save.do")
    public Object save(@RequestBody Message row,
                       @SessionAttribute User user,
                       @SessionAttribute UserAbility userAbility,
                       HttpServletRequest request) throws Exception
    {
        // 保存之前，检查用户的权利
        if(userAbility.msgCount <= 0)
            return new AfRestError("今天发贴数量太多，请明天再试！MAX=" + userAbility.msgMax);

//      title, content字段由前端传送过来
        row.creator = user.id;  //创建者
        row.cat1 = row.cat2 = row.cat3 = 0;
        row.ref1 = 0L;  // ref1=0表示这一条为主帖(楼主)
        row.ref2 = 0L;
        row.timeCreate = new Date();    //创建时间
        row.refId = user.id;     //自己的id
        row.refstr = user.name;       //自己的用户名
        row.numLike = 0;
        row.numReply = 0;
        row.imgCount = 0;
        row.niceFlag = 0;
        row.topFlag = 0;
        row.banFlag = false;
        row.delFlag = false;
        row.closeFlag = false;
        row.replyUser = user.id;
        row.replyTime = row.timeCreate;
//        row.replyText = row.content;
        row.replyName = user.name;
        row.replyUser = user.id;
        if (row.img1 == null)
            row.img1 = "";
        if (row.img2 == null)
            row.img2 = "";
        if (row.img3 == null)
            row.img3 = "";

        // 附件图片存储路径 示例 201911/01/15725791906031/
        row.storePath = makeStorePath();

        //图片处理
        row.img1 = moveTmpToStore(request, row.storePath, row.img1, userAbility);
        row.img2 = moveTmpToStore(request, row.storePath, row.img2, userAbility);
        row.img3 = moveTmpToStore(request, row.storePath, row.img3, userAbility);


        try(SqlSession sqlsession = MyBatis.factory.openSession()){
            sqlsession.insert("message.insertMes", row);
            sqlsession.commit();
        }

        //扣除当天的额度
        // 扣除用户权利
        //经验 + 5
        try {
            UserAbilityUtil.useMsgCount(userAbility);
            UserUtil.setExperience(user, 2);
        }catch(Exception e)
        {
        }

        return new AfRestData("");
    }

    // 将临时文件存储到Store，并返回存储路径 (此处只存储文件名)
    private String moveTmpToStore(HttpServletRequest request
            , String storePath,	String tmpName, UserAbility userAbility)
    {
        if(tmpName==null || tmpName.length()==0) return "";

        File tmpFile = MesgImgController.store.getFile(tmpName);

        //获取入库图片路径
//        FileStore store = new FileStore(request) ;
        File storeDir = this.store.getFile(storePath);
        System.out.println("tmp: " + tmpFile.getAbsolutePath());
        System.out.println("storeDir: " + storeDir.getAbsolutePath());
        try
        {
            FileUtils.moveFileToDirectory(tmpFile, storeDir, true);
            //String path = storePath + tmpName;
            //图片-1
            UserAbilityUtil.useImageCount(userAbility);
            return tmpName;
        } catch (IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    //列表页，无需登录，帖子总数，首页
    /*
    map.put("ref1", 0); //1楼，1级主帖子
    map.put("delFlag", 0); // 没有删除标识的
    map.put("filter", filter.trim());  //筛选器
    map.put("niceFlag", niceFlag);  //精华帖
    //count：符合条件的记录一共有多少条
    count = sql.selectOne("message.selectCount", map);
    //开始查询的信息index   limit(startIndex, pageSize)
    map.put("startIndex", startIndex);      //开始序号
    map.put("pageSize", pageSize);          //显示的数量

    // 返回的每一行记录是一个Map<String,String>, ref
    messageList = sql.selectList("message.selectList", map);
     */
    @GetMapping("/message/list")
    public String list(Model model,
                       HttpSession session,
                       Integer pageNumber,
                       String filter,
                       Integer niceFlag) throws Exception
    {
        //查询条件
        if(pageNumber==null) pageNumber=1;
        if(filter==null) filter = "";
        if(niceFlag==null) niceFlag=0;

        Map<String, Object> map = new HashMap<>();
        map.put("ref1", 0); //1楼，1级主帖子
        map.put("delFlag", 0); // 没有删除标识的
        map.put("filter", filter.trim());  //筛选器
        map.put("niceFlag", niceFlag);  //

        //count：符合条件的记录一共有多少条
        int count = 0;
        try(SqlSession sql =  MyBatis.factory.openSession()){
            count = sql.selectOne("message.selectCount", map);
        }

        //一页显示的数据量
        int pageSize = 10;
        int pageCount = count / pageSize;
        if(count % pageSize != 0) pageCount += 1;

        //查询
        //开始查询的信息index
        int startIndex = pageSize * (pageNumber-1);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);

        // 返回的每一行记录是一个Map<String,String>, ref
        List<Map> messageList = null;
        try(SqlSession sql = MyBatis.factory.openSession()){
            messageList = sql.selectList("message.selectList", map);
        }

        // 需要进一步处理处理成前端需要的格式
        TimeStrUtil tts = new TimeStrUtil();
        for(Map m : messageList)
        {
            String timeCreate =  m.get("timeCreate").toString();
            m.put("timeCreate", tts.format(timeCreate));

            String replyTime = (String) m.get("replyTime").toString();
            m.put("replyTime", tts.format(replyTime));
        }

        model.addAttribute("messageList",messageList);
        model.addAttribute("count",count);  //总数量
        model.addAttribute("pageCount",pageCount);  //总页数
        model.addAttribute("pageNumber",pageNumber);  //当前页码：1,2,3...
        model.addAttribute("baseIndex", count - pageSize * (pageNumber-1)); //总数量
        model.addAttribute("filter", filter);

        model.addAttribute("niceFlag", niceFlag);
        return "message/list";
    }

    // 附件图片存储路径 示例 201911/01/15725791906031/
    public static String makeStorePath()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
        return sdf.format(new Date()) + MyUtil.guid2() + "/";
    }

    // 用户对帖子的操作
    /*
    用户删自己的帖子
     map.put("msgId", msgId);   //msgId为 帖子的 id
     ref = sql.selectOne("message.selecctOne", map);    //查看该帖子是否自己发的，是否有该帖子
     sql.update("message.UpDel", map);      //将 id 相关联的帖子ref1,ref2为id的帖子的delFlag修改为 1,凌晨统一删除

     */
    @PostMapping("/u/message/userSetFlags.do")
    public Object PostMapping(@SessionAttribute User user,
                              @RequestBody JSONObject jreq) throws Exception
    {
        long msgId = jreq.getLongValue("msgId");
        long msgId2 = jreq.getLongValue("msgId2");
        String cmd = jreq.getString("cmd");
        if(cmd.equals("del"))
        {
            //用户删自己的帖子
            // "select id,creator from message where id=" + msgId;
            Map<String, Object> map = new HashMap<>();
            map.put("msgId", msgId);

            //返回的数据
            Message ref = null;
            try(SqlSession sql = MyBatis.factory.openSession()){
                ref = sql.selectOne("message.selecctOne", map);
            }
            if(ref.creator.intValue() != user.id)    // Integer的比较用equals
            {
                // 不能删除别人的帖子
                return new AfRestError("无权删除别人的帖子");
            }

            // 删除此贴，以及它的子帖
            //update message set delFlag=1 WHERE id=%d OR ref1=%d "
//            map.put("id", msgId);
            try(SqlSession sql = MyBatis.factory.openSession()){
                sql.update("message.UpDel", map);
                sql.commit();
            }
        }
        else if(cmd.equals("delref"))
        {
            //用户删自己的回复
            // "select id,creator from message where id=" + msgId;
            Map<String, Object> map = new HashMap<>();
            map.put("msgId", msgId);

            //返回的数据
            Message ref = null;
            try(SqlSession sql = MyBatis.factory.openSession()){
                ref = sql.selectOne("message.selecctOne", map);
            }
            if(ref.creator.intValue() != user.id)    // Integer的比较用equals
            {
                // 不能删除别人的回复
                return new AfRestError("无权删除别人的回复");
            }

            // 删除回复
//            map.put("id", msgId);
            try(SqlSession sql = MyBatis.factory.openSession()){
                sql.update("message.UpDel", map);
                sql.commit();
            }
        }
        return new AfRestData("");
    }

    // 管理操作
    /*
    map.put("msgId", msgId);    //msgId为帖子的id
    ref = sql.selectOne("message.selecctOne", map);     //查看是否有该帖子
    if(cmd.equals("top"))
            map.put("topFlag", 1);      //执行置顶操作
    else if(cmd.equals("nice"))
        map.put("niceFlag", 1);         //执行 加精帖子
    else if(cmd.equals("cantop"))
        map.put("topFlag", 0);          //执行 取消置顶
    else if(cmd.equals("cannice"))
        map.put("niceFlag", 0);         //执行 取消加精帖子
    else if(cmd.equals("del"))
        map.put("delFlag", 1);          //删除帖子

    sql.update("message.suSetFlags", map);
    map.put("ref1", msgId);     //ref1为主贴下面的回复，删除主贴，回复也需要删除
     sql.update("message.UpDel", map);
     */
    @PostMapping("/u/message/suSetFlags.do")
    public Object suSetFlags(@SessionAttribute User user,
                             @RequestBody JSONObject jreq) throws Exception
    {
        if(user.isAdmin==null || user.isAdmin==false)
            return new AfRestError("仅管理员可以操作");

        long msgId = jreq.getLongValue("msgId");
//        long msgId2 = jreq.getLongValue("msgId2");
        String cmd = jreq.getString("cmd");

        //"select id,creator from message where id=" + msgId;
        Map<String, Object> map = new HashMap<>();
        map.put("msgId", msgId);

        //返回的数据
        Message ref = null;
        try(SqlSession sql = MyBatis.factory.openSession()){
            ref = sql.selectOne("message.selecctOne", map);
        }
        if(ref == null)
            return new AfRestError("无此记录, msgId=" + msgId);

        // 设置 topFlag/niceFlag/delFlag 标识
        if(cmd.equals("top"))
            map.put("topFlag", 1);      //置顶
        else if(cmd.equals("nice"))
            map.put("niceFlag", 1);     //加精
        else if(cmd.equals("cantop"))
            map.put("topFlag", 0);      //取消置顶
        else if(cmd.equals("cannice"))
            map.put("niceFlag", 0);     //取消加精
        else if(cmd.equals("del"))
            map.put("delFlag", 1);      //删除

        try(SqlSession sql = MyBatis.factory.openSession()){
            sql.update("message.suSetFlags", map);
            sql.commit();
        }

        // 如果是删除操作，还需要把这条帖子的回复都删除
        if(cmd.equals("del"))
        {
            map.put("ref1", msgId);
            try(SqlSession sql = MyBatis.factory.openSession()){
                sql.update("message.UpDel", map);
                sql.commit();
            }
        }

        return new AfRestData("");
    }
}
