package com.ifnoelse.pdf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class PDFContents {


    private static Map<String, ContentsProvider> providers = new HashMap<>();

    static {
        providers.put("china-pub.com",new ChinaPubContentProvider());
    }

    public static String getContentsByUrl(String url) {
        ContentsProvider provider = null;
        for (Map.Entry<String, ContentsProvider> entry : providers.entrySet()) {
            if (url.contains(entry.getKey())) {
                provider = entry.getValue();
                break;
            }
        }

        return provider != null ? provider.getContentsByUrl(url) : null;
    }

    public void addContentsProvider(String site, ContentsProvider provider) {
        providers.put(site, provider);
    }


    public void removeContentsProvider(String site) {
        providers.remove(site);
    }
}
