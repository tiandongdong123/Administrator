package com.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.3-hudson-390-
 * Generated source version: 2.0
 * 
 */
@WebService(name = "WFDataCenterInterfaceHttpGet", targetNamespace = "http://wfdata.sale.gb168.cn/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface WFDataCenterInterfaceHttpGet {

	/**
	 * 
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "HelloWorld")
	@WebResult(name = "string", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public String helloWorld();

	/**
	 * 
	 * @return returns boolean
	 */
	@WebMethod(operationName = "IsConnected")
	@WebResult(name = "boolean", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public boolean isConnected();

	/**
	 * 
	 * @return returns javax.xml.datatype.XMLGregorianCalendar
	 */
	@WebMethod(operationName = "GetDateTime")
	@WebResult(name = "dateTime", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public XMLGregorianCalendar getDateTime();

	/**
	 * 
	 * @param userState
	 * @param startTime
	 * @param unit
	 * @param username
	 * @param cls
	 * @param onlines
	 * @param token
	 * @param copys
	 * @param rdptauth
	 * @param sigprint
	 * @param prints
	 * @param endTime
	 * @param ip
	 * @return returns int
	 */
	@WebMethod(operationName = "CreateNonAccountingUser")
	@WebResult(name = "int", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public int createNonAccountingUser(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Username") String username,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "StartTime") String startTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "EndTime") String endTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "rdptauth") String rdptauth,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "onlines") String onlines,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "copys") String copys,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "prints") String prints,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "sigprint") String sigprint,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "IP") String ip,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "CLS") String cls,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Unit") String unit,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "UserState") String userState,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Token") String token);

	/**
	 * 
	 * @param userState
	 * @param startTime
	 * @param unit
	 * @param username
	 * @param cls
	 * @param onlines
	 * @param token
	 * @param copys
	 * @param rdptauth
	 * @param sigprint
	 * @param prints
	 * @param endTime
	 * @param ip
	 * @return returns int
	 */
	@WebMethod(operationName = "UpdateNonAccountingUser")
	@WebResult(name = "int", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public int updateNonAccountingUser(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Username") String username,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "StartTime") String startTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "EndTime") String endTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "rdptauth") String rdptauth,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "onlines") String onlines,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "copys") String copys,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "prints") String prints,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "sigprint") String sigprint,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "IP") String ip,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "CLS") String cls,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Unit") String unit,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "UserState") String userState,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Token") String token);

	/**
	 * 
	 * @param userName
	 * @return returns boolean
	 */
	@WebMethod(operationName = "IsExistedNonAccountingUser")
	@WebResult(name = "boolean", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public boolean isExistedNonAccountingUser(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "userName") String userName);

	/**
	 * 
	 * @param unit
	 * @return returns int
	 */
	@WebMethod(operationName = "ValidateUnit")
	@WebResult(name = "int", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public int validateUnit(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "unit") String unit);

	/**
	 * 
	 * @param startTime
	 * @param unit
	 * @param username
	 * @param cls
	 * @param onlines
	 * @param copys
	 * @param rdptauth
	 * @param sigprint
	 * @param prints
	 * @param endTime
	 * @param ip
	 * @return returns int
	 */
	@WebMethod(operationName = "ValidateNonAccountingUser")
	@WebResult(name = "int", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public int validateNonAccountingUser(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Username") String username,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "StartTime") String startTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "EndTime") String endTime,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "rdptauth") String rdptauth,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "onlines") String onlines,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "copys") String copys,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "prints") String prints,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "sigprint") String sigprint,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "IP") String ip,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "CLS") String cls,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Unit") String unit);

	/**
	 * 
	 * @param fileID
	 * @param username
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "GetFileName")
	@WebResult(name = "string", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public String getFileName(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Username") String username,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "FileID") String fileID);

	/**
	 * 
	 * @param username
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "GetUserFolder")
	@WebResult(name = "string", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public String getUserFolder(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "username") String username);

	/**
	 * 
	 * @param unit
	 * @param fileType
	 * @param fileID
	 * @param username
	 * @param token
	 * @param postDate
	 * @param userType
	 * @return returns java.lang.String
	 */
	@WebMethod(operationName = "DownloadFileUrl")
	@WebResult(name = "string", targetNamespace = "http://wfdata.sale.gb168.cn/", partName = "Body")
	public String downloadFileUrl(
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Username") String username,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Unit") String unit,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "UserType") String userType,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "FileID") String fileID,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "FileType") String fileType,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "PostDate") String postDate,
			@WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "Token") String token);

}
