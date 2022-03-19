package part01.main;
import com.alibaba.fastjson.JSONObject;
import util.Utils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class Main {
    public static void main(String[] args) throws IOException {
        String url = "https://j1.pupuapi.com/client/product/storeproduct/detail/deef1dd8-65ee-46bc-9e18-8cf1478a67e9/c2bb76bb-8663-4c64-bc49-185aa7bfe420";
        Main main = new Main();
        // 展示商品所有信息
        String goodsData = main.getGoodsData(url);
        System.out.println(goodsData);
        // 展示实时价格(价格波动)
        main.displayPrice(url, 3);
    }
    /**
     * 功能：获取商品信息
     * @param url 商品地址
     * @return 返回商品信息
     * */
    public String getGoodsData(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        Main main = new Main();
        // 数据转json格式
        JSONObject jsonObject = JSONObject.parseObject(new Utils().sendGet(url));
        // 得到data部分
        JSONObject data1 = jsonObject.getJSONObject("data");
        // 添加关键信息 到 sb
        sb.append("-----------------------商品：").append(data1.get("name")).append("-----------------------\n");
        sb.append("规格：").append(data1.get("spec")).append("\n");
        sb.append("价格：").append(main.stringToDouble(data1.get("price").toString())).append("\n");
        sb.append("折扣价/原价：").append(main.stringToDouble(data1.get("price").toString())).append("/").append(main.stringToDouble(data1.get("market_price").toString())).append("\n");
        sb.append("-----------------------\"").append(data1.get("name")).append("\"的价格波动-----------------------\n");
        return sb.toString();
    }
    /**
     * 功能：获取商品价格
     * @param str 目标商品的数据字符串
     * @return 双精度商品价格
     * */
    public double getPrice(String str){
        // 转json 并 得到data部分
        return Integer.parseInt(JSONObject.parseObject(str).getJSONObject("data").get("price").toString())*1.00/100.00;
    }
    /**
     * 功能：展示价格波动
     * @param url 指定访问url
     * @param time 访问间隔时长（单位：秒）
     * */
    public void displayPrice(String url, int time){
        Timer timer = new Timer();
        // 定时任务
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                try{
                    double price = new Main().getPrice(new Utils().sendGet(url));
                    System.out.println("当前时间为：" + getLocalTime() + "，价格为：" + price);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },0,time*1000);
    }
    /**
     * 功能：String数字字符串转double数值
     * @param num 需要转换的字符
     * @return double数值
     * */
    public double stringToDouble(String num){
        // 判断是否为小数
        return num.contains(".") ? Double.parseDouble(num) : Integer.parseInt(num)*1.00/100.00;
    }
    /**
     * 功能：获取yyyy-MM-dd HH:mm:ss格式的当前时间
     * @return yyyy-MM-dd HH:mm:ss格式的当前时间
     * */
    public String getLocalTime(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
