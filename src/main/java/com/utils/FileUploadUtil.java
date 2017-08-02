package com.utils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

public class FileUploadUtil {
	protected static Logger logger = Logger.getLogger(FileUploadUtil.class);

	public static boolean checkFileSize(HttpServletRequest request, int maxSize){
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if(file.getSize()>maxSize){
					return false;
				}
			}
		}
		return true;
	}
	public static String upload(HttpServletRequest request, String basePath){

		String realpath = request.getServletContext().getRealPath("/");
		Calendar calendar = Calendar.getInstance();
		String pp = basePath + calendar.get(Calendar.YEAR) + 
				+ (calendar.get(Calendar.MONTH)+1) + 
				+ calendar.get(Calendar.DAY_OF_MONTH) + "_"+calendar.get(Calendar.HOUR)+ "_" + calendar.get(Calendar.MINUTE);
	
		realpath += pp;

		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// System.out.println(myFileName);
						String fix = myFileName.substring(myFileName
								.lastIndexOf("."));
						// 重命名上传后的文件名
						//String fileName = "/"+StringUtil.getIdByDate() + fix;
						String fileName = "/"+myFileName;
						// 定义上传路径
						String path = realpath + fileName;
						File dir = new File(realpath);
						//判断上传文件的保存目录是否存在
						if (!dir.exists() && !dir.isDirectory()) {
							System.out.println(dir+"目录不存在，需要创建");
							//创建目录
							dir.mkdirs();
						}
						
						File localFile = new File(path);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
							return null;
						}

						// 相对路径
						return pp + fileName;

					}
				}
			}
		}
		return null;
	}
	
	public static String uploadByGivenPath(HttpServletRequest request, String basePathParameter){
		String realpath = request.getServletContext().getRealPath("/");
		/*Calendar calendar = Calendar.getInstance();
		String pp = basePath + calendar.get(Calendar.YEAR) + "/"
				+ (calendar.get(Calendar.MONTH)+1) + "/"
				+ calendar.get(Calendar.DAY_OF_MONTH);*/
		realpath += basePathParameter;

		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				// 取得上传文件
				MultipartFile file = multiRequest.getFile(iter.next());
				if (file != null) {
					// 取得当前上传文件的文件名称
					String myFileName = file.getOriginalFilename();
					// 如果名称不为“”,说明该文件存在，否则说明该文件不存在
					if (myFileName.trim() != "") {
						// System.out.println(myFileName);
						String fix = myFileName.substring(myFileName
								.lastIndexOf("."));
						// 重命名上传后的文件名
						String fileName = "/"+StringUtil.getIdByDate() + fix;
						// 定义上传路径
						String path = realpath + fileName;
						File dir = new File(realpath);
						//判断上传文件的保存目录是否存在
						if (!dir.exists() && !dir.isDirectory()) {
							System.out.println(dir+"目录不存在，需要创建");
							//创建目录
							dir.mkdirs();
						}
						
						File localFile = new File(path);
						try {
							file.transferTo(localFile);
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
							return null;
						}
						// 相对路径
						return basePathParameter + fileName;
					}
				}
			}
		}
		return null;
	}
}
