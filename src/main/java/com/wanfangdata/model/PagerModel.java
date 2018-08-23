package com.wanfangdata.model;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 分页model类
 * <p>
 * 调用构造函数依次传递：查找第几页，数据库共多少条数据，每页需显示多少容量，查找到的数据，页面超链接url
 * 构造器将在初始化时进行其他数据的初始化，如：页面的总数，当前页起始数据条数，是否有上一页，是否有下一页，是否只有一页
 * btl页面可以通过model对象获取到这些数据
 * 注意：
 * 如果使用不传递“封装的参数类”的构造方法，那么url属性最后一定要是 “page=”
 * 如果使用传递“封装的参数类”的构造方法，那么封装的参数类里页面属性一定为“page”
 *
 * @param <T> 检索对象泛型
 * @author zhangft
 */
public class PagerModel<T> {
    /**
     * 当前页
     */
    private int currentPage;
    /**
     * 页面要显示多少条数据
     */
    private int pageSize;
    /**
     * 总记录
     */
    private int total;
    /**
     * 页面数据的集合
     */
    private List<T> pageRecords;
    /**
     * 页面超链接
     */
    private String url;
    /**
     * 页面的总数
     */
    private int pageCount;
    /**
     * 当前页起始数据条数
     */
    private int startIndex;
    /**
     * 是否有上一页
     */
    private boolean hasPreviousPage;
    /**
     * 是否有下一页
     */
    private boolean hasNextPage;
    /**
     * 是否只有一页
     */
    private boolean onlyOnePage;
    /**
     * 拼接url时用：是否是第一个参数
     */
    boolean isFirstHaveValue = true;


    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 构造方法，只构造空页.
     */
    public PagerModel()  {
        this(1, 0, 10, new ArrayList(), "", null);
    }

    /**
     * 构造方法，只构造空页.
     */
    public PagerModel(int currentPage, int pageSize) throws Exception {
        this(currentPage, 0, pageSize, new ArrayList(), "", null);
    }

    /**
     * 默认构造方法
     *
     * @param currentPage 查找第几页
     * @param totalSize   数据库共多少条数据
     * @param pageSize    每页需显示多少容量
     * @param data        查找到的数据
     * @param url         拼接好的url路径，最后一个参数为page=（如order/index?page=）
     */
    public PagerModel(int currentPage, int totalSize, int pageSize, List data, String url) {
        this.currentPage = currentPage;
        this.total = totalSize;
        this.pageSize = pageSize;
        this.pageRecords = data;
        this.url = url;
        initPagerData();
    }


    /**
     * 默认构造方法
     *
     * @param currentPage 查找第几页
     * @param totalSize   数据库共多少条数据
     * @param pageSize    每页需显示多少容量
     * @param data        查找到的数据
     * @param actionUrl   请求路径url（如order/index）
     * @param parameter    参数封装类型
     */
    public PagerModel(int currentPage, int totalSize, int pageSize, List data, String actionUrl, Object parameter) {
        this.currentPage = currentPage;
        this.total = totalSize;
        this.pageSize = pageSize;
        this.pageRecords = data;
        this.url = jointUrl(actionUrl, parameter);
        initPagerData();
    }

    public String jointUrl(String url, Object parameter) {
        if (parameter == null) {
            return url;
        }
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = parameter.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 拿到该属性的get方法
            String fieldName = field.getName();
            Method m;
            Object value;
            String fieldValue;
            try {
                //获取get方法
                m = parameter.getClass().getMethod("get" + getMethodName(fieldName));
                value = m.invoke(parameter);
            } catch (Exception e) {
                //使用get方法获取到的值为空时会抛出异常，直接遍历下一个参数
                continue;
            }
            if (value != null) {
                fieldValue = String.valueOf(value);
            } else {
                continue;
            }
            if (fieldName.equals("page")) {
                continue;
            } else if (value.getClass().isArray()) {

                Object[] object = (Object[]) value;
                for (Object item : object) {
                    if (item == null) {
                        continue;
                    }
                    url = executeJoint(url, fieldName, item);
                }
            } else if (value instanceof Date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String date = format.format(value);
                url = executeJoint(url, fieldName, date);
            } else if (!fieldValue.isEmpty()) {
                url = executeJoint(url, fieldName, fieldValue);
            }
        }
        if (isFirstHaveValue) {
            url = url + "?page=";
        } else {
            url = url + "&page=";
        }
        return url;
    }

    /**
     * 执行url拼接
     * @param url 要拼接的url
     * @param fieldName 字段名
     * @param value 字段值
     * @return
     */
    private String executeJoint(String url, String fieldName, Object value) {
        if (isFirstHaveValue) {
            url = url + "?" + fieldName + "=" + value;
            isFirstHaveValue = false;
        } else {
            url = url + "&" + fieldName + "=" + value;
        }
        return url;
    }


    /**
     * 把一个字符串的第一个字母大写（为了获取get方法）
     *
     * @param fieldName
     * @return
     * @throws Exception
     */
    private static String getMethodName(String fieldName) throws Exception {
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        String a = new String(items);
        return a;
    }

    /**
     * 初始化PagerModel数据
     */
    private void initPagerData() {
        getPageCount();
        getstartIndex();
        isHasPreviousPage();
        isHasNextPage();
        isOnlyOnePage();
    }

    /**
     * 输入当前页面
     *
     * @param currentPage 当前页面
     */
    public void setCurrentPage(int currentPage) {
        if (currentPage < 1) {
            this.currentPage = 1;
            return;
        }
        this.currentPage = currentPage;
    }

    /**
     * 获取每页显示数据数目
     *
     * @return 每页显示数据数目
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页显示数据数目
     *
     * @param pageSize 每页显示数据数目
     */
    public void setPageSize(int pageSize) {
        if (pageSize <= 0) {
            this.pageSize = 1;
        } else {
            this.pageSize = pageSize;
        }
    }

    /**
     * 获取数据总数目
     *
     * @return 数据总数目
     */
    public int getTotal() {
        return total;
    }

    /**
     * 设置数据总数目
     *
     * @param totalRecords 数据总数目
     */
    public void setTotal(int totalRecords) {
        this.total = totalRecords;
    }

    /**
     * 获取页面总数
     *
     * @return 页面总数
     */
    public int getPageCount() {
        if (total == 0) {
            pageCount = 1;
            return pageCount;
        }
        boolean isZero = total % pageSize == 0;
        pageCount = total / pageSize;
        pageCount = isZero ? pageCount : pageCount + 1;
        return pageCount;
    }

    /**
     * 获取当前页起始数据条数
     *
     * @return 当前页起始数据条数
     */
    public int getstartIndex() {
        startIndex = ((currentPage - 1) * pageSize);
        return startIndex;
    }

    /**
     * 判断是否有前一页
     */
    public boolean isHasPreviousPage() {
        hasPreviousPage = (currentPage != 1);
        return hasPreviousPage;
    }

    /**
     * 判断是否有下一页
     */
    public boolean isHasNextPage() {
        hasNextPage = (currentPage != getPageCount());
        return hasNextPage;
    }

    /**
     * 判断是否只有一页
     */
    public boolean isOnlyOnePage() {
        onlyOnePage = (getPageCount() == 1);
        return onlyOnePage;
    }

    /**
     * 获取查询结果集合
     *
     * @return 获取查询结果
     */
    public List<T> getPageRecords() {
        return pageRecords;
    }

    /**
     * 存入查询结果
     *
     * @param pageRecords 查询结果集合
     */
    public void setPageRecords(List<T> pageRecords) {
        this.pageRecords = pageRecords;
    }

    /**
     * 获取页面超链接
     *
     * @return url页面超链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置页面超链接
     *
     * @param url 页面超链接
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
