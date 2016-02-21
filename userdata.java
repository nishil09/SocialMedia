package com.example.nishil09.socialmedia;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/**
 * Created by nishil09 on 2/5/16.
 */
@DynamoDBTable(tableName = "user")
public class userdata {
    private String number;
    private String name;
    private String email;

    @DynamoDBHashKey(attributeName = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String fname) {
        this.name = fname;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
