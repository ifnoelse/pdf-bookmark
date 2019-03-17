package com.ifnoelse.pdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class PDFUtil {
    private static Pattern bookmarkPattern = Pattern.compile("^[\t\\s　]*?([0-9.]+)?(.*?)/?[\t\\s　]*([0-9]+)[\t\\s　]*?$");
    private static String blankRegex = "[\t\\s　]+";

    public static String replaceBlank(String str) {
        return str.replaceAll(blankRegex, " ").trim();
    }

    public static void addBookmark(String bookmarks, String srcFile, String destFile, int pageIndexOffset) {

        if (bookmarks != null && !bookmarks.isEmpty()) {
            if (bookmarks.trim().startsWith("http")) {
                addBookmark(PDFContents.getContentsByUrl(bookmarks), srcFile, destFile, pageIndexOffset);
            } else {
                addBookmark(Arrays.asList(bookmarks.split("\n")), srcFile, destFile, pageIndexOffset);
            }
        }
    }


    public static List<Bookmark> generateBookmark(String bookmarks, int pageIndexOffset, int minLens, int maxLnes) {
        return generateBookmark(Arrays.asList(bookmarks.split("\n")), pageIndexOffset, minLens, maxLnes);
    }

    public static List<Bookmark> generateBookmark(String bookmarks, int pageIndexOffset) {
        return generateBookmark(Arrays.asList(bookmarks.split("\n")), pageIndexOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 为pdf文件添加目录
     *
     * @param bookmarks       目录内容，每个list的元素为一条目录内容，比如：“1.1 Functional vs. Imperative Data Structures 1”
     * @param pageIndexOffset pdf文件真是页码与目录页码之间的偏移量
     * @param minLens         合法的目录条目最小长度
     * @param maxLnes         合法的目录条目最大长度
     * @return 返回报个书签内容的list集合
     */
    public static List<Bookmark> generateBookmark(List<String> bookmarks, int pageIndexOffset, int minLens, int maxLnes) {
        List<Bookmark> bookmarkList = new ArrayList<>();
        for (String ln : bookmarks) {
            ln = replaceBlank(ln);
            if (ln.length() < minLens || ln.length() > maxLnes) continue;
            Matcher matcher = bookmarkPattern.matcher(ln);
            if (matcher.find()) {
                String seq = matcher.group(1);
                String title = replaceBlank(matcher.group(2));
                int pageIndex = Integer.parseInt(matcher.group(3));
                if (seq != null && bookmarkList.size() > 0) {
                    Bookmark pre = bookmarkList.get(bookmarkList.size() - 1);
                    if (pre.getSeq() == null || seq.startsWith(pre.getSeq())) {
                        pre.addSubBookMarkBySeq(new Bookmark(seq, title, pageIndex + pageIndexOffset));
                    } else {
                        bookmarkList.add(new Bookmark(seq, title, pageIndex + pageIndexOffset));
                    }
                } else {
                    bookmarkList.add(new Bookmark(seq, title, pageIndex + pageIndexOffset));
                }

            } else {
                bookmarkList.add(new Bookmark(replaceBlank(ln)));
            }
        }
        return bookmarkList;
    }


    public static void addBookmark(List<String> bookmarks, String srcFile, String destFile, int pageIndexOffset, int minLens, int maxLnes) {
        addBookmark(generateBookmark(bookmarks, pageIndexOffset, minLens, maxLnes), srcFile, destFile);
    }

    public static void addBookmark(List<String> bookmarks, String srcFile, String destFile, int pageIndexOffset) {
        addBookmark(bookmarks, srcFile, destFile, pageIndexOffset, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void addBookmark(Bookmark bookmark, String srcFile, String destFile) {
        addOutlines(Arrays.asList(bookmark.outlines()), srcFile, destFile);
    }

    public static void addBookmark(List<Bookmark> bookmarks, String srcFile, String destFile) {
        addOutlines(bookmarks.stream().map(Bookmark::outlines).collect(Collectors.toList()), srcFile, destFile);
    }

    private static void addOutlines(List<HashMap<String, Object>> outlines, String srcFile, String destFile) {
        try {
            class MyPdfReader extends PdfReader {
                public MyPdfReader(String fileName) throws IOException {
                    super(fileName);
                    unethicalreading = true;
                    encrypted = false;
                }
            }
            PdfReader reader = new MyPdfReader(srcFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
            stamper.setOutlines(outlines);
            stamper.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
