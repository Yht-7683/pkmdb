package com.pkm.pkmdb.common;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 
 * @author vm-wanghao
 *
 */
public class Base64Utils {
    private final static Logger log = LoggerFactory.getLogger(com.pkm.pkmdb.common.Base64Utils.class);
    public static void main(String[] args) {
        String strImg = GetImageStr();
        System.out.println(strImg);
        //GenerateImage(strImg);
    }

    //图片转化成base64字符串
    public static String GetImageStr() {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "d://test.jpg";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        return Base64.encodeBase64String(data);//返回Base64编码过的字节数组字符串
    }

    //base64字符串转化成图片
    public static boolean GenerateImage(String imgData, String imgFilePath) { //对字节数组字符串进行Base64解码并生成图片
        if (imgData == null) //图像数据为空
            return false;
        try {
            //Base64解码
            byte[] b = Base64.decodeBase64(imgData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            log.info("55555555555555 :: "+imgFilePath);
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("66666 :: "+e.getMessage());
            return false;
        }
    }
}
