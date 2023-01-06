package com.skiply.system.common.domain.model;

import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode
@SuperBuilder
public abstract class BaseModel<T> {
    protected T id;
}
