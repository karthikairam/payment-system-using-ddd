package com.skiply.system.common.domain.model.valueobject;

import java.util.UUID;

public record PaymentTransactionId(UUID value) implements ValueObject {}
