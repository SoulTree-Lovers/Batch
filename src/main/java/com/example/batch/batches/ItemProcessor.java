package com.example.batch.batches;

public interface ItemProcessor <I, O> {

    O process(I item);
}
