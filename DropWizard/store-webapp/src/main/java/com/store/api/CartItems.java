package com.store.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItems {
    private long id;
    private String content;

    public CartItems() {
        // Jackson deserialization
    }

    public CartItems(long id, String content) {
        this.id = id;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }

}
