package test;

import com.ifnoelse.pdf.PDFContents;
import com.ifnoelse.pdf.PDFUtil;

import java.io.IOException;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class Test {
    public static void main(String[] args) throws IOException {

//        String lines = PDFContents.getContentsFromChinaPub("http://product.china-pub.com/4702043");

//        PDFUtil.addBookmark(PDFUtil.generateBookmark(lines,22),"E:\\pdf\\Java EE 7权威指南 卷2.pdf","E:\\pdf\\res.pdf");


        System.out.println(PDFContents.getContentsByUrl("http://product.china-pub.com/3663089"));


    }
}
