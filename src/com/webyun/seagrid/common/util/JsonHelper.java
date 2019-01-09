package com.webyun.seagrid.common.util;

import java.util.List;
import java.util.Map;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * JSON封装工具类
 * 
 * @author 彭颂
 * @version [版本号, 2013年11月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class JsonHelper {
    /** 从普通的Bean转换为字符串 * */
    public static String getJson(Object o) {
        JSONObject jo = JSONObject.fromObject(o);
        return jo.toString();
    }
    
    /** 从Java的列表转换为字符串 * */
    public static String getJson(List list) {
        JSONArray ja = JSONArray.fromObject(list);
        return ja.toString();
    }
    
    /** 从Java对象数组转换为字符串 * */
    public static String getJson(Object[] arry) {
        JSONArray ja = JSONArray.fromObject(arry);
        return ja.toString();
    }
    
    /** 从json格式的字符串转换为Map对象 * */
    public static Map getObject(String s) {
        return JSONObject.fromObject(s);
    }
    
    /** 从json格式的字符串转换为List数组 * */
    public static List getArray(String s) {
        return JSONArray.fromObject(s);
    }
    
    /** 从json格式的字符串转换为某个Bean * */
    public static Object getObject(String s, Class cls) {
        JSONObject jo = JSONObject.fromObject(s);
        return JSONObject.toBean(jo, cls);
    }
    
    /** 从json格式的字符串转换为某类对象的数组 * */
    public static Object getArray(String s, Class cls) {
        JSONArray ja = JSONArray.fromObject(s);
        return JSONArray.toArray(ja, cls);
    }
    
    public static void main(String[] args) {
//        String xml = "{\"sign\":\"1234\",\"msg\":\"成功\",\"data\":[{\"status\":\"success\",\"alipaySid\":\"123456\"}]}";
//        Map json = JsonHelper.getObject(xml);
//        String signTaobao = (String)json.get("sign");// 获取签名
//        String msg = (String)json.get("msg");// 获取信息抬头
//        JSONObject data = (JSONObject)json.get("data");// 获取data数据
//        Map dataJsonMap = JsonHelper.getObject(JsonHelper.getJson(data));// 解析data 的数据,成为map
//        String status = (String)dataJsonMap.get("status");// 交易状态: pay 已付款,unpay 未付款,success 出票成功,fail 出票失败
//        String alipaySid = (String)dataJsonMap.get("alipaySid");// 淘宝支付交易号
//        System.out.println(signTaobao + "|" + msg + "|" + status + "|" + alipaySid);
        
//        GradeQPF06Dao dao = new GradeQPF06Dao();
//        try {
//            List<Map<String, Object>> list = dao.findDynamicGrade("","" ,null, "00012", "2013-09-01", "2013-09-30");
//            System.out.println(JsonHelper.getJson(list));
//            for (Map<String, Object> map : list) {
//                System.out.println(map);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
   }
}
