/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.rmlproject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author xiaodiezi
 */
public class Printer implements Serializable{
    private String name;
    private List<PrinterFile> printfile;
    private int fileid;

    public Printer(String name, List<PrinterFile> printfile, int fileid) {
        this.name = name;
        this.printfile = printfile;
        this.fileid = fileid;
    }

    public Printer(String name) {
        this.name = name;
        this.printfile = new LinkedList<>();
        this.fileid=0;
    }
    
    public String getName() {
        return name;
    }

    public List<PrinterFile> getPrintfile() {
        return printfile;
    }

    public int getFileid() {
        return fileid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrintfile(List<PrinterFile> printfile) {
        this.printfile = printfile;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }
    public void addfile()
    {
        this.fileid++;
    }
    
}
