package com.expensemanager.service;

import com.expensemanager.domain.Category;
import com.expensemanager.domain.Expense;
import com.expensemanager.exception.ExpenseNotFoundException;
import com.expensemanager.exception.InvalidInputException;
import com.expensemanager.repository.ExpenseRepository;
import com.expensemanager.repository.FileExpenseRepository;
import com.expensemanager.service.dto.MonthlyReport;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExpenseService {

    private final ExpenseRepository repository;
    private long idSequence = 1; //자동 증가 ID

    public ExpenseService(ExpenseRepository repository){
        this.repository = repository;
    }

    //1️⃣ 지출 추가하기
    public void addExpense(String dateInput, String description, String amountInput, int categoryInput){

        //파싱
        LocalDate date = parseDate(dateInput); //문자 -> 날짜
        int amount = parseAmount(amountInput); // 문자 -> 숫자
        Category category = parseCategory(categoryInput); // 숫자 -> enum

        //ID 생성
        long id = idSequence++;

        //Expense 객체 생성
        Expense expense = new Expense(id, date, description, amount, category);

        repository.save(expense);
    }

    //[추가 함수] 입력받은 문자열 데이터를 원하는 타입으로 변환하는 '파싱' 함수
    private LocalDate parseDate(String dateInput){
        try{
            return LocalDate.parse(dateInput);
        }catch (DateTimeParseException error){
            throw new InvalidInputException("날짜 형식이 잘못되었습니다. 예: 2025-01-14");
        }
    }

    private int parseAmount(String amountInput){
        try{
            int amount = Integer.parseInt(amountInput);
            if(amount < 0){
                throw  new InvalidInputException("금액은 음수가 될 수 없습니다.");
            }
            return amount;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("금액은 숫자로 입력해야 합니다.");
        }
    }

    private Category parseCategory(int categoryInput){
        return switch (categoryInput){
            case 1 -> Category.FOOD;
            case 2 -> Category.TRANSPORT;
            case 3 -> Category.SHOPPING;
            case 4 -> Category.ENTERTAINMENT;
            case 5 -> Category.ETC;
            default -> throw new InvalidInputException("유효하지 않은 카테고리 번호입니다.");
        };
    }

    //2️⃣ 지출 항목 보기
    public List<Expense> getAllExpenses(){
        List<Expense> expenses = repository.findAll();

        //ID 순으로 정렬
        expenses.sort((e1, e2) -> Long.compare(e1.getId(), e2.getId()));

        return expenses;
    }

    //3️⃣ 월별 보고서 보기
    public MonthlyReport getMonthlyReport(int year, int month){
        List<Expense> expenses = repository.findByMonth(year, month);

        if(expenses.isEmpty()){
            return new MonthlyReport(0, Map.of(), null);
        }

        //총 지출 금액
        int totalAmount = expenses.stream().mapToInt(Expense::getAmount).sum();

        //카테고리별 지출
        Map<Category, Integer> categoryTotals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory, //1차적으로 카테고리별로 묶은 후
                        Collectors.summingInt(Expense::getAmount) //2차로 카테고리별 금액으로 묶기
                ));

        //가장 많이 쓴 카테고리
        Category topCategory = categoryTotals.entrySet().stream()//categoryTotals에서 만들어진 키-값이 한쌍의 세트가 된다
                .max(Map.Entry.comparingByValue())//값을 기준으로 제일 큰 값
                .map(Map.Entry::getKey)//제일 큰 값의 키
                .orElse(null);//데이터 비어있으면 null 반환

        return new MonthlyReport(totalAmount, categoryTotals, topCategory);
    }

    //4️⃣ 삭제 하기
    public void deleteExpense(long id){
        Optional<Expense> expenseOpt = repository.findById(id);

        Expense expense = expenseOpt.orElseThrow(
                () -> new ExpenseNotFoundException("ID " + id + "번 지출을 찾을 수 없습니다.")
        );

        repository.deleteById(expense.getId());
    }

    //✅ 파일 저장하기
    public void saveDate(){
        if(repository instanceof FileExpenseRepository fileExpenseRepository){
            fileExpenseRepository.saveToFile();
        }
    }
}
