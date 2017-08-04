package com.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "UserManagerServiceSoap", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface UserManagerServiceSoap {


    /**
     * ��Ӹ����û���Ϣ��ʵ����֤��Ϣ
     * 
     * @param user
     * @return
     *     returns int
     */
    @WebMethod(operationName = "AddUser", action = "http://tempuri.org/AddUser")
    @WebResult(name = "AddUserResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AddUser", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.AddUser")
    @ResponseWrapper(localName = "AddUserResponse", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.AddUserResponse")
    public int addUser(
        @WebParam(name = "user", targetNamespace = "http://tempuri.org/")
        WFUser user);

    /**
     * ���¸����û���Ϣ��ʵ����֤��Ϣ
     * 
     * @param ifUupdatePhone
     * @param user
     * @return
     *     returns int
     */
    @WebMethod(operationName = "EditUser", action = "http://tempuri.org/EditUser")
    @WebResult(name = "EditUserResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "EditUser", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.EditUser")
    @ResponseWrapper(localName = "EditUserResponse", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.EditUserResponse")
    public int editUser(
        @WebParam(name = "user", targetNamespace = "http://tempuri.org/")
        WFUser user,
        @WebParam(name = "ifUupdatePhone", targetNamespace = "http://tempuri.org/")
        boolean ifUupdatePhone);

    /**
     * ��ӻ����û���������Ϣ��IP�б���ԴȨ�ޣ���ɫ��Ϣ
     * 
     * @param settings
     * @param manages
     * @param contracts
     * @param user
     * @return
     *     returns int
     */
    @WebMethod(operationName = "AddOrganizationUser", action = "http://tempuri.org/AddOrganizationUser")
    @WebResult(name = "AddOrganizationUserResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AddOrganizationUser", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.AddOrganizationUser")
    @ResponseWrapper(localName = "AddOrganizationUserResponse", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.AddOrganizationUserResponse")
    public int addOrganizationUser(
        @WebParam(name = "user", targetNamespace = "http://tempuri.org/")
        WFUser user,
        @WebParam(name = "contracts", targetNamespace = "http://tempuri.org/")
        ArrayOfWFContract contracts,
        @WebParam(name = "manages", targetNamespace = "http://tempuri.org/")
        ArrayOfAccountIdMapping manages,
        @WebParam(name = "settings", targetNamespace = "http://tempuri.org/")
        ArrayOfUserSetting settings);

    /**
     * ���»����û���������Ϣ��IP�б���ԴȨ�ޣ���ɫ��Ϣ
     * 
     * @param settings
     * @param manages
     * @param contracts
     * @param user
     * @return
     *     returns int
     */
    @WebMethod(operationName = "EditOrganizationUser", action = "http://tempuri.org/EditOrganizationUser")
    @WebResult(name = "EditOrganizationUserResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "EditOrganizationUser", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.EditOrganizationUser")
    @ResponseWrapper(localName = "EditOrganizationUserResponse", targetNamespace = "http://tempuri.org/", className = "org.bigdata.framework.webservice.EditOrganizationUserResponse")
    public int editOrganizationUser(
        @WebParam(name = "user", targetNamespace = "http://tempuri.org/")
        WFUser user,
        @WebParam(name = "contracts", targetNamespace = "http://tempuri.org/")
        ArrayOfWFContract contracts,
        @WebParam(name = "manages", targetNamespace = "http://tempuri.org/")
        ArrayOfAccountIdMapping manages,
        @WebParam(name = "settings", targetNamespace = "http://tempuri.org/")
        ArrayOfUserSetting settings);

}
