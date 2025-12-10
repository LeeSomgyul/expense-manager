package com.expensemanager.repository;

import com.expensemanager.domain.Category;
import com.expensemanager.domain.Expense;
import com.expensemanager.exception.StorageException;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileExpenseRepository implements ExpenseRepository{

    private final File file;
    private final List<Expense> storage = new ArrayList<>();

    public FileExpenseRepository(String filename){
        this.file = new File(filename);
        loadFromFile(); //시작 시 자동 불러오기
    }

    @Override
    public void save(Expense expense){
        storage.add(expense);
    }

    @Override
    public List<Expense> findAll(){
        return new ArrayList<>(storage);
    }

    @Override
    public Optional<Expense> findById(long id){
        return storage.stream() //storage 리스트를 돌면서(stream)
                .filter(expense -> expense.getId() == id) //DB와 저장된 id와 찾으려는 id가 같은 경우의 Expense를 찾아서
                .findFirst(); //첫번째 값 반환
    }

    @Override
    public List<Expense> findByMonth(int year, int month){
        return storage.stream()
                //getDate()는 Expense.java에서 구현
                .filter(expense -> expense.getDate().getYear() == year && expense.getDate().getMonthValue() == month)
                .toList();
    }

    @Override
    public void deleteById(long id){
        storage.removeIf(expense -> expense.getId() == id);
    }

    //🔴 파일로 저장하기
    public void saveToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){

            for (Expense e : storage) {
                bw.write(
                        e.getId() + "," +
                                e.getDate() + "," +
                                e.getDescription() + "," +
                                e.getAmount() + "," +
                                e.getCategory()
                );
                bw.newLine();
            }
        }catch(IOException error){
            throw new StorageException("파일 저장 중 오류 발생", error);
        }
    }

    //🔴 파일 불러오기 (파일 → Expense 객체)
    public void loadFromFile(){
        if(!file.exists()) return; //파일이 존재하지 않으면 종료

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");

                long id = Long.parseLong(parts[0]);
                LocalDate date = LocalDate.parse(parts[1]);
                String description = parts[2];
                int amount = Integer.parseInt(parts[3]);
                Category category = Category.valueOf(parts[4]);

                storage.add(new Expense(id, date, description, amount, category));
            }
        } catch (IOException error) {
            throw new StorageException("파일 불러오던 중 오류 발생", error);
        }
    }

}
