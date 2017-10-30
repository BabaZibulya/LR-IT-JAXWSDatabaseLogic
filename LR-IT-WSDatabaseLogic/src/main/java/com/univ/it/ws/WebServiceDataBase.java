package com.univ.it.ws;

import com.univ.it.table.LocalDataBase;
import com.univ.it.table.Table;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.Collection;


@WebService(endpointInterface = "com.univ.it.ws.IWebServiceDataBase")
public class WebServiceDataBase implements IWebServiceDataBase {

    private LocalDataBase db;
    private final String PATH_TO_DB = "/home/bondarenko";
    private final String DB_NAME = "test.db";

    private WebServiceDataBase() throws Exception {
        db = LocalDataBase.readFromFile(PATH_TO_DB + "/" + DB_NAME);
    }

    public static void main(String[] args) {
        try {
            Endpoint ep = Endpoint.create(new WebServiceDataBase());
            ep.publish("http://127.0.0.1:10001/service");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean addTable(String tableName, String tableRepresentation) {
        try {
            return db.addTable(
                    new Table(
                            new BufferedReader(new StringReader(tableRepresentation)),
                            tableName
                    )
            );
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean dropTable(String tableName) {
        return db.dropTable(tableName);
    }

    @Override
    public WebServiceTable[] getTables() {
        WebServiceTable[] tablesStrings = new WebServiceTable[db.getTables().size()];
        Collection<Table> tables = db.getTables().values();
        int i = 0;
        for (Table table : tables) {
            tablesStrings[i] = new WebServiceTable(table.getName(), table.toString());
            ++i;
        }
        return tablesStrings;
    }

    @Override
    public void addRow(String tableName, String row) throws Exception {
        db.getTable(tableName).addNewRow(row);
    }

    @Override
    public void save() throws FileNotFoundException {
        db.writeToFile(PATH_TO_DB);
    }

    @Override
    public WebServiceTable getTable(String tableName) {
        return new WebServiceTable(db.getTable(tableName).getName(), db.getTable(tableName).toString());
    }

    @Override
    public WebServiceTable calculateDifferenceBetween(String table1, String table2) {
        Table difference = Table.differenceBetween(db.getTable(table1), db.getTable(table2));
        db.addTable(difference);
        return new WebServiceTable(difference.getName(), difference.toString());
    }
}
