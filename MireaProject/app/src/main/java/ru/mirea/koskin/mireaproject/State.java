package ru.mirea.koskin.mireaproject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class State implements Serializable {
    private static int num_all = 1;
    private int num;
    private String text;

    public State(String text) {
        this.num = num_all++;
        this.text = text;
    }
    public State(int num, String text) {
        this.num = num;
        this.text = text;
    }

    public int getNum() {
        return num;
    }

    private void setNum(String name) {
        this.text = name;
    }

    public String getText() {
        return this.text;
    }

    public static Integer getNum_All(){
        return num_all;
    }
    public static void setNum_All(int _num_all){
        num_all = _num_all;
    }

    public void setText(String text) {
        this.text = text;
    }

    /*private void writeObject(ObjectOutputStream oos)
            throws IOException
    {
        oos.defaultWriteObject();
        oos.writeObject(new Integer(num_all));
        oos.writeInt(num);
        oos.writeObject(text);
    }
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        num_all = (String) stream.readObject();
        id = stream.readInt();
        DOB = (String) stream.readObject();
    }*/

}