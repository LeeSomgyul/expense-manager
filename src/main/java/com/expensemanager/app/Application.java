package com.expensemanager.app;

import com.expensemanager.domain.Category;
import com.expensemanager.domain.Expense;
import com.expensemanager.exception.ExpenseNotFoundException;
import com.expensemanager.repository.MemoryExpenseRepository;
import com.expensemanager.service.ExpenseService;
import com.expensemanager.service.dto.MonthlyReport;

import java.util.List;
import java.util.Scanner;

public class Application {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        //🔴 expenseService가  MemoryExpenseRepository만 사용하도록 의존성 주입
        ExpenseService expenseService = new ExpenseService(new MemoryExpenseRepository());

        while(true){
            System.out.println("============================");
            System.out.println("    개인 지출 관리 시스템");
            System.out.println("============================");
            System.out.println("1. 지출 추가하기");
            System.out.println("2. 지출 목록 보기");
            System.out.println("3. 월별 보고서 보기");
            System.out.println("4. 삭제하기");
            System.out.println("5. 종료하기");
            System.out.print("메뉴 선택: ");

            int choice = scanner.nextInt(); //🔴사용자가 입력 후 엔터를 누르면서 choice에 저장됨.
            scanner.nextLine(); //🔴여기서 엔터 비워주기.

            switch (choice){
                case 1 -> addExpenseMenu(scanner, expenseService);
                case 2 -> displayAllExpenses(expenseService);
                case 3 -> displayMonthlyReport(scanner, expenseService);
                case 4 -> deleteExpenseMenu(scanner, expenseService);
                case 5 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    //1️⃣ 지출 추가하기
    private static void addExpenseMenu(Scanner scanner, ExpenseService expenseService){
        System.out.println("지출 날짜를 입력하세요 (예: 2025-01-14) :");
        System.out.print(">");
        String dateInput = scanner.nextLine();

        System.out.println("지출 내용을 입력하세요 :");
        System.out.print(">");
        String description = scanner.nextLine();

        System.out.println("지출 금액을 입력하세요 :");
        System.out.print(">");
        String amountInput = scanner.nextLine();

        System.out.println("카테고리를 선택하세요 :");
        System.out.println("1. FOOD");
        System.out.println("2. TRANSPORT");
        System.out.println("3. SHOPPING");
        System.out.println("4. ENTERTAINMENT");
        System.out.println("5. ETC");
        System.out.print(">");
        int categoryInput = scanner.nextInt();
        scanner.nextLine(); //🔴버퍼 비우기

        //🔴ExpenseService로 사용자에게 입력받은 값들 전달
        expenseService.addExpense(dateInput, description, amountInput, categoryInput);

        System.out.println("지출이 추가되었습니다!");
    }

    //2️⃣ 지출 목록 보기
    private static void displayAllExpenses(ExpenseService expenseService){
        System.out.println("[지출 목록]");
        System.out.println();

        List<Expense> expenses = expenseService.getAllExpenses();

        //저장된 지출 목록이 없다면
        if(expenses.isEmpty()){
            System.out.println("저장된 지출이 없습니다.");
            return;
        }

        System.out.println("ID | 날짜        | 내용        | 금액   | 카테고리");
        System.out.println("--------------------------------------------------------");

        int totalExpense = 0;

        for(Expense e : expenses){
            System.out.printf("%-3d|%-13s|%-10s|%-8d|%s",
                    e.getId(), e.getDate(), e.getDescription(), e.getAmount(), e.getCategory());

            totalExpense += e.getAmount();
        }

        System.out.println();
        System.out.println("--------------------------------------------------------");
        System.out.println("총 지출: " + totalExpense + "원");
        System.out.println();
        System.out.println("엔터를 누르면 메뉴로 돌아갑니다...");
    }

    //3️⃣ 월별 보고서 보기
    private static void displayMonthlyReport(Scanner scanner, ExpenseService expenseService){
        System.out.println("보고서를 생성할 연도를 입력하세요 :");
        System.out.print("> ");
        int year = scanner.nextInt();

        System.out.println();

        System.out.println("월을 입력하세요 :");
        System.out.print("> ");
        int month = scanner.nextInt();
        scanner.nextLine();

        System.out.println();

        MonthlyReport monthlyReports = expenseService.getMonthlyReport(year, month);

        System.out.println("===== " + year + "년 " + month + "월 지출 보고서 =====");
        System.out.println();

        int totalAmount = monthlyReports.getTotalAmount();
        System.out.println("총 지출 금액: " + totalAmount + "원");
        System.out.println();

        System.out.println("카테고리별 지출:");
        for(Category c : Category.values()){
            int amount = monthlyReports.getCategoryTotals().getOrDefault(c, 0);
            System.out.printf("%-15s : %d원\n", c, amount);
        }

        System.out.println();

        if(monthlyReports.getTopCategory() != null){
            System.out.println("가장 많이 쓴 카테고리: " + monthlyReports.getTopCategory());
        }else{
            System.out.println("가장 많이 쓴 카테고리가 없습니다.");
        }

        System.out.println();
        System.out.println("보고서 생성 완료!");
    }

    //4️⃣ 삭제하기
    private static void deleteExpenseMenu(Scanner scanner, ExpenseService expenseService){
        System.out.println("삭제할 지출 ID를 입력하세요 :");
        System.out.print("> ");

        long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println();

        try{
            expenseService.deleteExpense(id);
            System.out.println("ID " + id + "번 지출이 삭제되었습니다.");
        }catch (ExpenseNotFoundException error){
            System.out.println(error.getMessage());
        }
    }
}
