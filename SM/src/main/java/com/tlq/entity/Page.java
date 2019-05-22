/**
 * @program: SSM
 * @description:分页对象，里面包括分页信息和数据结果
 * @author: TLQ
 * @create: 2019-03-29 22:37
 **/
package com.tlq.entity;

import java.io.Serializable;
import java.util.List;

public class Page<E> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int pageNum;
    private int pageSize;
    private int startRow;
    private int endRow;
    private long total;
    private int pages;
    private List<E> result;

    public Page(Integer pageNum, Integer pageSize) {
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 7;
        }
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }

    public int getPages() {
        // 计算总页数
        long totalPage = this.getTotal() / this.getPageSize()
                + ((this.getTotal() % this.getPageSize() == 0) ? 0 : 1);
        this.setPages((int) totalPage);
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getEndRow() {
        this.endRow = pageNum * pageSize;
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getStartRow() {
        this.startRow = this.pageNum > 0
                ? (this.pageNum - 1) * this.pageSize
                : 0;
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public long getTotal() {
        return total;
    }

    private int startPage;//开始页码（按钮上的数字）
    private int endPage;//结束页码（按钮上的数字）

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
    public void setTotal(long total) {
        //计算总页码数：
        int totalCount = Integer.parseInt(total+"");
        pages=(totalCount+pageSize-1)/pageSize;
        //计算页面的页码中“显示”的起始页码和结束页码
        //一般显示的页码较好的效果是最多显示10个页码
        //算法是前5后4，不足补10
        //计算显示的起始页码（根据当前页码计算）：当前页码-5
        startPage = pageNum - 5;
        if(startPage < 1){
            startPage = 1;//页码修复
        }

        //计算显示的结束页码（根据开始页码计算）：开始页码+9
        endPage = startPage + 9;
        if(endPage > pages){//页码修复
            endPage = pages;
        }

        //起始页面重新计算（根据结束页码计算）：结束页码-9
        startPage = endPage - 9;
        if(startPage < 1){
            startPage = 1;//页码修复
        }

        System.out.println(startPage +"和" +endPage);

        this.total = total;
    }

    @Override
    public String toString() {
        return "Page{" + "pageNum=" + pageNum + ", pageSize=" + pageSize
                + ", startRow=" + startRow + ", endRow=" + endRow
                + ", total=" + total + ", pages=" + pages + '}';
    }
}