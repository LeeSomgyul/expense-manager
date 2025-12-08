package com.expensemanager.app;

import com.expensemanager.repository.MemoryExpenseRepository;
import com.expensemanager.service.ExpenseService;

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
                case 2 -> {}
                case 3 -> {}
                case 4 -> {}
                case 5 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

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
}
