package com.example.kalkulator;

public class ProsesHitung {
    private String ID, Variabel1, Variabel2, Operator, Result;

    public ProsesHitung(String ID, String Variabel1, String Variabel2, String Operator, String Result){
        this.ID = ID;
        this.Variabel1 = Variabel1;
        this.Variabel2 = Variabel2;
        this.Operator = Operator;
        this.Result = Result;
    }
    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public String getVariabel1(){
        return Variabel1;
    }
    public void setVariabel1(String Variabel1){
        this.Variabel1 = Variabel1;
    }
    public String getVariabel2(){
        return Variabel2;
    }
    public void setVariabel2(String Variabel2){
        this.Variabel2 = Variabel2;
    }
    public String getOperator(){
        return Operator;
    }
    public void setOperator(String Operator){
        this.Operator = Operator;
    }
    public String getResult(){
        return Result;
    }
    public void setResult(String Result){
        this.Result = Result;
    }
}
