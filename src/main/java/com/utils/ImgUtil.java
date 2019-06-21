package com.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import org.apache.log4j.Logger;
public class ImgUtil {
    private static final Logger log = Logger.getLogger(ImgUtil.class);
    /**
     * 将String类型转成BASE64格式
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String imgToBase64(String url) throws IOException {
        int width = 400;
        int height = 400;
        String format = "png";
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
        } catch (WriterException e) {
            log.error("将String类型转成BASE64格式出错,url：" + url, e);
        }
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream os = new ByteArrayOutputStream();//新建流。
        ImageIO.write(image, format, os);//利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
        byte b[] = os.toByteArray();//从流中获取数据数组。
        String str = new BASE64Encoder().encode(b);
        return str;
    }

    /**
     * 图片转Base64
     *
     * @param mFiles
     * @return
     * @throws Exception
     */
    public static String imageToBase64(MultipartFile mFiles, String suffix) throws Exception {
        String encoded;
        InputStream inputStream = null;
        try {
            CommonsMultipartFile cFile = (CommonsMultipartFile) mFiles;
            DiskFileItem fileItem = (DiskFileItem) cFile.getFileItem();
            inputStream = fileItem.getInputStream();
            byte[] bytes = readInputStream(inputStream);
            encoded = getImageString(bytes, suffix);
        } catch (Exception e) {
            throw new Exception("MultipartFile转Base64出错！");
        } finally {
            inputStream.close();
        }
        return encoded;
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 二进制流转Base64字符串
     *
     * @param data 二进制流
     * @return data
     * @throws IOException 异常
     */
    public static String getImageString(byte[] data, String suffix) throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        return data != null ? "data:image/" + suffix + ";base64," + encoder.encode(data) : "";
    }

    /**
     * file转换base64
     *
     * @param path 文件路径
     * @return
     */
    public static String fileToBase64(String path, String suffix) throws Exception {
        String base64;
        InputStream in = null;
        if (suffix == null) {
            suffix = "jpg";
        }
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = readInputStream(in);
            base64 = getImageString(bytes, suffix);
        } catch (Exception e) {
            log.error("file转换base64出错！path：" + path, e);
            throw new Exception("file转换base64出错！", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error("file转换base64,关闭流出错！path：" + path, e);
                }
            }
        }
        return base64;
    }

    /**
     * base64下载
     *
     * @param imgStr
     * @return
     */
    public static ResponseEntity<byte[]> GenerateImage(String name, String imgStr, HttpServletResponse response) { // 对字节数组字符串进行Base64解码并生成图片
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            String dfileName = name + "." + imgStr.substring(11, 14);
            // Base64解码
            byte[] buffer = decoder.decodeBuffer(imgStr.substring(22));
            // 处理数据
            for (int i = 0; i < buffer.length; ++i) {
                if (buffer[i] < 0) {
                    buffer[i] += 256;
                }
            }
            response.setCharacterEncoding("UTF-8");
            //下面开始设置HttpHeaders,使得浏览器响应下载
            HttpHeaders headers = new HttpHeaders();
            //设置响应方式
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            // 设置响应文件
            headers.setContentDispositionFormData("attachment", new String(dfileName.getBytes(), "ISO8859-1"));
            //  把文件以二进制形式写回
            return new ResponseEntity<byte[]>(buffer, headers, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("");
        }
        return null;
    }
}
