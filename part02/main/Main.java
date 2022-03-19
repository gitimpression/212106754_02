package part02.main;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import util.Utils;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("【选做】进阶：爬取自己的知乎收藏夹，以每个收藏夹的名称为大类，其下展示各个具体收藏文章的名称及其链接。");
        String url = "https://www.zhihu.com/api/v4/people/zhi-hu-yong-hu-2021-36/collections?include=data%5B*%5D.updated_time%2Canswer_count%2Cfollower_count%2Ccreator%2Cdescription%2Cis_following%2Ccomment_count%2Ccreated_time%3Bdata%5B*%5D.creator.vip_info&offset=0&limit=20";
        // 1.获取收藏夹
        ArrayList<String> packageId = new Main().getPackageId(url);
        // 2.获取各个收藏夹里面的文章信息
        new Main().displayData(packageId);
    }

    /**
     * 功能：获取收藏夹id
     * @param url 收藏夹
     * @return 收藏夹id列表
     * */
    public ArrayList<String> getPackageId(String url) throws IOException {
        Utils utils = new Utils();
        JSONObject jsonObject = JSONObject.parseObject(utils.sendGet(url));
        JSONArray data = JSONArray.parseArray(jsonObject.get("data").toString());
        System.out.println("该用户共有" + data.size() + "个收藏夹");
        ArrayList<String> arrayList = new ArrayList<>();// 收藏夹id  方便后续请求收藏夹下的文章
        for (Object datum : data) {
            // 转换
            JSONObject temp = JSONObject.parseObject(datum.toString());
            System.out.println("title: " + temp.get("title") + " , description: " + temp.get("description") + " , url: " + temp.get("url"));
            // 记录收藏夹id
            arrayList.add(temp.get("id").toString());
        }
        return arrayList;
    }

    /**
     * 功能：输出文章信息
     * @param packageId 收藏夹id列表
     * */
    public void displayData(ArrayList<String> packageId) throws IOException {
        // 接口格式 https://www.zhihu.com/api/v4/collections/xxxxx/items?offset=0&limit=20  其中xxxxx为收藏夹id
        Utils utils = new Utils();
        System.out.println("----------------------------------------------------------------------------");
        for (Object id : packageId) {
            String _url = "https://www.zhihu.com/api/v4/collections/" + id + "/items?offset=0&limit=20";
            // 查看数据
            JSONObject object = JSONObject.parseObject(utils.sendGet(_url));
            JSONArray data1 = JSONObject.parseArray((object.get("data").toString()));// 单篇文章内容对象 每个对象5个Object
            for (Object o : data1) {
                JSONObject temp = JSONObject.parseObject(o.toString());
                JSONObject content = JSONObject.parseObject(temp.get("content").toString());// 单篇文章内容
                JSONObject question = JSONObject.parseObject(content.get("question").toString());// 问题对象
                String title = question.get("title").toString();// 问题
                String excerpt = content.get("excerpt").toString();// 回答
                String mk_content = content.get("content").toString();// Markdown格式问题，获取图片地址
                if (mk_content.indexOf("<img src=") > 0){// 存在图片
                    String mk_content_sub = mk_content.substring(mk_content.indexOf("<img src="));
                    String mk_content_sub2 = mk_content_sub.substring(0, mk_content_sub.indexOf("data-"));
                    String imgUrl = mk_content_sub2.replaceAll("\"|<img src=", "");
                    System.out.println("【图片】： " + imgUrl);
                }
                System.out.println("【问题】： " + title);
                System.out.println("【回答】： " + excerpt);
                System.out.println("----------------------------------------------------------------------------");
            }
        }
    }
}
