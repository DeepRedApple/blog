package com.zzuli.blog.util;

import java.util.List;

/**
 * @author: 李正浩
 * @Date: 2018/3/22 8:54
 * @Description:
 */
public class Page<T> {
    public static final Integer PAGE_SIZE = 10;

    private int pageNo = 1; // 当前页码
    private int pageSize = PAGE_SIZE;
    private int totalCount; //总页数
    private int totalPage;// 尾页索引
    private boolean firstPage;// 是否是第一页
    private boolean lastPage;// 是否是最后一页
    private List<T> list;

    public Page() {
    }

    public Page(int pageNo, int pageSize, int totalCount) {
        this.setTotalCount(totalCount);
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
        initialize();
    }

    public Page(int pageNo, int pageSize, int totalCount, List<T> list) {
        this(pageNo,pageSize,totalCount);
        this.list = list;
    }

    private void initialize() {
        int page = pageSize == 0 ? 0 : totalCount / pageSize;
        if (page == 0 || totalCount % pageSize != 0) {
            page++;
        }
        this.totalPage = page;

        if (pageNo <= 1) {
            this.firstPage = true;
        }

        if(pageNo >= this.totalPage){
            this.lastPage = true;
            this.pageNo = this.totalPage;
        }
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo + 1;
        }
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            this.pageSize = PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    public void setTotalCount(int totalCount) {
        if (totalCount < 0) {
            this.totalCount = 0;
        } else {
            this.totalCount = totalCount;
        }
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}