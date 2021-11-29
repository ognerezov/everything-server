package net.okhotnikov.everything.util;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Sergey Okhotnikov.
 */
public class DataUtil {

    public static final String RECORDS = "records";
    public static final String TYPE = "type";
    public static final String QUOTATION = "quotation";
    public static final String POEM = "poem";
    public static final String CHAPTER = "chapter";

    public static Map<String,Object> retainQuotations(Map<String,Object> full){
        List<Map<String,Object>> records =(List<Map<String,Object>>) full.get(RECORDS);
        ListIterator<Map<String,Object>> it= records.listIterator();
        while (it.hasNext()){
            Map<String,Object> record = it.next();
            String type = (String)record.get(TYPE);
            if(QUOTATION.equals(type) || CHAPTER.equals(type))
                continue;

            it.remove();
        }

        return full;
    }

    public static TreeSet<Integer> getDateNumbers(int day, int month, int year, int max){
        year = reduce(year);

        TreeSet<Integer> res = new TreeSet<>();

        res.add(day);
        res.add(month);
        res.add(year);

        int dm = day * month;

        if (dm <= max){
            res.add(dm);
        }

        int dy = day * year;
        int my = month * year;

        if (dy <= max){
            res.add(dy);
        }

        if (my <= max){
            res.add(my);
        }

        int dmy = dm * year;

        if (dmy <= max){
            res.add(dmy);
        }

        return res;
    }

    public static Set<String> numberOfTheDay(int max){
        int num = getMaxDateNumber(max);
        Set<String> res = new HashSet<>();
        res.add(String.valueOf(num));
        return res;
    }

    public static int getMaxDateNumber(int max){
        return getDateNumbers(max).last();
    }

    public static Set<String> numbersOfTheDay(int max){
        return  getDateNumbers(max)
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }

    public static TreeSet<Integer> getDateNumbers(int max){
        LocalDate date = LocalDate.now();
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        return  getDateNumbers(day,month,year, max);
    }

    private static int reduce(int number) {
        int res = 0;
        do{
            res += number % 10;
            number = number /10;

        } while (number > 0);

        return res;
    }
}
