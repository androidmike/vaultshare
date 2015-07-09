package com.vaultshare.play;

/**
 * Created by mchang on 7/8/15.
 */
public class EchoNestSearchResult {
    EchoNestArtistSearchResponse resp;
    String                       name;

    public EchoNestSearchResult(String name, EchoNestArtistSearchResponse resp) {
        this.resp = resp;
        this.name = name;
    }
}
