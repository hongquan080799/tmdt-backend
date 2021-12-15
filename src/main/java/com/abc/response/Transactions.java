package com.abc.response;

import java.util.List;

public class Transactions {
    List<RelatedResouces> related_resources;

    public List<RelatedResouces> getRelated_resources() {
        return related_resources;
    }

    public void setRelated_resources(List<RelatedResouces> related_resources) {
        this.related_resources = related_resources;
    }
}
