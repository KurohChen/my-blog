package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Value("${community.path.upload}")
    private String uploadPath;

    @Value("${community.path.domain}")
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @LoginRequired
    @RequestMapping(path="/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @RequestMapping(path="/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage == null){
            model.addAttribute("error","????????????????????????");
            return null;
        }
        String fileName = headerImage.getOriginalFilename();
        // ????????????
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isBlank(suffix)){
            model.addAttribute("error","????????????????????????");
            return "/site/setting";
        }
        // ????????????????????????
        fileName = CommunityUtil.generateUUID()+suffix;
        // ??????????????????
        File dest = new File(uploadPath+"/",fileName);
        try {
            headerImage.transferTo(dest);
        } catch (IOException e) {
            logger.error("??????????????????:"+e.getMessage());
            throw new RuntimeException("??????????????????????????????????????????");
        }

        //?????????????????????????????????web???
        // http://localhost:8080/community/user/header/xxx.png
        User user = hostHolder.getUser();
        String headUrl = domain + contextPath + "/user/header/" + fileName;
        userService.updateHeader(user.getId(),headUrl);

        return "redirect:/index";

    }

    @RequestMapping(path="/header/{fileName}", method = RequestMethod.GET)
    public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response){
        //?????????????????????
        fileName = uploadPath + "/" + fileName;
        //????????????
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
        //????????????
        response.setContentType("image/"+suffix);
        try (
                //???????????????????????????????????????
                //?????????:????????????
                OutputStream os = response.getOutputStream();
                //????????????????????????????????????????????????????????????
                FileInputStream fis = new FileInputStream(fileName);
        ) {

            //???????????????
            byte[] buffer = new byte[1024];
            int b = 0;
            // b = -1 ??????????????????????????????
            while ((b = fis.read(buffer)) != -1 ){
                os.write(buffer,0,b);
            }
        } catch (IOException e) {
            logger.error("?????????????????????"+e.getMessage());
        }
    }

    @LoginRequired
    @RequestMapping(path = "/password",method = RequestMethod.POST)
    public String login(String originPassword, String newPassword, Model model, HttpSession session, HttpServletResponse response){
        //????????????
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(originPassword,newPassword,user);
        if(map.containsKey("originPassword")){
            model.addAttribute("originPassword",map.get("originPassword"));
            return "/site/setting";
        }
        if(map.containsKey("newPassword")){
            model.addAttribute("newPassword",map.get("newPassword"));
            return "/site/setting";
        }

        return "redirect:/index";
    }

    //????????????
    @RequestMapping(path = "/profile/{userId}", method=RequestMethod.GET)
    public String getProfile(@PathVariable("userId") int userId,Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("??????????????????");
        }

        //??????
        model.addAttribute("user",user);
        //????????????
        int likeCount = likeService.findUserLikeCount(userId);
        model.addAttribute("likeCount",likeCount);

        //????????????
        long followeeCount = followService.findFolloweeCount(userId,ENTITY_TYPE_USER);
        model.addAttribute("followeeCount",followeeCount);

        //????????????
        long followerCount = followService.findFollowerCount(ENTITY_TYPE_USER, userId);
        model.addAttribute("followerCount",followerCount);

        //???????????????
        boolean hasFollowed = false;
        if(hostHolder.getUser() != null){
            hasFollowed = followService.hasFollowed(hostHolder.getUser().getId(),ENTITY_TYPE_USER,userId);
        }
        model.addAttribute("hasFollowed",hasFollowed);

        return "/site/profile";
    }

}
