package com.example.nishil09.socialmedia;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/**
 * Created by nishil09 on 2/5/16.
 */
@DynamoDBTable(tableName = "user")
public class userdata {
    private String number;
    private String fname;
    private String lname;
    private String email;

    @DynamoDBHashKey(attributeName = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @DynamoDBAttribute(attributeName = "fname")
    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    @DynamoDBAttribute(attributeName = "lname")
    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String ename) {
        this.email = email;
    }
}
