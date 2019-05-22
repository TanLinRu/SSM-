/**
 * @program: SSM
 * @description: 首页控制器
 * @author: TLQ
 * @create: 2019-05-11 12:03
 **/
package com.tlq.controller;

import com.tlq.common.DateUtils;
import com.tlq.common.StringUtil;
import com.tlq.entity.*;
import com.tlq.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class IndexJspController extends BaseController{
    private final static Logger log = Logger.getLogger(IndexJspController.class);

    @Autowired
    private SolrService solrService;
    @Autowired
    private UserContentService userContentService;
    @Autowired
    private UpvoteService upvoteService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;




    /**
     *@Description: 分页
     *@Param: [model, id, pageNum, pageSize, request]
     *@return: java.lang.String
     *@Author: TLQ
     *@date: 2019/5/11 14:56
    **/
    @RequestMapping("/index_list")
    public String findAllList(Model model, @RequestParam(value = "keyword",required = false) String keyword,
                              @RequestParam(value = "pageNum",required = false) Integer pageNum ,
                              @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        log.info( "===========进入index_list=========" );
        User user = (User)getSession().getAttribute("user");
        if(user!=null){
            model.addAttribute( "user",user );
        }
        if(StringUtils.isNotBlank(keyword)){
            Page<UserContent> page = solrService.findByKeyWords( keyword ,pageNum,pageSize);
            model.addAttribute("keyword", keyword);
            model.addAttribute("page", page);
        }else {
            Page<UserContent> page =  findAll(pageNum,pageSize);
            model.addAttribute( "page",page );
        }
        return "../index";
    }

    @RequestMapping("/loginout")
    public String exit(Model model, HttpSession session) {
        log.info("退出登录");
        session.removeAttribute("user");
//       强制销毁相关会话信息
        session.invalidate();
        return "../login";
    }

    /**
     *@Description: 点赞
     *@Param: [model, id, uid, upvote, session]
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: TLQ
     *@date: 2019/5/11 20:01
    **/

    @RequestMapping("/upvote")
    @ResponseBody
    public Map<String,Object> upvote(Model model, @RequestParam(value = "id",required = false) long id,
                                     @RequestParam(value = "uid",required = false) Long uid,
                                     @RequestParam(value = "upvote",required = false) int upvote,
                                     HttpSession session) {
        log.info( "id="+id+",uid="+uid+"upvote="+upvote );
        LinkedHashMap map = new LinkedHashMap<String,Object>();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            map.put("data","fail");
            return map;
        }
        Upvote upvote1 = new Upvote();
        upvote1.setContentId(id);
        upvote1.setuId(user.getId());
        Upvote upv = upvoteService.findByUidAndConld(upvote1);
        if (upv != null) {
            log.info( upv.toString()+"============" );
        }
        UserContent userContent = new UserContent();
        if(upvote == -1){
            if(upv != null ){
                if( "1".equals( upv.getDownvote() ) ){
                    map.put( "data","down" );
                    return map;
                }else {
                    upv.setDownvote( "1" );
                    upv.setUpvoteTime( new Date());
                    upv.setIp( getClientIpAddress() );
                    upvoteService.update(upv);
                }

            }else {
                upvote1.setDownvote( "1" );
                upvote1.setUpvoteTime( new Date(  ) );
                upvote1.setIp( getClientIpAddress() );
                upvoteService.add(upvote1);
            }

            userContent.setDownvote( userContent.getDownvote()+upvote);
        }else {
            if(upv != null){
                if( "1".equals( upv.getUpvote() ) ){
                    map.put( "data","done" );
                    return map;
                }else {
                    upv.setUpvote( "1" );
                    upv.setUpvoteTime( new Date(  ) );
                    upv.setIp( getClientIpAddress() );
                    upvoteService.update(upv);
                }

            }else {
                upvote1.setUpvote( "1" );
                upvote1.setUpvoteTime( new Date(  ) );
                upvote1.setIp( getClientIpAddress() );
                upvoteService.add(upvote1);
            }


            userContent.setUpvote( userContent.getUpvote() + upvote );
        }
        userContentService.updateById( userContent );
        map.put( "data","success" );
        return map;
    }

    public Map<String,Object> reply(Model model, @RequestParam(value = "content_id",required = false) Long content_id){
        LinkedHashMap map = new LinkedHashMap<String,Object>();
        List<Comment> list = commentService.findAllFirstComment(content_id);
        if(list!=null && list.size()>0){
            for(Comment c:list){
                List<Comment> coments = commentService.findAllChildrenCommnet( c.getConId(), c.getChildren() );
                if(coments!=null && coments.size()>0){
                    for(Comment com:coments){
                        if(com.getById()!=null ){
                            User byUser = userService.findById( com.getById() );
                            com.setByUser( byUser );
                        }

                    }
                }
                c.setComList( coments );
            }
        }
        map.put( "list",list );
        return map;
    }


    /**
     *@Description: 点赞
     *@Param: [model, id, content_id, uid, bid, oSize, comment_time, upvote]
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: TLQ
     *@date: 2019/5/11 23:14
    **/
    @RequestMapping("/comment")
    @ResponseBody
    public Map<String,Object> comment(Model model, @RequestParam(value = "id",required = false) Long id ,
                                      @RequestParam(value = "content_id",required = false) Long content_id ,
                                      @RequestParam(value = "uid",required = false) Long uid ,
                                      @RequestParam(value = "by_id",required = false) Long bid ,
                                      @RequestParam(value = "oSize",required = false) String oSize,
                                      @RequestParam(value = "comment_time",required = false) String comment_time,
                                      @RequestParam(value = "upvote",required = false) Integer upvote) {
        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            map.put( "data","fail" );
            return map;
        }
        if(id==null ){

            Date date = DateUtils.StringToDate( comment_time, "yyyy-MM-dd HH:mm:ss" );

            Comment comment = new Comment();
            comment.setComContent( oSize );
            comment.setCommTime( date );
            comment.setConId( content_id );
            comment.setComId( uid );
            if(upvote==null){
                upvote = 0;
            }
            comment.setById( bid );
            comment.setUpvote( upvote );
            User u = userService.findById( uid );
            comment.setUser( u );
            commentService.add( comment );
            map.put( "data",comment );

            UserContent userContent = userContentService.findById( content_id );
            Integer num = userContent.getCommentNum();
            userContent.setCommentNum( num+1 );
            userContentService.updateById( userContent );

        }else {
            //点赞
            Comment c = commentService.findById( id );
            c.setUpvote( upvote );
            commentService.update( c );


        }

        return map;

    }

    @RequestMapping("/deleteComment")
    @ResponseBody
    public Map<String,Object>  deleteComment(Model model, @RequestParam(value = "id",required = false) Long id,@RequestParam(value = "uid",required = false) Long uid,
                                             @RequestParam(value = "con_id",required = false) Long con_id,@RequestParam(value = "fid",required = false) Long fid) {
        int num = 0;
        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user==null){
            map.put( "data","fail" );
        }else{
            if(user.getId().equals( uid )){
                Comment comment = commentService.findById( id );
                if(StringUtils.isBlank( comment.getChildren() )){
                    if(fid!=null){
                        //去除id
                        Comment fcomm = commentService.findById( fid );
                        String child = StringUtil.getString( fcomm.getChildren(), id );
                        fcomm.setChildren( child );
                        commentService.update( fcomm );
                    }
                    commentService.deleteById(id);
                    num = num + 1;
                }else {
                    String children = comment.getChildren();
                    commentService.deleteChildrenComment(children);
                    String[] arr = children.split( "," );

                    commentService.deleteById( id );

                    num = num + arr.length + 1;

                }
                UserContent content = userContentService.findById( con_id );
                if(content!=null){
                    if(content.getCommentNum() - num >= 0){
                        content.setCommentNum( content.getCommentNum() - num );
                    }else {
                        content.setCommentNum( 0 );
                    }

                    userContentService.updateById( content );
                }
                map.put( "data",content.getCommentNum() );
            }else {
                map.put( "data","no-access" );
            }
        }

        return  map;
    }

    /**
     *@Description: 获取评论
     *@Param: [model, id, content_id, uid, bid, oSize, comment_time, upvote]
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: TLQ
     *@date: 2019/5/12 0:27
    **/
    @RequestMapping("/comment_child")
    @ResponseBody
    public Map<String,Object> addCommentChild(Model model, @RequestParam(value = "id",required = false) Long id ,
                                              @RequestParam(value = "content_id",required = false) Long content_id ,
                                              @RequestParam(value = "uid",required = false) Long uid ,
                                              @RequestParam(value = "by_id",required = false) Long bid ,
                                              @RequestParam(value = "oSize",required = false) String oSize,
                                              @RequestParam(value = "comment_time",required = false) String comment_time,
                                              @RequestParam(value = "upvote",required = false) Integer upvote) {
        Map map = new HashMap<String,Object>(  );
        User user = (User)getSession().getAttribute("user");
        if(user == null){
            map.put( "data","fail" );
            return map;
        }

        Date date = DateUtils.StringToDate( comment_time, "yyyy-MM-dd HH:mm:ss" );

        Comment comment = new Comment();
        comment.setComContent( oSize );
        comment.setCommTime( date );
        comment.setConId( content_id );
        comment.setComId( uid );
        if(upvote==null){
            upvote = 0;
        }
        comment.setById( bid );
        comment.setUpvote( upvote );
        User u = userService.findById( uid );
        comment.setUser( u );
        commentService.add( comment );

        Comment com = commentService.findById( id );
        if(StringUtils.isBlank( com.getChildren() )){
            com.setChildren( comment.getId().toString() );
        }else {
            com.setChildren( com.getChildren()+","+comment.getId() );
        }
        commentService.update( com );
        map.put( "data",comment );

        UserContent userContent = userContentService.findById( content_id );
        Integer num = userContent.getCommentNum();
        userContent.setCommentNum( num+1 );
        userContentService.updateById( userContent );
        return map;

    }
}