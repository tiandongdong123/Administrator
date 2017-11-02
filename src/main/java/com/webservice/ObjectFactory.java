package com.webservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.webservice package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _Int_QNAME = new QName("http://wfdata.sale.gb168.cn/", "int");
	private final static QName _String_QNAME = new QName("http://wfdata.sale.gb168.cn/", "string");
	private final static QName _DateTime_QNAME = new QName("http://wfdata.sale.gb168.cn/",
			"dateTime");
	private final static QName _Boolean_QNAME = new QName("http://wfdata.sale.gb168.cn/", "boolean");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.webservice
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link UpdateNonAccountingUserResponse }
	 * 
	 */
	public UpdateNonAccountingUserResponse createUpdateNonAccountingUserResponse() {
		return new UpdateNonAccountingUserResponse();
	}

	/**
	 * Create an instance of {@link IsExistedNonAccountingUserResponse }
	 * 
	 */
	public IsExistedNonAccountingUserResponse createIsExistedNonAccountingUserResponse() {
		return new IsExistedNonAccountingUserResponse();
	}

	/**
	 * Create an instance of {@link DownloadFileUrlResponse }
	 * 
	 */
	public DownloadFileUrlResponse createDownloadFileUrlResponse() {
		return new DownloadFileUrlResponse();
	}

	/**
	 * Create an instance of {@link ValidateNonAccountingUser }
	 * 
	 */
	public ValidateNonAccountingUser createValidateNonAccountingUser() {
		return new ValidateNonAccountingUser();
	}

	/**
	 * Create an instance of {@link ValidateUnit }
	 * 
	 */
	public ValidateUnit createValidateUnit() {
		return new ValidateUnit();
	}

	/**
	 * Create an instance of {@link ValidateUnitResponse }
	 * 
	 */
	public ValidateUnitResponse createValidateUnitResponse() {
		return new ValidateUnitResponse();
	}

	/**
	 * Create an instance of {@link GetFileResponse }
	 * 
	 */
	public GetFileResponse createGetFileResponse() {
		return new GetFileResponse();
	}

	/**
	 * Create an instance of {@link IsConnected }
	 * 
	 */
	public IsConnected createIsConnected() {
		return new IsConnected();
	}

	/**
	 * Create an instance of {@link GetFileNameResponse }
	 * 
	 */
	public GetFileNameResponse createGetFileNameResponse() {
		return new GetFileNameResponse();
	}

	/**
	 * Create an instance of {@link UpdateNonAccountingUser }
	 * 
	 */
	public UpdateNonAccountingUser createUpdateNonAccountingUser() {
		return new UpdateNonAccountingUser();
	}

	/**
	 * Create an instance of {@link CreateNonAccountingUserResponse }
	 * 
	 */
	public CreateNonAccountingUserResponse createCreateNonAccountingUserResponse() {
		return new CreateNonAccountingUserResponse();
	}

	/**
	 * Create an instance of {@link GetUserFolder }
	 * 
	 */
	public GetUserFolder createGetUserFolder() {
		return new GetUserFolder();
	}

	/**
	 * Create an instance of {@link ValidateNonAccountingUserResponse }
	 * 
	 */
	public ValidateNonAccountingUserResponse createValidateNonAccountingUserResponse() {
		return new ValidateNonAccountingUserResponse();
	}

	/**
	 * Create an instance of {@link GetFileName }
	 * 
	 */
	public GetFileName createGetFileName() {
		return new GetFileName();
	}

	/**
	 * Create an instance of {@link CreateNonAccountingUser }
	 * 
	 */
	public CreateNonAccountingUser createCreateNonAccountingUser() {
		return new CreateNonAccountingUser();
	}

	/**
	 * Create an instance of {@link GetDateTimeResponse }
	 * 
	 */
	public GetDateTimeResponse createGetDateTimeResponse() {
		return new GetDateTimeResponse();
	}

	/**
	 * Create an instance of {@link DownloadFileUrl }
	 * 
	 */
	public DownloadFileUrl createDownloadFileUrl() {
		return new DownloadFileUrl();
	}

	/**
	 * Create an instance of {@link HelloWorld }
	 * 
	 */
	public HelloWorld createHelloWorld() {
		return new HelloWorld();
	}

	/**
	 * Create an instance of {@link GetUserFolderResponse }
	 * 
	 */
	public GetUserFolderResponse createGetUserFolderResponse() {
		return new GetUserFolderResponse();
	}

	/**
	 * Create an instance of {@link IsExistedNonAccountingUser }
	 * 
	 */
	public IsExistedNonAccountingUser createIsExistedNonAccountingUser() {
		return new IsExistedNonAccountingUser();
	}

	/**
	 * Create an instance of {@link GetDateTime }
	 * 
	 */
	public GetDateTime createGetDateTime() {
		return new GetDateTime();
	}

	/**
	 * Create an instance of {@link IsConnectedResponse }
	 * 
	 */
	public IsConnectedResponse createIsConnectedResponse() {
		return new IsConnectedResponse();
	}

	/**
	 * Create an instance of {@link HelloWorldResponse }
	 * 
	 */
	public HelloWorldResponse createHelloWorldResponse() {
		return new HelloWorldResponse();
	}

	/**
	 * Create an instance of {@link GetFile }
	 * 
	 */
	public GetFile createGetFile() {
		return new GetFile();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://wfdata.sale.gb168.cn/", name = "int")
	public JAXBElement<Integer> createInt(Integer value) {
		return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://wfdata.sale.gb168.cn/", name = "string")
	public JAXBElement<String> createString(String value) {
		return new JAXBElement<String>(_String_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link XMLGregorianCalendar }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://wfdata.sale.gb168.cn/", name = "dateTime")
	public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
		return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://wfdata.sale.gb168.cn/", name = "boolean")
	public JAXBElement<Boolean> createBoolean(Boolean value) {
		return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
	}

}
