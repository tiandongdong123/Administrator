package com.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * 二维码工具类
 * Created by zhangfutao on 2017/4/10.
 */
public class QRCodeUtil {
    private static Logger log = Logger.getLogger(QRCodeUtil.class);
    /**
     * 二维码格式
     */
    private static final String FORMAT = "png";

    /**
     * 向页面输出二维码
     *
     * @param response HttpServletResponse
     * @param URL      url地址
     * @param width    宽度
     * @param height   高度
     */
    public static void outputQRCode(HttpServletResponse response, String URL, int width, int height) {
        OutputStream stream;
        try {
            stream = response.getOutputStream();
            MatrixToImageWriter.writeToStream(QRCodeUtil.createQRCode(URL, width, height), FORMAT, stream);
        } catch (IOException e) {
            log.error("QRCodeUtil类在向页面输出二维码时失败，二维码URL为：" + URL + "，宽度为：" + width + "，高度为：" + height + "，response：" + response, e);
        }
    }

    /**
     * 生成二维码方法
     *
     * @param URL    生成URL
     * @param width  宽度
     * @param height 高度
     * @return
     */
    public static BitMatrix createQRCode(String URL, int width, int height) {
        //设置二维码的参数
        HashMap hints = new HashMap();
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(URL, BarcodeFormat.QR_CODE, width, height, hints);
            return bitMatrix;
        } catch (Exception e) {
            log.error("QRCodeUtil类在生成二维码时失败，二维码URL为：" + URL + "，宽度为：" + width + "，高度为：" + height, e);
            return null;
        }
    }


}

