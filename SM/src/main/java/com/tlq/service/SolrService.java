/**
 * @program: SSM
 * @description: 全文搜索
 * @author: TLQ
 * @create: 2019-05-15 20:38
 **/
package com.tlq.service;

import com.tlq.entity.Page;
import com.tlq.entity.UserContent;

public interface SolrService {
    /**
     * 根据关键字搜索文章并分页
     * @param keyword
     * @return
     */
    Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize);
    /**
     * 添加文章到solr索引库中
     * @param userContent
     */
    void addUserContent(UserContent userContent);

    /**
     * 根据solr索引库
     * @param userContent
     */
    void updateUserContent(UserContent userContent);

    /**
     * 根据文章id删除索引库
     * @param userContent
     */
    void deleteById(Long id);
}