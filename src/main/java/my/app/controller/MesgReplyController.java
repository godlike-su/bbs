package my.app.controller;

import af.spring.AfRestData;
import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.entity.Message;
import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.service.MesgReplyService;
import my.app.service.MessageService;
import my.app.service.UserAbilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MesgReplyController
{
    @Autowired
    private UserAbilityService userAbilityService;

    @Autowired
    private MesgReplyService mesgReplyService;

    @Autowired
    private MessageService messageService;

    //发表主贴
    /*
    sql.selectOne("message.selecctOne", map); 查看原帖，map函数传入 map.put("msgId", msgId); 帖子的id来查询
    msgId2 为 为回复 人家回复的帖，为0则为回复原帖
     */
    @GetMapping("/u/reply/add")
    public String add(Model model,
                      @SessionAttribute User user,
                      long msgId,
                      long msgId2) throws Exception
    {
        // 取出原贴
//        String s1 = "select * from message where id=" + msgId;
        Message ref = mesgReplyService.selectMessageOne(msgId);

        if(ref == null)
            throw new Exception("无此记录, messageId=" + msgId);

        model.addAttribute("ref", ref);
        model.addAttribute("msgId2", msgId2);

        return "reply/add";
    }

    //发表回复帖子保存
    /*
    Map函数
    1、首先放入msgId map.put("msgId", msgId); sql.selectOne("message.selecctOne", map);
    相当于帖子id用来查询原帖
    2、主贴更新
    map.put("msgId", msgId);   msgId 为主贴 id
    map.put("replyUser", row.creator);  //回复用户id
    map.put("replyText", row.title);     //回复文本
    map.put("replyName", user.name);       // 回复用户名
    map.put("replyTime", sdf.format(new Date()));   //回复时间
    sql.update("message.updateOne", map);
     */
    @PostMapping("/u/reply/save.do")
    public Object save(@RequestBody JSONObject jreq,
                       @SessionAttribute User user,
                       @SessionAttribute UserAbility userAbility) throws Exception
    {
        // 保存之前，检查用户的权利
        if(userAbility.msgCount <= 0)
            return new AfRestError("今天回复数量太多，请明天再试！MAX=" + userAbility.msgMax);

        // 取出原帖子（此处可以优化，没必要取出所有字段)
        long msgId = jreq.getLongValue("msgId");
        //取出回复贴子
        long msgId2 = jreq.getLongValue("msgId2");

        //原文帖子 ref
        Message ref = null;
        if(msgId2==0)
        {
            ref = mesgReplyService.selectMessageOne(msgId);
        }
        else
        {
            ref = mesgReplyService.selectMessageOne(msgId2);
        }

        if(ref == null)
            return new AfRestError("无此记录, messageId=" + msgId);

        // 创建一条回复记录
        Message row = new Message();
        row.creator = user.id; // 创建者
        row.cat1 = ref.cat1;
        row.cat2 = ref.cat2;
        row.cat3 = ref.cat3;
        row.title = jreq.getString("title"); // title即缩略显示
        row.content = jreq.getString("content"); // 回复内容
        row.ref1= msgId; // 父级ID (即1楼原文ID)
        row.ref2= msgId2;   //2级ID(即回复楼ID)
        row.refId = ref.replyUser;     //回复层楼的Id
        row.refstr = ref.replyName;       //回复层楼的用户名
        row.timeCreate=new Date();
        row.numReply=0;
        row.numLike =0;
        row.imgCount = 0;
        row.niceFlag = 0;
        row.topFlag = 0;
        row.banFlag = false;
        row.delFlag = false;
        row.closeFlag = false;
        row.timeCreate = new Date();
        row.storePath = MessageController.makeStorePath();
        row.replyText = ref.title;
        row.replyUser = user.id;        //回复贴自己的id
        row.replyName = user.name;      //回复贴自己的姓名

        if(row.img1==null) row.img1="";
        if(row.img2==null) row.img2="";
        if(row.img3==null) row.img3="";

        // 插入回复并更新原帖统计数据
        mesgReplyService.replyMessageOne(row);

        // 扣除用户权利
        userAbilityService.useReplyCount(userAbility);

        return new AfRestData("");
    }

    /*
     * 不需要登录，即可浏览，所以不加 /u 前缀，帖子内容
     */
    @GetMapping("/reply/list")
    public String list(Model model,
                       Long msgId,
                       Integer pageNumber,
                       HttpServletRequest request) throws Exception
    {
        if(pageNumber==null) pageNumber=1;

        //取出原文,ref
        Map<String, Object> ref = mesgReplyService.selectReplyMessageOne(msgId, request);
        if(ref == null)
            throw new Exception("无此记录 ，messageId=" + msgId);
        else
        {
            model.addAttribute("ref", ref);
        }

        //放入map
        Map<String, Object> map = new HashMap<>();
        map.put("ref1", msgId); //1楼，1级主帖子,回复的帖子的id
        map.put("delFlag", 0); // 没有删除标识的

        //count：符合条件的记录一共有多少条
        int count = messageService.selectMessageCount(map);

        //一页显示的数据量
        int pageSize = 10;
        int pageCount = count / pageSize;
        if(count % pageSize != 0) pageCount += 1;

        //查询
        //开始查询的信息index
        int startIndex = pageSize * (pageNumber-1);
        map.put("startIndex", startIndex);
        map.put("pageSize", pageSize);      //显示数据量

        // 返回的每一行记录是一个Map<String,String>
        List<Map> messageList = mesgReplyService.replyMessageList(map);

        model.addAttribute("messageList",messageList);
        model.addAttribute("count",count);  //总数量
        model.addAttribute("pageCount",pageCount);  //总页数
        model.addAttribute("pageNumber",pageNumber);  //当前页码：1,2,3...
        model.addAttribute("baseIndex", pageSize * (pageNumber-1)); //startIndex

        return "reply/list";
    }



}
