/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-30 15:20
 **/
package com.tlq.dao;

import com.tlq.entity.User;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface UserMapper extends Mapper<User> {
    @Update("update user set state = '1' , enable = '1' where email = #{user.email}" )
    public void active(User user);
}