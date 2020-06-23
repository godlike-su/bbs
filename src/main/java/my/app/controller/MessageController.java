package my.app.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import af.spring.AfRestError;
import com.alibaba.fastjson.JSONObject;
import my.app.entity.Message;
import my.app.entity.User;
import my.app.entity.UserAbility;
import my.app.service.*;
import my.app.util.*;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import af.spring.AfRestData;

@Controller
public class MessageController
{
	// 图片的存储位置
	@Autowired
    private MsgDelTaskService msgDelTaskService;

    // 临时图片的存储位置
	@Autowired
    private TmpFileService tmpFileService;

	@Autowired
    private UserAbilityService userAbilityService;

	@Autowired
    private UserService userService;

	@Autowired
    private MessageService messageService;

    //发表主贴
    @GetMapping("/u/message/add")
    public String add(Model model,
                     @SessionAttribute User user,
                      @SessionAttribute UserAbility userAbility) throws Exception
    {
        return "message/add";
    }

    //发表主贴，插入数据库
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
        row.replyName = user.name;
        if (row.img1 == null)
            row.img1 = "";
        if (row.img2 == null)
            row.img2 = "";
        if (row.img3 == null)
            row.img3 = "";

        // 附件图片存储路径 示例 201911/01/15725791906031/
        row.storePath = makeStorePath();

        //图片处理
        row.img1 = moveTmpToStore(row.storePath, row.img1, userAbility);
        row.img2 = moveTmpToStore(row.storePath, row.img2, userAbility);
        row.img3 = moveTmpToStore(row.storePath, row.img3, userAbility);

        messageService.insertMes(row);

        //扣除当天的额度
        // 扣除用户权利
        //经验 + 2
        userAbilityService.useMsgCount(userAbility);
        userService.setExperience(user, 2);

        return new AfRestData("");
    }

    // 附件图片存储路径 示例 202001/01/15725791906031/
    public static String makeStorePath()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM/dd/");
        return sdf.format(new Date()) + MyUtil.guid2() + "/";
    }

    // 将临时文件存储到Store，并返回存储路径 (此处只存储文件名)
    private String moveTmpToStore(String storePath,	String tmpName, UserAbility userAbility)
    {
        if(tmpName==null || tmpName.length()==0) return "";

        File tmpFile = tmpFileService.store.getFile(tmpName);

        //获取入库图片路径
        File storeDir = msgDelTaskService.store.getFile(storePath);
        System.out.println("tmp: " + tmpFile.getAbsolutePath());
        System.out.println("storeDir: " + storeDir.getAbsolutePath());
        try
        {
            FileUtils.moveFileToDirectory(tmpFile, storeDir, true);
            //图片-1
            userAbilityService.useImageCount(userAbility);
            return tmpName;
        }catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    //列表页，无需登录，帖子总数，首页
    @GetMapping("/message/list")
    public String list(Model model,
                       HttpSession session,
                       Integer pageNumber,
                       String filter,
                       Integer niceFlag) throws Exception
    {
        Map<String, Object> map = new HashMap<>();

        //查询条件
        if(pageNumber==null) pageNumber=1;
        if(filter==null) filter = "";
        if(niceFlag==null)  niceFlag=0;

        map.put("ref1", 0); //1楼，1级主帖子
        map.put("delFlag", 0); // 没有删除标识的
        map.put("filter", filter.trim());  //筛选器
        map.put("niceFlag", niceFlag);

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
        map.put("pageSize", pageSize);

        // 返回的每一行记录是一个Map<String,String>, ref
        List<Map> messageList = messageService.homeMessageList(map);

        model.addAttribute("messageList",messageList);
        model.addAttribute("count",count);  //总数量
        model.addAttribute("pageCount",pageCount);  //总页数
        model.addAttribute("pageNumber",pageNumber);  //当前页码：1,2,3...
        model.addAttribute("baseIndex", count - pageSize * (pageNumber-1)); //总数量
        model.addAttribute("filter", filter);
        model.addAttribute("niceFlag", niceFlag);
        return "message/list";
    }

    // 用户对帖子的操作
    @PostMapping("/u/message/userSetFlags.do")
    public Object PostMapping(@SessionAttribute User user,
                              @RequestBody JSONObject jreq) throws Exception
    {
        long msgId = jreq.getLongValue("msgId");
        String cmd = jreq.getString("cmd");
        int creator = jreq.getIntValue("creator");
        if(cmd.equals("del"))
        {
            //返回的数据
            if(creator != user.id)    // Integer的比较用equals
            {
                // 不能删除别人的帖子
                return new AfRestError("无权删除别人的帖子");
            }
            //用户删自己的帖子，以及它的子帖
            Map<String, Object> map = new HashMap<>();
            map.put("msgId", msgId);
            messageService.deleteMessage(map);
        }
        else if(cmd.equals("delref"))
        {
            //返回的数据
            if(creator != user.id)    // Integer的比较用equals
            {
                // 不能删除别人的回复
                return new AfRestError("无权删除别人的回复");
            }
            //用户删自己的回复,以及子回复
            Map<String, Object> map = new HashMap<>();
            map.put("msgId", msgId);
            messageService.deleteMessage(map);
        }
        return new AfRestData("");
    }

    // 管理员操作帖子
    @PostMapping("/u/message/suSetFlags.do")
    public Object suSetFlags(@SessionAttribute User user,
                             @RequestBody JSONObject jreq) throws Exception
    {
        if(user.isAdmin==null || user.isAdmin==false)
            return new AfRestError("仅管理员可以操作");

        long msgId = jreq.getLongValue("msgId");
        String cmd = jreq.getString("cmd");

        //传入数据和需要操作的指令
        int i = messageService.rootUpdateMessageState(msgId, cmd);
        if(i==0)
            return new AfRestError("无此msgId: " + msgId);

        return new AfRestData("");
    }
}
