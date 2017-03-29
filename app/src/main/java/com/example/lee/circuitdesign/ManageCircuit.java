package com.example.lee.circuitdesign;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Lee on 2016-12-11.
 */


//회로를 저장하고 로드하는 클래스

public class ManageCircuit {
    private FileOutputStream fos;
    private  ObjectOutputStream out;
    private FileInputStream fis;
    private ObjectInputStream in;
    private HashMap<String,Object> objects;
    private  File path;
    File tempFile;
    private String fileName;

    private static ManageCircuit instance;

    public static ManageCircuit getInstance(){

        if(instance==null) {


            instance = new ManageCircuit();


        }return instance;
    }
    public ManageCircuit(){



        objects=new HashMap<>();
        path=new File(Environment.getExternalStorageDirectory(),"CirCuitDesignData");
        if(path.exists()!=true){
            path.mkdirs();
            path.setReadOnly();
        }


    }

    public void store(String filename) {



        this.fileName=filename;
        System.out.println(filename+".ser 생성");
        tempFile=new File(path,filename+".ser");

        this.store();

    }
    public void store(){

        try {



            if(tempFile.exists())
                tempFile.delete();

            fos = new FileOutputStream(tempFile);
            out = new ObjectOutputStream(fos);


            out.writeObject(new FileInfo(this.fileName));
            out.writeObject(MyCanvas.components);
            out.writeObject(MyCanvas.wires);

            out.close();
            fos.close();


        }catch (Exception e){
            e.printStackTrace();

        }finally {



        }
    }


    public void load(String filename){
        Object o;
        tempFile=new File(path,filename+".ser");
        this.fileName=filename;
        try {
           fis = new FileInputStream(tempFile);
             in = new ObjectInputStream(fis);


            while((o=in.readObject())!=null)
            {
                objects.put(o.getClass().getName(),o);

            }





            in.close();
            fis.close();

        }catch (Exception e){



        }
    }

    public void delete(String filename){
        tempFile=new File(path,filename+".ser");
        tempFile.delete();
    }



    public ArrayList getFileList(){

        File[] files=path.listFiles();
        ArrayList<FileInfo> list=new ArrayList<FileInfo>();
        for(File f:files){

            if(f.getName().endsWith(".ser")){  //.ser확장자인 파일 찾기

                try { //.ser 확장자 찾으면 파일읽어서 날짜와 이름을 가진 FileInfo클래스(인스턴스) 가져오기

                    fis = new FileInputStream(f);
                    in = new ObjectInputStream(fis);

                     list.add((FileInfo)in.readObject());

                    in.close();
                    fis.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e){

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }



        }

        return list;

    }


    public Object getObject(String className){

        return objects.get(className);
    }

}

class FileInfo implements Serializable{
    private String date;
    private String fileName;



    FileInfo(){
        this.date=getDateFormat(Calendar.getInstance());
        this.fileName="No name";

    }
    FileInfo(String fileName){
        this.date=getDateFormat(Calendar.getInstance());

        this.fileName=fileName;
    }

    public String getDate(){
        return this.date;
    }
    public String getFileName(){
        return this.fileName;
    }

    public String getDateFormat(Calendar date){
        return date.get(Calendar.YEAR)+"/"+(date.get(Calendar.MONTH)+1)+"/"
                +date.get(Calendar.DATE)+"/ "+date.get(Calendar.HOUR_OF_DAY)+":"
                +date.get(Calendar.MINUTE);
    }

}
