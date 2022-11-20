package com.mycompany.rmlproject;

import java.io.*;

public class Xor {
    public static String  xor(String content, Integer key){
        char[] chars = content.toCharArray();
        for(int i=0; i<chars.length; i++){
            chars[i] = (char)  (chars[i]^key) ;
        }
        return new String(chars);
    }

    public static String passwordEncryption(String value, Integer key) throws IOException {
        //password encryption
        String encryptPsw = xor(value,key);
        return encryptPsw;
    }
    public static String passwordDecryption(String value,Integer key) throws IOException {
        String decryptPsw = xor(value,key);
        return decryptPsw;
    }

    public static void encryptAll() throws IOException {
        BufferedReader userInfo = new BufferedReader(new FileReader("src/data/UserInfo.txt"));
        BufferedWriter userInfoUpdate = new BufferedWriter(new FileWriter("src/data/EncryptUserInfo.txt"));
        String usr;
        int key = 1000;
        while ((usr = userInfo.readLine())!=null){
            String[] info = usr.split(",");
            String encryptPsw = passwordEncryption(info[2],key);
            usr = ""+info[0]+","+ info[1]+","+encryptPsw+"\r\n";
            userInfoUpdate.write(usr);

        }
        userInfoUpdate.close();
    }
}
