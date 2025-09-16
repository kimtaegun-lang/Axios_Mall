package org.zerock.mallapi.controller.formatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter; import java.util.Locale;

import org.springframework.format.Formatter;

public class LocalDateFormatter implements Formatter<LocalDate>{
    @Override
    public LocalDate parse(String text, Locale locale) // 문자열 -> localdate
    {
        return LocalDate.parse(text,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//
        
    }
    
    @Override
    public String print(LocalDate object,Locale locale) // localdate-> 문자열
    {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(object);
    }
}
