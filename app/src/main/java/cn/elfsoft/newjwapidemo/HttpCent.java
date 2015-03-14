package cn.elfsoft.newjwapidemo;

import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCent {
    protected String httpUrl = "http://api.elfsoft.cn/newjw/servlet/";

    protected InputStream request2(String path) throws Exception {
        URL url = new URL(httpUrl + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(2 * 1000);
        conn.setReadTimeout(2 * 1000);
        InputStream in = conn.getInputStream();
        if (in == null)
            throw new Exception("网络连接错误");
        return in;
    }

    protected InputStream request(String path) throws Exception {
        for (int i = 0; i < 5; i++) {
            try {
                return request2(path);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public String initCent() throws Exception {
        String path = "newjw?commond=init";
        InputStream in = request(path);
        return new String(readInputStream(in), "gbk");
    }

    public Drawable getValidateCode(String sessionid) throws Exception {
        String path = "testaction?sessionid=" + sessionid;
        InputStream in = request(path);
        return Drawable.createFromStream(in, "code");
    }

    public boolean login(String sessionid, String username, String password, String code) throws Exception {
        String path = "newjw?commond=login&id=" + username + "&pw=" + password + "&sessionid=" + sessionid + "&code=" + code;
        InputStream in = request(path);
        String status = new String(readInputStream(in), "gbk");
        if (status.equals("success")) {
            return true;
        }
        return false;
    }

    public boolean loginStatus(String sessionid) throws Exception {
        String path = "newjw?commond=loginstatus&sessionid=" + sessionid;
        InputStream in = request(path);
        String status = new String(readInputStream(in), "gbk");
        System.out.println(status + "  fffff");
        if (status.equals("login")) {
            return true;
        }
        return false;
    }

    public String getScheduleInfo(String sessionid, String term, String hidyzm) throws Exception {
        String path = "newjw?commond=schedule&sessionid=" + sessionid + "&term=" + term + "&hidyzm=" + hidyzm;
        InputStream in = request(path);
        return new String(readInputStream(in), "utf-8");
    }

    public String getGradeInfo(String sessionid, String year, String term) throws Exception {
        String path = "newjw?commond=grade&sessionid=" + sessionid + "&year=" + year + "&term=" + term;
        InputStream in = request(path);
        return new String(readInputStream(in), "utf-8");
    }

    public String getExamBarInfo(String sessionid) throws Exception {
        String path = "newjw?commond=exambar&sessionid=" + sessionid;
        InputStream in = request(path);
        String result = new String(readInputStream(in), "utf-8");
        if (result.equals("false"))
            return "";
        return result;
    }

    public String getExamInfo(String sessionid, String term, String type) throws Exception {
        String path = "newjw?commond=exam&sessionid=" + sessionid + "&term=" + term + "&type=" + type;
        InputStream in = request(path);
        return new String(readInputStream(in), "utf-8");
    }

    public String getGradeBarInfo(String sessionid) throws Exception {
        String path = "newjw?commond=gradebar&sessionid=" + sessionid;
        InputStream in = request(path);
        String result = new String(readInputStream(in), "utf-8");
        if (result.equals("false"))
            return "";
        return result;
    }

    public String getScheduleBarInfo(String sessionid) throws Exception {
        String path = "newjw?commond=schedulebar&sessionid=" + sessionid;
        InputStream in = request(path);
        String result = new String(readInputStream(in), "utf-8");
        System.out.println(result);
        if (result.equals("false"))
            return "";
        return result;
    }

    protected static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }
}
