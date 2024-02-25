package com.example.batch.support;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormatJobParametersValidator implements JobParametersValidator {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final String[] names;

    public DateFormatJobParametersValidator(String[] names) {
        this.names = names;
    }

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        for (String name: names) {
            validateDateFormat(parameters, name);
        }
    }

    private void validateDateFormat(JobParameters parameters, String name) throws JobParametersInvalidException {
        try { // 파라미터로 받은 날짜를 날짜형식에 맞는지 변환해보고 에러 발생 시 예외처리
            final String string = parameters.getString(name);
            LocalDate.parse(string, dateTimeFormatter);
        } catch (Exception e) {
            throw new JobParametersInvalidException("날짜는 yyyyMMdd 형식만 지원합니다.");
        }
    }
}
