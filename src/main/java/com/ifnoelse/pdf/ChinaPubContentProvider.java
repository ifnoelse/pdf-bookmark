package com.ifnoelse.pdf;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class ChinaPubContentProvider implements ContentsProvider {
    @Override
    public String getContentsByUrl(String url) {
        String contents = null;
        try {
            Document doc = Jsoup.connect(url).get();
            String contentsHtml = doc.select("#ml + div").first().html().replaceAll("<br>", "###");
            contents = Jsoup.parse(contentsHtml).text().replaceAll("###", "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contents;
    }
}
