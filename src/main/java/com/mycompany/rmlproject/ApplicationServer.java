/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rmlproject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *
 * @author xiaodiezi
 */
public class ApplicationServer {
    public static void main(String[] args) throws RemoteException{
        Registry registry=LocateRegistry.createRegistry(2802);
        registry.rebind("printer", new PrinterServant());
    }
    
}
