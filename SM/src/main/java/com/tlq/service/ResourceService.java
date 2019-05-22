/**
 * @program: SSM
 * @description:资源服务
 * @author: TLQ
 * @create: 2019-03-28 17:02
 **/
package com.tlq.service;

import com.tlq.entity.Resource;

import java.util.List;

public interface ResourceService {

    /**
     *@Description: 添加资源
     *@Param: [resource]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 17:03
    **/
    public void add(Resource resource);

    /**
     *@Description: 根据资源id查询资源
     *@Param: [id]
     *@return: com.tlq.entity.Resource
     *@Author: TLQ
     *@date: 2019/3/28 17:04
    **/
    public Resource findById(Long id);

    /**
     *@Description: 查询所有资源
     *@Param: []
     *@return: java.util.List<com.tlq.entity.Resource>
     *@Author: TLQ
     *@date: 2019/3/28 17:04
    **/
    public List<Resource> findAll();

    /**
     *@Description: 根据资源id删除资源
     *@Param: [id]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 17:05
    **/
    public void deleteById(Long id);

    /**
     *@Description: 更新资源
     *@Param: [resource]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 17:06
    **/
    public void update(Resource resource);
}