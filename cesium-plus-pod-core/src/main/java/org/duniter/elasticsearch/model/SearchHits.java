package org.duniter.elasticsearch.model;

public class SearchHits {

    protected SearchHit[] hits;

    protected long totalHits;

    public SearchHits() {
    }

    public long getTotalHits() {
        return totalHits;
    }

    public SearchHit[] getHits() {
        return hits;
    }

    public void setHits(SearchHit[] hits) {
        this.hits = hits;
    }

    public void setTotalHits(long totalHits) {
        this.totalHits = totalHits;
    }

}