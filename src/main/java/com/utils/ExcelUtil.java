package com.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cuican
 * @version 1.0
 */
public class ExcelUtil {
	
    private final static String xls = "xls";  
    private final static String xlsx = "xlsx";
    private final static DecimalFormat decimalFormat = new DecimalFormat("#.000000");
    
    /**
     * 读取Excel表格表头的内容
     * @param InputStream
     * @return String 表头内容的数组
     */
    public static String[] readExcelTitle(Row row){
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getValue(row.getCell(i));
        }
        return title;
    }

	
    public static void checkFile(MultipartFile file) throws IOException{  
        //判断文件是否存在  
        if(null == file){  
            throw new FileNotFoundException("文件不存在！");  
        }  
        //获得文件名  
        String fileName = file.getOriginalFilename();  
        //判断文件是否是excel文件  
        if(!fileName.endsWith(xls) && !fileName.endsWith(xlsx)){  
            throw new IOException(fileName + "不是excel文件");  
        }  
    }  

    
    public static Workbook getWorkBook(MultipartFile file) {  
        //创建Workbook工作薄对象，表示整个excel  
        Workbook workbook = null;  
        try {  
            InputStream is = file.getInputStream();  
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象  
            if(POIFSFileSystem.hasPOIFSHeader(is)){  
                //2003  
                workbook = new HSSFWorkbook(is);  
            }else if(POIXMLDocument.hasOOXMLHeader(is)){  
                //2007  
                workbook = new XSSFWorkbook(OPCPackage.open(is));  
            }  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return workbook;
    } 

	/**
	 * 读取前校验类型
	 */ 
	public static String getValue(Cell cell){
		String strCell = "";
		if(cell!=null){			
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				strCell = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				String resultStr = decimalFormat.format(new Double(cell.getNumericCellValue() + ""));
				if (resultStr.matches("^[-+]?\\d+\\.[0]+$")) {
					strCell = resultStr.substring(0, resultStr.indexOf("."));
				} else {
					strCell = cell.getNumericCellValue() + "";
				}
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				strCell = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_BLANK:
				strCell = "";
				break;
			default:
				strCell = "";
				break;
			}
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null){
			return "";
		}
		return strCell;
	}
}
