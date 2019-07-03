package test;

import com.ifnoelse.pdf.Bookmark;
import com.ifnoelse.pdf.PDFContents;
import com.ifnoelse.pdf.PDFUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by ifnoelse on 2017/2/25 0025.
 */
public class Test {
    public static void main(String[] args) throws IOException {

        //Get catalog information for books
        String contents = PDFContents.getContentsByUrl("http://product.china-pub.com/223565");


        //Add a table of contents to a book
        PDFUtil.addBookmark(PDFUtil.generateBookmark(contents, 14), "Pdf path to add bookmarks", "Pdf output path after adding bookmark");


    }
}
