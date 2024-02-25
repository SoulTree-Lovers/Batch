package com.example.batch.group;

import com.example.batch.domain.SettleGroup;
import com.example.batch.domain.repository.SettleGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SettleGroupItemDbWriter implements ItemWriter<List<SettleGroup>> {

    private final SettleGroupRepository settleGroupRepository;

    @Override
    public void write(Chunk<? extends List<SettleGroup>> chunk) throws Exception {
        final List<SettleGroup> settleGroups = new ArrayList<>();

        chunk.forEach(settleGroups::addAll);

        settleGroupRepository.saveAll(settleGroups);
    }
}
