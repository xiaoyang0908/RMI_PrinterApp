/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.rmlproject;

import java.io.IOException;
import static java.lang.System.in;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author xiaodiezi
 */

public class Client {
    static String curUser;
    public static void printMenu(PrinterService service) throws IOException{
        String role = service.rolePermission();
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("Hi! "+curUser);
        System.out.print("you are the "+ role);

        System.out.println(" ");
        //the menu with available options
        System.out.println("*****************Printer****************");
        System.out.println("1. Print filename on the specified printer");
        System.out.println("2. List the print queue for a given printer");
        System.out.println("3. Move job to the top of the queue");
//        System.out.println("4. Start the print server");
        System.out.println("4. Stop the print server");
        System.out.println("5. Restart the print server");
        System.out.println("6. Print status of printer");
        System.out.println("7. Print the value of a user configuration parameter");
        System.out.println("8. Set a user parameter to value");
        System.out.println("9. Start the print server");
        System.out.println("10. Back to Login Menu");
        System.out.println("11. Exit");
        System.out.print("Enter a number: ");
    }
    public static Boolean loginMenu(PrinterService service) throws RemoteException, IOException {
        Scanner input = new Scanner(System.in);
        while(true)
        {
            System.out.println("");
            System.out.println("*******************************");
            System.out.println("*     Login Print Server      *");
            System.out.println("*******************************");
            System.out.println("Username: ");
            String username = input.next();
            System.out.println("Password: ");
            String password = input.next();
            String encrptPsw = Xor.passwordEncryption(password,1000);
            //System.out.println("密码"+ encrptPsw);
            //System.out.println(Xor.passwordDecryption(encrptPsw,1000));
            //boolean test = service.PasswordAuthentication(username,password);
            if(service.PasswordAuthentication(username,password))
            {
//                service.start();
                //User currentuser = new User(username,HashAndSalt.encryption(password),true);
                curUser = username;
                System.out.println("login successful");
                return true;
            }
            System.out.println("wrong username or password, please try again ");
          
        }
    } 
    public static void main(String[] args) throws NotBoundException, MalformedURLException, RemoteException, IOException {
        Xor.encryptAll();
        PrinterService service = (PrinterService) Naming.lookup("rmi://localhost:2802/printer");
        //System.out.println("---" + service.echo("hey server"+" "+ service.getClass().getName()));
        Scanner input = new Scanner(System.in);
        String select = "";
        String result="";
        String printer="";
        String parameter;
        Boolean loginsuccessgul = loginMenu(service);
        Boolean serviceStart = false;
        Boolean hasOperation;
        ArrayList<String> Rolelist;
        while(loginsuccessgul)
        {
            printMenu(service);
            select = input.next();
            hasOperation=false;
            Rolelist=service.checkRolePermission(curUser);
            switch(select){
            case "1" :
            if(Rolelist.contains("print"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println("prints file filename on the specified printer");
                System.out.println("");
                System.out.print("Please enter the printer name: ");
                printer = input.next();
                System.out.println("");
                System.out.print("Please enter the file name: ");
                String filename = input.next();
                System.out.println("");
                service.log(printer,"print");
                result = service.print(filename, printer);
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to print");
            }
            break;
            case "2" :
            if(Rolelist.contains("queue"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println("list the print queue for a given printer");
                System.out.println("");
                System.out.print("Please enter the printer name: ");
                printer = input.next();
                System.out.println("");
                service.log(printer,"queue");
                result=service.queue(printer);
                System.out.println("FileId   FilesName");
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to see the queue");
            }
            break;
            case "3" :
            if(Rolelist.contains("topQueue"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println("list the print queue for a given printer");
                System.out.println("");
                System.out.print("Please enter the printer name: ");
                printer = input.next();
                System.out.println("");
                System.out.print("Please enter the job id: ");
                String jobid = input.next();
                System.out.println("");
                service.log(printer,"topQueue");
                result=service.topQueue(printer, Integer.valueOf(jobid));
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to topQueue");
            }
            break;
            case "4" :
            if(Rolelist.contains("stop"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                service.log(printer,"stop");
                result=service.stop();
                serviceStart=false;
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to stop the server");
            }
            //loginMenu(service);
            break;
            case "5" :
                
            if(Rolelist.contains("restart"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println("restart the printer server");
                service.log(printer,"restart");
                result=service.restart();
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to restart the server");
            }
            //loginMenu(service);
            break;
            case "6" :
            if(Rolelist.contains("status"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println("Print status of printer");
                System.out.println(" ");
                System.out.print("Please enter the printer name: ");
                printer = input.next();
                service.log(printer,"status");
                result=service.status(printer);
                System.out.println(result);
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to see the status of the printer");
            }
            break;
            case "7" :
            if(Rolelist.contains("readConfig"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println(" ");
                System.out.print("Print the value of a user configuration parameter");
                System.out.println(" ");
                System.out.print("Please enter a user configuration parameter(username/password): ");
                parameter = input.next();
                service.log(printer,"readConfig");
                result=service.readConfig(parameter);
                System.out.println(parameter + " : " + result );
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to read config");
            }
            break;
            case "8" :
            if(Rolelist.contains("setConfig"))
            {
                hasOperation = true;
            }
            if((serviceStart==true)&&(hasOperation==true))
            {
                System.out.println(" ");
                System.out.print("Set a user parameter to value");
                System.out.println(" ");
                System.out.print("Please enter a user configuration parameter(username/password): ");
                parameter = input.next();
                System.out.print("Please enter the new value of this parameter: ");
                String value = input.next();
                service.log(printer,"setConfig");
                result=service.setConfig(parameter, value);
                System.out.println(result );
            }
            else if(serviceStart==false)
            {
                System.out.println(" ");
                System.out.println("The Printer Server is close ");
                System.out.println("Please open the Printer Server first !");
            }
            else
            {
                System.out.println(" ");
                System.out.println("This user does not have permission to set config");
            }
            break;
            case "9":
            if(Rolelist.contains("start"))
            {
                hasOperation = true;
            }
            if(hasOperation==true)
            {
                serviceStart=true;
                System.out.println("Start Printer Server Successful!");
            }
            else
            {
                System.out.println("This user does not have permission to start the printer server");
            }
            break;
            case "10" :
            loginMenu(service);
            break;
            case "11" :
            System.out.println("Exiting......");
            service.log(printer,"exit");
            System.exit(0);
            break;
            default : System.out.println("The number entered does not exist, please select again\n");
        }
        }
    }
}
