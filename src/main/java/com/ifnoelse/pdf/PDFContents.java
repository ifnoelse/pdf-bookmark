package com.ifnoelse.pdf;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class PDFContents {


    private static Map<String, ContentsProvider> providers = new HashMap<>();

    static {
        providers.put("china-pub.com", new ChinaPubContentProvider());
    }

    public static String getContentsByUrl(String url) {
        ContentsProvider provider = null;
        for (Map.Entry<String, ContentsProvider> entry : providers.entrySet()) {
            if (url.contains(entry.getKey())) {
                provider = entry.getValue();
                break;
            }
        }

        if (provider != null) {
            String contents = provider.getContentsByUrl(url);
            contents = Stream.of(contents.split("\n")).map(String::trim).collect(Collectors.joining("\n"));
            return contents;
        }

        return null;
    }

    public void addContentsProvider(String site, ContentsProvider provider) {
        providers.put(site, provider);
    }


    public void removeContentsProvider(String site) {
        providers.remove(site);
    }
}
