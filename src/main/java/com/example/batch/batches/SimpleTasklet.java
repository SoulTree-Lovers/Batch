package com.example.batch.batches;


public class SimpleTasklet<I, O> implements Tasklet {

    private final ItemReader<I> itemReader;
    private final ItemProcessor<I, O> itemProcessor;
    private final ItemWriter<O> itemWriter;

    public SimpleTasklet(ItemReader<I> itemReader, ItemProcessor<I, O> itemProcessor, ItemWriter<O> itemWriter) {
        this.itemReader = itemReader;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
    }


    @Override
    public void execute() {
        int pageNo = 0;
        while (true) {
            // READ
            final I read = itemReader.read();
            if (read == null) break;

            // PROCESS
            final O process = itemProcessor.process(read);
            if (process == null) continue;

            // WRITE
            itemWriter.write(process);
        }
    }

}
