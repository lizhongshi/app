package com.dly.app.commons.sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.dly.app.commons.baes.Result;
import com.dly.app.commons.util.Util;
@Component
public class ShortMessage {
	 @Resource
	public  SmsBean smsBean;
	public Map<String,String>  sendSms(String phoneNmb) { 
	
		String info =Util.randomNumber();
		System.out.println(info);
		
		   //可自助调整超时时间
     System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
     System.setProperty("sun.net.client.defaultReadTimeout", "10000");
System.out.println("+++++++++"+smsBean);
     //初始化acsClient,暂不支持region化
     IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsBean.getAccessKeyId(), smsBean.getAccessKeySecret());
     try {
			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", smsBean.getProduct(),  smsBean.getDomain()) ;
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     IAcsClient acsClient = new DefaultAcsClient(profile);

     //组装请求对象-具体描述见控制台-文档部分内容
     SendSmsRequest request = new SendSmsRequest();
     //必填:待发送手机号
     request.setPhoneNumbers(phoneNmb);
     //必填:短信签名-可在短信控制台中找到
     request.setSignName("旅游日记");
     //必填:短信模板-可在短信控制台中找到
     request.setTemplateCode(smsBean.getTemplateCode());
     //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
     ShortMessageBean sm=new ShortMessageBean();
     sm.setCode(info);
//     System.out.println(JSONObject.toJSONString(sm));
     request.setTemplateParam(JSONObject.toJSONString(sm));

     //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
     //request.setSmsUpExtendCode("90997");

     //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
     request.setOutId("yourOutId");

     //hint 此处可能会抛出异常，注意catch
     SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			
		} catch (ServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> map=new HashMap<String,String>();
		map.put("code", sendSmsResponse.getCode());
		map.put("message", sendSmsResponse.getMessage());
		map.put("requestId", sendSmsResponse.getRequestId());
		map.put("bizId", sendSmsResponse.getBizId());
		map.put("info", info);
		//System.out.println(map.toString());
		return map;
	}
	 public  QuerySendDetailsResponse querySendDetails(String bizId) {

	        //可自助调整超时时间
	        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
	        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

	        //初始化acsClient,暂不支持region化
	        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsBean.getAccessKeyId(),  smsBean.getAccessKeySecret());
	        try {
				DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou",  smsBean.getProduct(),  smsBean.getDomain());
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        IAcsClient acsClient = new DefaultAcsClient(profile);

	        //组装请求对象
	        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
	        //必填-号码
	        request.setPhoneNumber("15000000000");
	        //可选-流水号
	        request.setBizId(bizId);
	        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
	        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
	        request.setSendDate(ft.format(new Date()));
	        //必填-页大小
	        request.setPageSize(10L);
	        //必填-当前页码从1开始计数
	        request.setCurrentPage(1L);

	        //hint 此处可能会抛出异常，注意catch
	        QuerySendDetailsResponse querySendDetailsResponse=null;
			try {
				querySendDetailsResponse = acsClient.getAcsResponse(request);
			} catch (ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        return querySendDetailsResponse;
	    }


}
