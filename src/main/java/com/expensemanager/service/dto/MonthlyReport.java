package com.expensemanager.service.dto;

import com.expensemanager.domain.Category;

import java.util.Map;

//⭐ "3.월별 지출 보고서 보기" 기능에서 출력할 보고서 DTO
public class MonthlyReport {

    //🔴 지출 보고서에 필요한 3가지 데이터
    private final int totalAmount; //총 지출 금액
    private final Map<Category, Integer> categoryTotals; //카테고리별 지출
    private final Category topCategory; //가장 많이 쓴 카테고리

    public MonthlyReport(int totalAmount, Map<Category, Integer> categoryTotals, Category topCategory){
        this.totalAmount = totalAmount;
        this.categoryTotals = categoryTotals;
        this.topCategory = topCategory;
    }

    public int getTotalAmount(){
        return totalAmount;
    }

    public Map<Category, Integer> getCategoryTotals(){
        return categoryTotals;
    }

    public Category getTopCategory(){
        return topCategory;
    }
}
