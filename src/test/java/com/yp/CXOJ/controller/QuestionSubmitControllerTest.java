package com.yp.CXOJ.controller;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuestionSubmitControllerTest {
    @Test
    public void doSubmit(){
        HttpClient client = new DefaultHttpClient();
//        client.prepare("POST", "https://linkedin-api8.p.rapidapi.com/search-posts-by-hashtag")
//                .setHeader("x-rapidapi-key", "Sign Up for Key")
//                .setHeader("x-rapidapi-host", "linkedin-api8.p.rapidapi.com")
//                .setHeader("Content-Type", "application/json")
//                .setBody("{\"hashtag\":\"golang\",\"sortBy\":\"REV_CHRON\",\"start\":\"0\",\"paginationToken\":\"\"}")
//                .execute()
//                .toCompletableFuture()
//                .thenAccept(System.out::println)
//                .join();
//
//        client.close();
    }
}