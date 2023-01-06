package com.skiply.system.common.domain.model;

import lombok.experimental.SuperBuilder;

/**
 * It is just a marker class to denote the class is Aggregate Root in the Domain Driven Model
 * */
@SuperBuilder
public abstract class AggregateRoot<T> extends BaseModel<T> {
}
