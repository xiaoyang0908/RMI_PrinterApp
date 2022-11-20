/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rmlproject;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


/**
 *
 * @author xiaodiezi
 */
public interface PrinterService extends Remote {
    //ALL INTERFACE
    //test demo
    public String echo(String input) throws RemoteException;
    // prints file filename on the specified printer
    public String print(String filename,String printer) throws IOException;
    // lists the print queue for a given printer on the user's display in lines of the form <job number>   <file name>
    public String queue(String printer) throws IOException;
    // moves job to the top of the queue
    public String topQueue(String printer,int job) throws IOException;
    // starts the print server
    public String start() throws IOException;
    // stops the print server
    public String stop() throws IOException;
    // stops the print server, clears the print queue and starts the print server again
    public String restart() throws IOException;
    // prints status of printer on the user's display
    public String status(String printer) throws IOException;
    // prints the value of the parameter on the user's display
    public String readConfig(String parameter) throws IOException;
    // sets the parameter to value
    public String setConfig(String parameter, String value)throws IOException;
    //authenticate user
    public boolean PasswordAuthentication(String username, String password) throws IOException;
    //check the role permission
    public ArrayList<String> checkRolePermission(String username) throws IOException;

    public String rolePermission() throws IOException;

    //print log
    public void log(String username, String functionName) throws IOException;
    //ACL
    public boolean AccessControlList(String operation) throws IOException;
    //
    public ArrayList<String> displayACL()throws IOException;

}
