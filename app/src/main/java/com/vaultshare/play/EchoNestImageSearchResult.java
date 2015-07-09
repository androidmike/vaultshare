package com.vaultshare.play;

/**
 * Created by mchang on 7/8/15.
 */
public class EchoNestImageSearchResult {
    EchoNestImageSearchResponse resp;
String name;
    public EchoNestImageSearchResult(String name, EchoNestImageSearchResponse resp) {
        this.resp = resp;
        this.name = name;
    }
}
