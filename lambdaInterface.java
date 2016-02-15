package com.example.nishil09.socialmedia;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
/**
 * Created by nishil09 on 2/7/16.
 */

public interface lambdaInterface{
@LambdaFunction
    String sendOtp(otpLambda obj);


}





