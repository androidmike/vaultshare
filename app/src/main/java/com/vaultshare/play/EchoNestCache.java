package com.vaultshare.play;

import android.media.Image;

import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mchang on 7/8/15.
 */
public class EchoNestCache {

    static EchoNestCache instance;
    HashMap<String, Artist>              artistMap = new HashMap();
    HashMap<String, List<EchoNestImage>> imagesMap = new HashMap();

    public EchoNestCache() {
        Bus.getInstance().register(this);
    }

    public static EchoNestCache getInstance() {
        if (instance == null) {
            instance = new EchoNestCache();
        }
        return instance;
    }

    public Artist getArtistByName(String artistName) {
        artistName = "Nipsey Hussle";
        if (artistMap.get(artistName) == null) {
            EchoNest.getInstance().search(artistName);
            return null;
        }
        return artistMap.get(artistName);
    }

    public List<EchoNestImage> getImagesOfArtistName(String artistName) {
        artistName = "Nipsey Hussle";
        if (imagesMap.get(artistName) == null) {
            EchoNest.getInstance().search(artistName);
            return null;
        }
        return imagesMap.get(artistName);
    }

    @Subscribe
    public void onEchoNestSearchResult(EchoNestSearchResult e) {
        if (e.resp.response.artists != null && !e.resp.response.artists.isEmpty()) {
            artistMap.put(e.name, e.resp.response.artists.get(0));
        }
    }

    @Subscribe
    public void onEchoNestImageSearchResult(EchoNestImageSearchResult e) {
        if (e.resp.response.images != null) {
            imagesMap.put(e.name, e.resp.response.images);
        }
    }
}
