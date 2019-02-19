package org.ukwikora.export.website.model;

import org.ukwikora.utils.StringUtils;

import java.io.UnsupportedEncodingException;

public class Link {
    private String text;
    private String url;

    public Link(String name) throws UnsupportedEncodingException {
        text = StringUtils.lineTruncate(StringUtils.toBeautifulName(name), 20);
        url = StringUtils.toBeautifulUrl(name, "html");
    }

    public String getText(){
        return text;
    }

    public String getUrl(){
        return url;
    }
}
