package org.duniter.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SearchHits {

    protected SearchHit[] hits;

    protected Long totalHits;

    public SearchHits() {
    }

    @JsonGetter("total")
    public Long getTotalHits() {
        return totalHits;
    }

    @JsonSetter("total")
    public void setTotalHits(Long totalHits) {
        this.totalHits = totalHits;
    }

    public SearchHit[] getHits() {
        return hits;
    }

    public void setHits(SearchHit[] hits) {
        this.hits = hits;
    }



}