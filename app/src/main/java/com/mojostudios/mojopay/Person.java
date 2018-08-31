package com.mojostudios.mojopay;

/**
 * Created by Harsh Gupta on 19-Mar-18.
 */


/**
 * Created by Harsh Gupta on 27-Feb-18.
 */

public class Person {

    //private variables
    private int person_id;
    private String person_name;
    private int credit; //money owes to me
    private int debit; //money i have to give
    private int total;
    private String image;
    private String cDescription;
    private String dDescription;
    // Empty constructor
    public Person() {
    }

    // constructor
    public Person(int id, String name, int credit, int debit, int total,String image) {
        this.person_id = id;
        this.person_name = name;
        this.credit = credit;
        this.debit = debit;
        this.total = total;
        this.image=image;
    }

    // constructor
    public Person(String name, int credit, int debit,int total,String image) {
        this.person_name = name;
        this.credit = credit;
        this.debit = debit;
        this.total = total;
        this.image=image;
    }

    // getting ID
    public int getID() {
        return this.person_id;
    }

    // setting id
    public void setID(int id) {
        this.person_id = id;
    }

    // getting name
    public String getName() {
        return this.person_name;
    }

    // setting name
    public void setName(String name) {
        this.person_name = name;
    }

    public int getCredit(){
        return this.credit;
    }

    // setting id
    public void setCredit(int credit){
        this.credit= credit;
    }

    public int getDebit(){
        return this.debit;
    }

    // setting id
    public void setDebit(int debit){
        this.debit= debit;
    }

    public int getTotal(){
        return this.total;
    }

    // setting id
    public void setTotal(int total){
        this.total= total;
    }
    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getcDescription() {
        return cDescription;
    }

    public void setcDescription(String cDescription) {
        this.cDescription = cDescription;
    }

    public String getdDescription() {
        return dDescription;
    }

    public void setdDescription(String dDescription) {
        this.dDescription = dDescription;
    }
}

