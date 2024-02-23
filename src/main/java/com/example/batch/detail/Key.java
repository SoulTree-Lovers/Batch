package com.example.batch.detail;

import java.io.Serializable;

public record Key(Long customerId, Long serviceId) implements Serializable {
}
