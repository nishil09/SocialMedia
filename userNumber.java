package com.example.nishil09.socialmedia;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/**
 * Created by nishil09 on 2/7/16.
 */
@DynamoDBTable(tableName = "userotp")
public class userNumber {

    String number;
    String code;
    String timestamp;

    @DynamoDBHashKey(attributeName = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    @DynamoDBAttribute(attributeName = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
