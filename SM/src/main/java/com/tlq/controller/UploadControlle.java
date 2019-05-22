/**
 * @program: SSM
 * @description: 文件上传
 * @author: TLQ
 * @create: 2019-05-12 21:47
 **/
package com.tlq.controller;

import com.alibaba.fastjson.JSON;
import com.tlq.ClassComparator.NameComparator;
import com.tlq.ClassComparator.SizeComparator;
import com.tlq.ClassComparator.TypeComparator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class UploadControlle {

    private final static Logger LOGGER = Logger.getLogger(UploadControlle.class);

    private Map<String, Object> getError(String message) {
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("success",0);//0失败
        msg.put("message", message);
        return msg;
    }

    @RequestMapping("/fileUpload")
    public Map<String, Object> fileUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException,
            FileUploadException{
        Map map = new HashMap<String,Object>();
//      定义图片上传路径
        String savePath =  request.getSession().getServletContext().getRealPath("");
        String saveUrl = savePath + "/images/";

//        定义允许上传文件的扩展名
        HashMap fileMap = new HashMap<String,String>();
        fileMap.put("image", "gif,jpg,jpeg,png,bmp");
        fileMap.put("flash", "swf,flv");
        fileMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
        fileMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");

//       最大文件大小
        long maxSzie = 100000;

        response.setContentType("text/html; charset=UTF-8");
//      servlet3.0的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        if ( !multipartResolver.isMultipart(request)) {
            return getError("请选择文件。");
        }

        File uploadDir = new File(savePath);
//        检查目录
        if (!uploadDir.isDirectory()) {
            return getError("上传目录不存在。");
        }

//       检查目录写权限
        if (!uploadDir.canWrite()) {
            return getError("上传目录没有写权限。");
        }

        String dirName = request.getParameter("dir");
        if (dirName == null) {
            dirName = "image";
        }
        if (!fileMap.containsKey(dirName)) {
            return getError("目录名不正确。");
        }

//       创建文件夹
        savePath += dirName+"/";
        saveUrl += dirName+"/";
        File saveDirFile = new File(savePath);
        if (!saveDirFile.exists()) {
            saveDirFile.mkdirs();
        }
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyMMdd ");
        String date = simpleFormatter.format(new Date());
        savePath += date+"/";
        saveUrl += date+"/";
        File dirFile = new File(savePath);

        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
//      获取request的二进制
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;
        Iterator iterator = multipartHttpServletRequest.getFileNames();
        while (iterator.hasNext()) {
            MultipartFile file = multipartHttpServletRequest.getFile(iterator.next().toString());
//           检查文件大小
            if (file.getSize() > maxSzie) {
                return getError("上传文件大小超过限制。");
            }
//          扩展名校验
            String fileExt =  file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1).toLowerCase();
            if (!Arrays.asList(fileMap.get("image").toString().split(".")).contains(fileExt)) {
                return getError("上传文件扩展名是不允许的扩展名。\n只允许"
                        + fileMap.get(dirName) + "格式。");
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

            String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + fileExt;
            if (file!=null ) {
                System.out.println(222);
                try {
                    File uploadFile = new File(savePath,newFileName);
                    file.transferTo(uploadFile);
                }catch (Exception e) {
                    return getError("上传文件失败。");
                }
            }else {
                System.out.println(3333);
            }
//            try {
////                File uploadFile = new File(savePath,newFileName);
////                System.out.println(222);
//////              ByteStreams一次性复制
////                ByteStreams.copy(file.getInputStream(),new FileOutputStream(uploadFile));
////                System.out.println(333);println
//
//            }catch (Exception e) {
//                return getError("上传文件失败。");
//            }

            Map<String, Object> msg = new HashMap<String, Object>();
            msg.put("success", 1);
            msg.put("url", saveUrl + newFileName);
            // =======savePath=======G:\wly\dreamland\target\dreamland\images/image/20180108/=====url======/images/image/20180108/20180108180524_256.png
            LOGGER.info( "=======savePath======="+savePath+"=====url======"+saveUrl+newFileName );
            return msg;
        }
        return  null;

    }


    @RequestMapping(value = "/fileManager")
    public void fileManager(HttpServletRequest request,
                            HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream outputStream = response.getOutputStream();
//        图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp" };
//        根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath =  request.getSession().getServletContext().getRealPath("/")+ "images/";
//        根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl = request.getContextPath()+ "images/";
        String dirName = request.getParameter("dir");
        if (dirName != null) {
            if (!Arrays.asList(
                    new String[] { "image", "flash", "media", "file" }
                ).contains(dirName)) {
                outputStream.print("Invalid Directory name.");
                return;
            }
            rootUrl += dirName+"/";
            rootPath += dirName+"/";
            File saveDirFile = new File(rootPath);
            if (!saveDirFile.exists()) {
                saveDirFile.mkdirs();
            }
        }
//      对path参数进行处理
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath =  rootPath+path;
        String currentUrl = rootUrl+path;
        String currentDirPath = path;
        String moveupDirPath = "";
        System.out.println("path:"+path);
        if (!path.equals("")) {
            String string = currentDirPath.substring(0,currentDirPath.length()-1);
            moveupDirPath = string.lastIndexOf("/") >= 0 ? string.substring(0,
                    string.lastIndexOf("/") + 1) : "";
        }
        // 排序形式，name or size or type
       String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";


        // 不允许使用..移动到上一级目录
        if (path.indexOf(".." )> 0) {
            outputStream.print("Access is not allowed.");
            return;
        }
        // 最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            outputStream.println("Parameter is not valid.");
            return;
        }
        // 目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if (!currentPathFile.isDirectory()) {
            outputStream.println("Directory does not exist.");
            return;
        }
        // 遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if (currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                   Hashtable<String,Object> hashtable = new Hashtable<String, Object>();
                   String fileName = file.getName();
                   if (file.isDirectory()) {
                       hashtable.put("is_dir",true);
                       hashtable.put("has_file",(file.listFiles() != null));
                       hashtable.put("fileSize",0L);
                       hashtable.put("is_photo",false);
                       hashtable.put("filetype","");

                   }else  if (file.isFile()) {
                       String fileExt = fileName.substring(fileName.indexOf("."));
                       hashtable.put("is_dir",false);
                       hashtable.put("filesize",file.length());
                       hashtable.put("is_photo",Arrays.<String>asList(fileTypes).contains(fileExt));
                       hashtable.put("filetype",fileExt);
                   }
                   hashtable.put("filename",fileName);
                   hashtable.put("datetime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                   fileList.add(hashtable);

            }
        }


        if ("size".equals(order)) {
            Collections.sort(fileList, new SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new TypeComparator());
        } else {
            Collections.sort(fileList, new NameComparator());
        }
        Map<String, Object> msg = new HashMap<String, Object>();
        msg.put("moveup_dir_path", moveupDirPath);
        msg.put("current_dir_path", currentDirPath);
        msg.put("current_url", currentUrl);
        msg.put("total_count", fileList.size());
        msg.put("file_list", fileList);
        response.setContentType("application/json; charset=UTF-8");
        String msgStr = JSON.toJSONString(msg);
       outputStream.println(msgStr);
    }
}