/**
 * @program: SSM
 * @description:roleRource
 * @author: TLQ
 * @create: 2019-03-28 17:06
 **/
package com.tlq.service;
import com.tlq.entity.RoleResource;

import java.util.List;

public interface RoleResourceService {
    /**
     *@Description: 添加roleRource
     *@Param: [roleResource]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 17:08
    **/
    public void add(RoleResource roleResource);

    /**
     *@Description: 根据id查询RoleResource
     *@Param: [id]
     *@return: com.tlq.entity.RoleResource
     *@Author: TLQ
     *@date: 2019/3/28 17:09
    **/
    public RoleResource findById(Long id);

    /**
     *@Description: 根据角色id查询角色资源集合
     *@Param: [rid]
     *@return: java.util.List<com.tlq.entity.RoleResource>
     *@Author: TLQ
     *@date: 2019/3/28 17:10
    **/
    public List<RoleResource> findByRoleId(Long rid);

    /**
     *@Description: 根据id删除RoleResource
     *@Param: [id]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 17:10
    **/
    public void deleteById(Long id);
}