package com.ubiloc.tools;

import service.ConnectAndSendService;

import com.ubiloc.model.MORangeRequest;
import com.ubiloc.ubilocmap.UbilocMapActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class MORangeRequestTools {

	/**
	 * 范围查询接口
	 * @param context
	 * @param userid
	 * @param range
	 */
	public void RangRequest(Context context,String userid,String range) {
		MORangeRequest mRequest=new MORangeRequest(userid, range);
		Intent mintent=new Intent(context,ConnectAndSendService.class);
		Bundle bundle=new Bundle();
		bundle.putString(ConstConfig.MSG_TYPE, ConstConfig.Range_MO_Query);
		bundle.putSerializable("RangeQuery", mRequest);
		mintent.putExtras(bundle);
		context.startService(mintent);
	}
}
