package com.ifnoelse.pdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class Bookmark {
    private String seq;
    private int pageIndex = -1;
    private String title;
    private List<Bookmark> subBookMarks = new ArrayList<>();
    public Bookmark(String title, int pageIndex) {
        this.pageIndex = pageIndex;
        this.title = title;
    }
    public Bookmark(String seq, String title, int pageIndex) {
        this.pageIndex = pageIndex;
        this.title = title;
        this.seq = seq;
    }

    public Bookmark(String title) {
        this.title = title;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {

        this.pageIndex = pageIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Bookmark> getSubBookMarks() {
        return subBookMarks;
    }

    public void addSubBookMark(Bookmark kid) {
        subBookMarks.add(kid);
    }

    public void addSubBookMarkBySeq(Bookmark kid) {

        for (Bookmark bookmark : subBookMarks) {
            if (kid.getSeq().startsWith(bookmark.getSeq()+".")) {
                bookmark.addSubBookMarkBySeq(kid);
                return;
            }
        }
        subBookMarks.add(kid);
    }


    public HashMap<String, Object> outlines() {

        HashMap<String, Object> root = new HashMap<String, Object>();
        root.put("Title", (getSeq() != null ? getSeq() + " " : "") + getTitle());
        root.put("Action", "GoTo");
        if (pageIndex >= 0)
            root.put("Page", String.format("%d Fit", pageIndex));
        ArrayList<HashMap<String, Object>> kids = new ArrayList<HashMap<String, Object>>();
        if (subBookMarks != null && !subBookMarks.isEmpty()) {
            for (Bookmark bookmark : subBookMarks) {
                kids.add(bookmark.outlines());
            }
            root.put("Kids", kids);
        }

        return root;
    }

    @Override
    public String toString() {
        String indent = "- ";
        StringBuffer sb = new StringBuffer();
        if (getSeq() != null) {
            sb.append(getSeq());
            sb.append(" ");
        }

        sb.append(getTitle());

        if (getSubBookMarks() != null && !getSubBookMarks().isEmpty()) {
            for (Bookmark bookmark : getSubBookMarks()) {
                sb.append("\n");
                sb.append(indent);
                sb.append(bookmark.toString().replaceAll(indent,indent+indent));
            }
        }

        return sb.toString();
    }
}
