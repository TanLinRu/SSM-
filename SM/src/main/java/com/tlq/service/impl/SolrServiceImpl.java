/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-05-15 20:39
 **/
package com.tlq.service.impl;

import com.tlq.common.PageHelper;
import com.tlq.entity.Page;
import com.tlq.entity.UserContent;
import com.tlq.service.SolrService;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SolrServiceImpl implements SolrService {
    @Autowired
    private HttpSolrClient solrClient;
    @Override
    public Page<UserContent> findByKeyWords(String keyword, Integer pageNum, Integer pageSize) {
        SolrQuery solrQuery = new SolrQuery();
//        设置查询条件
        solrQuery.setQuery("title:"+keyword);
//        设置高亮
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.setHighlightSimplePre( "<span style='color:red'>" );
        solrQuery.setHighlightSimplePost( "</span>" );

        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 ) {
            pageSize = 7;
        }
        solrQuery.setStart((pageNum-1)*pageSize);
        solrQuery.setRows(pageSize);
        solrQuery.addSort("rpt_time", SolrQuery.ORDER.desc);
//        开始查询
        try {
            QueryResponse response = solrClient.query(solrQuery);
//            获取高亮结果
            Map<String,Map<String, List<String>>> hiighlighting = response.getHighlighting();
//           获取结果集
            SolrDocumentList resultList  = response.getResults();
//          获取总数量
            long totalNum = resultList.getNumFound();
            List<UserContent> list = new ArrayList<UserContent>();
            for (SolrDocument solrDocument :resultList) {
                UserContent userContent = new UserContent();
                //文章id
                String id = (String) solrDocument.get( "id" );
                Object content1 = solrDocument.get( "content" );
                Object commentNum = solrDocument.get( "comment_num" );
                Object downvote = solrDocument.get( "downvote" );
                Object upvote = solrDocument.get( "upvote" );
                Object nickName = solrDocument.get( "nick_name" );
                Object imgUrl = solrDocument.get( "img_url" );
                Object uid = solrDocument.get( "u_id" );
                Object rpt_time = solrDocument.get( "rpt_time" );
                Object category = solrDocument.get( "category" );
                Object personal = solrDocument.get( "personal" );
                //取得高亮数据集合中的文章标题
                Map<String, List<String>> map = hiighlighting.get( id );
                String title = map.get( "title" ).get( 0 );

                userContent.setId( Long.parseLong( id ) );
                userContent.setCommentNum( Integer.parseInt( commentNum.toString() ) );
                userContent.setDownvote( Integer.parseInt( downvote.toString() ) );
                userContent.setUpvote( Integer.parseInt( upvote.toString() ) );
                userContent.setNickName( nickName.toString() );
                userContent.setImgUrl( imgUrl.toString() );
                userContent.setuId( Long.parseLong( uid.toString() ) );
                userContent.setTitle( title );
                userContent.setPersonal( personal.toString() );
                Date date = (Date)rpt_time;
                userContent.setRptTime(date);
                List<String> clist = (ArrayList)content1;
                userContent.setContent( clist.get(0).toString() );
                userContent.setCategory( category.toString() );
                list.add( userContent );
            }
            PageHelper.startPage(pageNum,pageSize);
            Page page = PageHelper.endPage();
            page.setResult(list);
            page.setTotal(totalNum);
        }catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addUserContent(UserContent userContent) {
        if(userContent!=null){
            addDocument(userContent);
        }
    }

    @Override
    public void updateUserContent(UserContent userContent) {
        if(userContent!=null){
            addDocument(userContent);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            solrClient.deleteById(id.toString());
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDocument(UserContent cont){
        try {
            SolrInputDocument inputDocument = new SolrInputDocument();
            inputDocument.addField( "comment_num", cont.getCommentNum() );
            inputDocument.addField( "downvote", cont.getDownvote() );
            inputDocument.addField( "upvote", cont.getUpvote() );
            inputDocument.addField( "nick_name", cont.getNickName());
            inputDocument.addField( "img_url", cont.getImgUrl() );
            inputDocument.addField( "rpt_time", cont.getRptTime() );
            inputDocument.addField( "content", cont.getContent() );
            inputDocument.addField( "category", cont.getCategory());
            inputDocument.addField( "title", cont.getTitle() );
            inputDocument.addField( "u_id", cont.getuId() );
            inputDocument.addField( "id", cont.getId());
            inputDocument.addField( "personal", cont.getPersonal());
            solrClient.add( inputDocument );
            solrClient.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}