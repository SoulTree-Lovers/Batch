package com.example.batch.detail;

import com.example.batch.domain.ApiOrder;
import com.example.batch.domain.ServicePolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PreSettleDetailProcessor implements ItemProcessor<ApiOrder, Key> {
    @Override
    public Key process(ApiOrder item) throws Exception {

        if (item.getState() == ApiOrder.State.FAIL) { // state가 null이면 필터링하지 않고 그냥 넘어감
            return null;
        }

        // url을 통해 service의 id를 찾아오기
        final Long serviceId = ServicePolicy.findByUrl(item.getUrl()).getId();

        return new Key(
                item.getCustomerId(),
                serviceId
        );
    }
}
