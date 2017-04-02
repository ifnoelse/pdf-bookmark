package test;

import com.ifnoelse.pdf.PDFContents;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Created by ifnoelse on 2017/2/27 0027.
 */
public class ContentProviderTest {
    public static void main(String[] args) throws ScriptException, IOException {
        System.out.println(PDFContents.getContentsByUrl("http://product.china-pub.com/3684420"));
    }

}
