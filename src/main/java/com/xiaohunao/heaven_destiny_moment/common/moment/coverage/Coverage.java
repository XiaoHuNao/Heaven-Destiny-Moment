package com.xiaohunao.heaven_destiny_moment.common.moment.coverage;

public class Coverage<T> implements ICoverage<T>{
    protected T coverage;

    public Coverage(T coverage) {
        this.coverage = coverage;
    }
}
