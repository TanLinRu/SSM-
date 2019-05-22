/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-30 15:21
 **/
package com.tlq.dao;

import com.tlq.entity.Role;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface RoleMapper extends Mapper<Role> {
    /**
     * 根据用户id查询角色信息
     * @param uid
     * @return
     */
    List<Role> findByUid(@Param("uid") Long uid);
}