package com.skiply.system.common.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@EqualsAndHashCode
@SuperBuilder
public abstract class BaseModel<T> {
    protected T id;
}
