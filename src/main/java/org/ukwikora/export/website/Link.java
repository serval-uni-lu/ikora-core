package org.ukwikora.export.website;

import org.ukwikora.utils.StringUtils;

import java.io.UnsupportedEncodingException;

public class Link {
    private String text;
    private String url;

    Link(String name) throws UnsupportedEncodingException {
        text = StringUtils.lineTruncate(StringUtils.toBeautifulName(name), 20);
        url = StringUtils.toBeautifulUrl(name, "html");
    }

    String getText(){
        return text;
    }

    String getUrl(){
        return url;
    }
}
