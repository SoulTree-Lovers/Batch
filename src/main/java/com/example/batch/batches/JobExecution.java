package com.example.batch.batches;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class JobExecution {

    private BatchStatus batchStatus;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
