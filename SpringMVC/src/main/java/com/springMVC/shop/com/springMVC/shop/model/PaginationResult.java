package com.springMVC.shop.com.springMVC.shop.model;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ryan Thebloez on 9/17/2017.
 */
public class PaginationResult<E> {
    private int totalRecords, currentPage;
    private List<E> list;

    private int maxResult, totalPages;

    private int maxNavigationPage;

    private List<Integer> navigationPages;

    // @page : 1,2,...


    public PaginationResult(Query query, int page, int maxResult, int maxNavigationPage) {
        final int pageIndex = page - 1 < 0 ? 0 : page - 1;

        int fromRecordIndex = pageIndex * maxResult;
        int maxRecordIndex = fromRecordIndex + maxResult;

        ScrollableResults resultScroll = query.scroll(ScrollMode.SCROLL_INSENSITIVE);

        List results = new ArrayList();

        boolean hasResult = resultScroll.first();

        if (hasResult) {
            // scroll to position
            hasResult = resultScroll.scroll(fromRecordIndex);

            if (hasResult){
                do {
                    E record = (E) resultScroll.get(0);
                    results.add(record);
                } while (resultScroll.next()//
                && resultScroll.getRowNumber() >= fromRecordIndex//
                && resultScroll.getRowNumber() < maxRecordIndex );
            }
            // go to last record
            resultScroll.last();
        }

        // total records
        this.totalPages = resultScroll.getRowNumber() + 1;
        this.currentPage = pageIndex + 1;
        this.list = results;
        this.maxResult = maxResult;

        this.totalPages = (this.totalRecords / this.maxResult) + 1;
        this.maxNavigationPage = maxNavigationPage;

        if(maxNavigationPage < totalPages){
            this.maxNavigationPage = maxNavigationPage;
        }

        this.calcNavigationPages();
    }

    private void calcNavigationPages() {
        this.navigationPages = new ArrayList<Integer>();

        int current = this.currentPage > this.totalPages ? this.totalPages : this.currentPage;

        int begin = current - this.maxNavigationPage / 2;
        int end = current + this.maxNavigationPage / 2;

        // 1st Page
        navigationPages.add(1);
        if (begin > 2) {
            // For '...'
            navigationPages.add(-1);
        }

        for (int i = begin; i < end ; i++) {
            if (i > 1 && i < this.totalPages){
                navigationPages.add(i);
            }
        }

        if (end < this.totalPages - 2) {
            // For '...'
            navigationPages.add(-1);
        }
        // last page
        navigationPages.add(this.totalPages);
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<E> getList() {
        return list;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<Integer> getNavigationPages() {
        return navigationPages;
    }
}
