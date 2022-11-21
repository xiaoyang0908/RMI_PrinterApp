/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rmlproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author xiaodiezi
 */
public class PrinterServant extends UnicastRemoteObject implements PrinterService{
    private  HashMap<String, Printer> printers = new HashMap<>();
    private User currentuser = new User("","",false);
    private int user_file_line = 0;
    ArrayList<String> ACLlist=new ArrayList<>();
    ArrayList<String> permissionList;

    public PrinterServant() throws RemoteException {
        super();
        int printer_num = 4;
        String[] printer_name = new String[]{"Printer1","Printer2","Printer3","Printer4"};
        for (int i=0;i<printer_num;i++)
        {
            this.printers.put(printer_name[i],new Printer(printer_name[i]));
        }
    }
    @Override
    public String echo(String input) throws RemoteException {
       return "From sever:" + input;
    }
    @Override
    public String print(String filename, String printer) throws IOException {
        Printer current =this.printers.get(printer);

        if(current!=null)
        {
            current.addfile();
            PrinterFile newfile = new PrinterFile(filename, current.getFileid(), currentuser.getUsername());
            current.getPrintfile().add(newfile);
            return "add a newfile successful";

        }
        else
        {
            return"This printer is not exist";
        }
    }


    @Override
    public String queue(String printer) throws IOException {
        Printer current =this.printers.get(printer);

        String queues="";
        if(current!=null)
        {
            List<PrinterFile> files = current.getPrintfile();
            for (int i = 0; i < files.size(); i++) {
                PrinterFile file = files.get(i);
                if (file.getUsername().equals(currentuser.getUsername())) {
                    queues = queues+file.getId() + " , " + file.getName()+"\n";
                }
            }
            return queues;
        }
        else
        {
            return"This printer is not exist";
        }
    }

    @Override
    public String topQueue(String printer, int job) throws IOException {
        Printer targetPrinter = this.printers.get(printer);

        if (targetPrinter != null) {
            List<PrinterFile> fileList = targetPrinter.getPrintfile();
            for (int i = 0; i < fileList.size(); i++) {
                if (fileList.get(i).getUsername().equals(currentuser.getUsername()) && fileList.get(i).getId() == job) {
                    fileList.add(0, fileList.get(i));
                    fileList.remove(i + 1);
                    return "topQueue successful";
                }
            }
            return "This filename not exist.";
        } else {
            return "This printer is not exist";
        }
    
    }

    @Override
    public String start() throws IOException {
        currentuser.setStatus(true);
       return "Start Server Successfull";
    }

    @Override
    public String stop() throws IOException {

        return "Stop the Print Server Successful. Now we are backing to the start server page ";
    }

    @Override
    public String restart() throws IOException {

        int printer_num=4;
        for (int i=0;i<printer_num;i++)
        {
            this.printers.remove("Printer"+(i+1));
        }
        String[] printer_name = new String[]{"Printer1","Printer2","Printer3","Printer4"};
        for (int i=0;i<printer_num;i++)
        {
            this.printers.put(printer_name[i],new Printer(printer_name[i]));
        }
       return "restart the printer server successful";
    }

    @Override
    public String status(String printer) throws IOException {

        String status="";
        int file_num = 0;
        Printer current =this.printers.get(printer);
        String queues="";
        if(current!=null)
        {
            List<PrinterFile> files = current.getPrintfile();
            for (int i = 0; i < files.size(); i++) {
                PrinterFile file = files.get(i);
                if (file.getUsername().equals(currentuser.getUsername())) {
                    file_num++;
                    queues = queues+file.getName()+"  ";
                }
            }
        }
        else
        {
            return"This printer is not exist";
        }
        status ="Printer Name : "+ printer + "\n"+"Number of File : "+ file_num+ "\n"
                +"Queue of File : " + queues + "\n";
        return status;
    }

    @Override
    public String readConfig(String parameter) throws IOException {

        switch (parameter) {
            case "username":
                return currentuser.getUsername();
            case "password":
                return currentuser.getPassword();
            default:
                return "this parameter is not exist";
        }
    }

    @Override
    public String setConfig(String parameter, String value) throws IOException {

       String[] userinfo = new String[10];
       String[] userinfo1 = new String[10];
               
       File file = new File("src/data/EncryptUserInfo.txt");
       FileInputStream input =  new FileInputStream(file);
       BufferedReader reader = new BufferedReader(new InputStreamReader(input));
       String tempString;
       int del_line=0;
       List<String> list = new ArrayList<>();
       while((tempString=reader.readLine())!=null)
       {
           list.add(tempString);
       }
       for(String user:list)
       {
           userinfo = user.split(",");
       }
       for(String delUserinfo : list)
       {
           userinfo1 = delUserinfo.split(",");
           del_line++;
           if(user_file_line==del_line)
           {
               list.remove(delUserinfo);
               FileWriter fd = new FileWriter(file,false);
               fd.write("");
               fd.close();
               break;
           }
       }
       
       //rewrite new value into the file
       for(String user:list)
       {
           userinfo1 = user.split(",");
           FileWriter fw = new FileWriter(file,true);
           fw.write(userinfo1[0]+","+userinfo1[1]);
           fw.write(System.getProperty("line.separator"));
           fw.close();
       }
       
       FileWriter fw = new FileWriter(file,true);
       if(parameter.equals("password"))
       {
        fw.write(this.currentuser.getUsername()+","+HashAndSalt.encryption(value));
        fw.write(System.getProperty("line.separator"));
        fw.close();
       }
       else if(parameter.equals("username"))
       {
           fw.write(value+","+HashAndSalt.encryption(this.currentuser.getPassword()));
           fw.write(System.getProperty("line.separator"));
           fw.close();
       }
       else
       {
           return "Error: this parameter is not exist";
       }
       
       //delete
       
       return "update successful";
    }

    @Override
    public boolean PasswordAuthentication(String username, String password) throws IOException {
        BufferedReader userInfo = new BufferedReader(new FileReader("src/data/EncryptUserInfo.txt"));
        int key = 1000;
        String usr;
        user_file_line=0;
        while ((usr = userInfo.readLine())!=null){
            String[] info = usr.split(",");
            user_file_line++;
            if (username.equals(info[1])){
                if(Xor.passwordDecryption(info[2],key).equals(password))
                {
                    currentuser.setUsername(username);
                    currentuser.setPassword(password);
                    return true;
                }
            }
            //return false;
        }
        //userInfo.close();
        return false;
    }

    @Override
    public ArrayList<String> checkRolePermission(String username) throws IOException {
        //get the role of user
        BufferedReader userRoles = new BufferedReader(new FileReader("src/data/UserRoles.txt"));
        String roles;
        user_file_line=0;
        String role = null;
        while ((roles = userRoles.readLine())!=null) {
            String[] roleInfo = roles.split(",");
            user_file_line++;
            if (username.equals(roleInfo[0])){
                role = roleInfo[1];
            }
        }

        //get permission from RolePermission
        BufferedReader permissionRole = new BufferedReader(new FileReader("src/data/RolePermission.txt"));
        String permission;
        int i=0;
        permissionList=new ArrayList<>();
        while ((permission = permissionRole.readLine())!=null) {
            String[] permissionInfo = permission.split(",");
            if (role.equals(permissionInfo[0])){
                for(i=1;i<permissionInfo.length;i++){
                    if(!permissionInfo[i].equals("-"))
                    {
                        permissionList.add(permissionInfo[i]);
                    }
                }
            }
        }
        return permissionList;
    }

    @Override
    public String rolePermission() throws IOException {
        //get the role of user
        BufferedReader userRoles = new BufferedReader(new FileReader("src/data/UserRoles.txt"));
        String roles;
        user_file_line=0;
        String role = null;
        while ((roles = userRoles.readLine())!=null) {
            String[] roleInfo = roles.split(",");
            user_file_line++;
            if (this.currentuser.getUsername().equals(roleInfo[0])){
                role = roleInfo[1];
            }
        }
        return role;
    }

    @Override

    public void log(String printer, String functionName) throws IOException {
        // Enable write mode for log file
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/data/log.txt"));
        String item = "User: " + printer + ", Action: " + functionName + ", at time: "+ System.currentTimeMillis();
        writer.write("" + item + "\r\n");
        writer.close();
    }

    @Override
    public boolean AccessControlList(String operation) throws IOException {
        BufferedReader ACLfile = new BufferedReader(new FileReader("src/data/UserACL.txt"));
        String aclinfo;
        int aclfile_line=0;
        while ((aclinfo = ACLfile.readLine())!=null){
            String[] info = aclinfo.split(",");
            aclfile_line++;
            if(this.currentuser.getUsername().equals(info[1]))
            {
            
                for(int i = 2; i<info.length;i++)
                {
                    if(info[i].equals(operation))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
  
    }
    //display the access control list
    @Override
    public ArrayList<String> displayACL()throws IOException
    {
        BufferedReader ACLfile = new BufferedReader(new FileReader("src/data/UserACL.txt"));
        String aclinfo;
        int aclfile_line=0;
        ACLlist=new ArrayList<>();
        while ((aclinfo = ACLfile.readLine())!=null){
            String[] info = aclinfo.split(",");
            aclfile_line++;
            if(this.currentuser.getUsername().equals(info[1]))
            {
            
                for(int i = 1; i<info.length;i++)
                {     
                        ACLlist.add(info[i]);
                }
            }
        }
       
        return ACLlist;
        
    }
}