package test;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;

/**
 * Created by ifnoelse on 2017/2/27 0027.
 */
public class ContentProviderTest {
    public static void main(String[] args) throws ScriptException, IOException {
//        ContentsProvider provider = new JDContentsProvider();
//        System.out.println(provider.getContentsByUrl("https://item.jd.com/12037418.html"));




//        Connection.Response s = Jsoup.connect("https://dx.3.cn/desc/11886254?cdn=2&callback=showdesc").execute();
//
        HttpRequest request = new HttpRequest();
        HttpResponse response = HttpRequest.get("https://dx.3.cn/desc/11886254?cdn=2&callback=showdesc").send();


        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine nashorn = scriptEngineManager.getEngineByName("nashorn");
        Object o = nashorn.eval("function showdesc(s){return s;};"+response.body());
        ScriptObjectMirror so = (ScriptObjectMirror) o;
        System.out.println(((ScriptObjectMirror) o).get("content"));
    }
}
