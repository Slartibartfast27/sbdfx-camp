package com.zuehlke.sbdfx.dataaccess.api;

public class BaseRequest {

    public final static int DEFAULT_MAX_RESULTS = 10_000;

    private int maxResults = DEFAULT_MAX_RESULTS;

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(final int maxResults) {
        this.maxResults = maxResults;
    }

}
