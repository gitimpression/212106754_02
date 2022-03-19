package util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * 发送请求的工具类
 * */
public class Utils {
    /**
     * 功能：发送GET请求，获取响应体
     *
     * @param _url 请求URL
     * @return 响应体
     */
    public String sendGet(String _url) throws IOException {
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestProperty("Host","j1.pupuapi.com");
//        connection.setRequestProperty("Connection","keep-alive");
//        connection.setRequestProperty("Accept","application/json");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
//        connection.setRequestProperty("content-type","application/json");
//        connection.setRequestProperty("open-id","oMwzt0HELosalSPJfmKq2cifPTAk");
//        connection.setRequestProperty("pp-os","0");
//        connection.setRequestProperty("pp-placeid","227bd5f1-40d5-4bdf-b5c2-2e091a512c9d");
//        connection.setRequestProperty("pp-version","2021063100");
//        connection.setRequestProperty("pp_storeid","7c1208da-907a-4391-9901-35a60096a3f9");
//        connection.setRequestProperty("Referer","https://servicewechat.com/wx122ef876a7132eb4/156/page-frame.html");
//        connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
//        connection.setRequestProperty("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8");
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder result = new StringBuilder();
        String lines;
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), StandardCharsets.UTF_8);
            result.append(lines).append("\n");
        }
        // 关闭流
        reader.close();
        // 关闭连接
        connection.disconnect();
        // 返回结果
        return result.toString();
    }

    /**
     * 功能：发送POST请求，获取响应体
     * @param _url 请求URL
     * @param param 请求体
     * @return 响应体
     */
    public String sendPost(String _url, String param) {
        PrintWriter out = null;// URLConnection的输出流
        BufferedReader in = null;// 读取响应
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(_url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36 MicroMessenger/7.0.9.501 NetType/WIFI MiniProgramEnv/Windows WindowsWechat");
            // 在连接之前 设置可读写
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // 输出流缓冲
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {// 关闭流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }

}
