package com.lz.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class Message {

    private static final Logger logger= LoggerFactory.getLogger(Message.class);

    public static void messagePost(String mobile,String password){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4GEjAVNvBMxQh9NvBehw", "k1YZXc5TnSMxvc9chbtqDzoYXiNPWt");
        IAcsClient client = new DefaultAcsClient(profile);
        logger.info(mobile+"\t"+password);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", "校园帮帮");
        request.putQueryParameter("TemplateCode", "SMS_191801536");
        request.putQueryParameter("TemplateParam", "{code:" + password+ "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            logger.info(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
