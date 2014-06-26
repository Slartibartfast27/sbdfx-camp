package com.zuehlke.sbdfx.dataaccess.api;

import java.util.List;

import com.google.common.collect.Lists;

public class ListResult<T> {

    private final int totalCount;
    private final List<T> delegates = Lists.newArrayList();

    public ListResult(final int totalCount) {
        super();
        this.totalCount = totalCount;
    }

    public static <T> ListResult<T> create(final int totalCount) {
        return new ListResult<>(totalCount);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getFetchedCount() {
        return delegates.size();
    }

    public T getElement(final int index) {
        return delegates.get(index);
    }

    public void add(final T toAdd) {
        delegates.add(toAdd);
    }

}
