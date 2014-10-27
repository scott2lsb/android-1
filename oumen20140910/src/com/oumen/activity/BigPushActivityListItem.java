package com.oumen.activity;

import java.text.ParseException;

import widget.viewpager.CirclePageIndicator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oumen.R;
import com.oumen.activity.detail.HeaderAdapter;
import com.oumen.activity.detail.HuodongDetailHeaderProvider;
import com.oumen.activity.message.DetailActivityMessage;
import com.oumen.android.App;
import com.oumen.message.ActivityMessage;
import com.oumen.widget.file.ImageData;

public class BigPushActivityListItem extends LinearLayout implements PushActivityListActivity.ItemData {
	private ViewPager headerViewPager;
	private CirclePageIndicator indicator;
	private HeaderAdapter headerAdapter;
	private TextView title;
	
	protected ActivityMessage data;
	
	private int width = App.INT_UNSET;
	private Resources res ;
	
	public BigPushActivityListItem(Context context) {
		this(context, null, 0);
	}

	public BigPushActivityListItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BigPushActivityListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.push_activity_list_item_big, this, true);

		res = context.getResources();
		width = res.getDisplayMetrics().widthPixels;
		
		headerAdapter = new HeaderAdapter();
		headerAdapter.setPadding(R.dimen.padding_large);
		headerViewPager = (ViewPager) findViewById(R.id.header_viewpager);
		headerViewPager.setAdapter(headerAdapter);
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(headerViewPager);
		indicator.setFillColor(getResources().getColor(R.color.default_bg));
		
		title = (TextView) findViewById(R.id.big_item_title);
		
		setViewDefaultHeight(width, R.dimen.padding_large);
	}
	
	/**
	 * 设置viewpager高度
	 * @param width
	 * @param padding
	 */
	public void setViewDefaultHeight(int width, int padding) {
		ViewGroup.LayoutParams params = headerViewPager.getLayoutParams();
		params.width = width - res.getDimensionPixelSize(padding) * 2;
		params.height = params.width * 18 / 29;//Banner宽高比为29:18
		headerViewPager.setLayoutParams(params);
		headerAdapter.setPadding(padding);
	}
	
	public void setViewHeight() {
		ViewGroup.LayoutParams params = headerViewPager.getLayoutParams();
		params.width = width - res.getDimensionPixelSize(R.dimen.default_big_gap) * 2;
		params.height = params.width * 18 / 29;//Banner宽高比为29:18
		headerViewPager.setLayoutParams(params);
		headerAdapter.setPadding(R.dimen.default_big_gap);
	}
	
	public void setImageHasClickListener(boolean hasClick, OnClickListener listener) {
		headerAdapter.setImageViewHasClickListener(hasClick, listener);
	}
	
	public void update(HuodongDetailHeaderProvider provider) {
		if (provider instanceof DetailActivityMessage) {
			DetailActivityMessage bean = (DetailActivityMessage) provider;
			data = new ActivityMessage();
			data.setId(bean.getId());
			data.setTitle(provider.getHuodongTitle());
			data.setAddress(provider.getHuodongAddress());
			data.setOwnerName(provider.getHuodongSenderName());
			data.setOwnerId(bean.getSenderUid());
			data.setOwnerPhotoUrl(provider.getHuodongSenderPhoto());
			//TODO 多少人看过
			try {
				data.setTimestamp(App.YYYY_MM_DD_FORMAT.parse(bean.getStartTime()));
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
		}
		else {
			data = (ActivityMessage) provider;
		}
		
		title.setText(provider.getHuodongTitle());
		
		int size = provider.getHuodongPics().size();
		if (size > 0) {
			if (size <= 1) {
				indicator.setVisibility(View.GONE);
			}
			else {
				indicator.setVisibility(View.VISIBLE);
			}
			for (int i = 0; i < size; i++) {
				ImageData data = new ImageData(provider.getHuodongPics().get(i));
				headerAdapter.datas.add(data);
			}
			headerAdapter.notifyDataSetChanged();
			
		}
	}

	@Override
	public ActivityMessage getActivityMessage() {
		return data;
	}
}
