package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 公共工具类
 */
public class CommonUtils {

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isNotBlank(key)) {
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
        return null;
    }

    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",code);
        jsonObject.put("msg",msg);
        if(map!=null){
            for (String key:map.keySet()) {
                jsonObject.put(key,map.get(key));
            }
        }
        return jsonObject.toJSONString();
    }
    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

    public static int dayOfYearByLocalDate(int year, int month, int day) throws ParseException {
        String s = year+"-"+month+"-"+day;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = simpleDateFormat.parse(s);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parse);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static void main(String[] args) throws ParseException {
//        int i = CommonUtils.dayOfYearByLocalDate(2020, 1, 22);
//        System.out.println(i);
    }


}
