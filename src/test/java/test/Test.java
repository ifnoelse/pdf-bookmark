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

        //获取书籍的目录信息
        String contents = PDFContents.getContentsByUrl("http://product.china-pub.com/223565");


        //为书籍添加目录
        PDFUtil.addBookmark(PDFUtil.generateBookmark(contents, 14), "需要添加书签的pdf路径", "添加书签之后的pdf输出路径");


    }
}
