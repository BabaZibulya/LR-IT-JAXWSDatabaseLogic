
package com.univ.it.ws;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.FileNotFoundException;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface IWebServiceDataBase {

    @WebMethod
    public boolean addTable(String tableName, String tableRepresentation);

    @WebMethod
    public boolean dropTable(String tableName);

    @WebMethod
    public WebServiceTable[] getTables();

    @WebMethod
    public void addRow(String tableName, String row) throws Exception;

    @WebMethod
    public void save() throws FileNotFoundException;

    @WebMethod
    public WebServiceTable getTable(String tableName);

    @WebMethod
    public WebServiceTable calculateDifferenceBetween(String table1, String table2);
}
