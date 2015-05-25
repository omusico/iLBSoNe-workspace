package com.verticalmenu;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.capricorn.R;

public class VerticalMenu extends LinearLayout {
	private View root_view;
	private View vertical_menu_control;
	private ImageView vertical_menu_control_img;
	private LinearLayout vertical_menu_items_container;
	private boolean isExpanded = false;
	/**
	 * 打开动画
	 */
	private Animation expandingAnimation;
	/**
	 * 关闭动画
	 */
	private Animation shrinkingAnimation;
	private List<View> items;
	private OnVerticalMenuItemClickListener onItemClicklistener;

	public VerticalMenu(Context context) {
		super(context);
	}

	@SuppressLint("InflateParams")
	public VerticalMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = LayoutInflater.from(context);

		root_view = inflater.inflate(R.layout.vertical_menu, this);
		vertical_menu_control = root_view
				.findViewById(R.id.vertical_menu_control);
		vertical_menu_control_img = (ImageView) root_view
				.findViewById(R.id.vertical_menu_control_img);
		vertical_menu_items_container = (LinearLayout) root_view
				.findViewById(R.id.vertical_menu_items_container);

		vertical_menu_items_container.setVisibility(View.GONE);

		vertical_menu_control.setOnClickListener(new MyListener());

		expandingAnimation = getExpandingAnimation();
		shrinkingAnimation = getShrinkingAnimation();
		items = new ArrayList<View>();
	}

	/**
	 * 设置menu 控制的背景
	 * 
	 * @param rid
	 * @return
	 */
	public VerticalMenu setControlBackground(int rid) {
		vertical_menu_control_img.setBackgroundResource(rid);
		return this;
	}

	/**
	 * 增加一个子项
	 * 
	 * @param item
	 * @return
	 */
	public VerticalMenu addMenuItem(View item) {
		vertical_menu_items_container.addView(item);
		items.add(item);
		refreshItemCLickListener();
		return this;
	}

	/**
	 * @return 扩展动画
	 */
	private Animation getExpandingAnimation() {
		Animation expandingAnimation = new ScaleAnimation(1, 1, 0, 1.1f, 0.5f,
				0.5f);
		expandingAnimation.setDuration(300);
		return expandingAnimation;
	}

	/**
	 * @return 关闭动画
	 */
	private Animation getShrinkingAnimation() {
		Animation shrinkingAnimation = new ScaleAnimation(1, 1, 1.1f, 0, 0.5f,
				0.5f);
		shrinkingAnimation.setDuration(300);
		return shrinkingAnimation;
	}

	/**
	 * 为menu子项添加点击事件
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(
			final OnVerticalMenuItemClickListener listener) {
		this.onItemClicklistener = listener;
		if (listener != null) {
			for (int i = 0; i < items.size(); i++) {
				final int j = i;
				items.get(i).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						listener.OnItemClick(view, j);
					}
				});
			}
		}
	}

	/**
	 * 当增加子项或移除子项时需要刷新
	 */
	private void refreshItemCLickListener() {
		if (onItemClicklistener != null) {
			for (int i = 0; i < items.size(); i++) {
				final int j = i;
				items.get(i).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						onItemClicklistener.OnItemClick(view, j);
					}
				});
			}
		}
	}

	/**
	 * 内部类，响应点击事件
	 * 
	 * @author crazy
	 * @Date 2015-5-24
	 */
	class MyListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (isExpanded) {
				vertical_menu_items_container
						.startAnimation(shrinkingAnimation);
				shrinkingAnimation
						.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationEnd(Animation arg0) {
								vertical_menu_items_container
										.setVisibility(View.GONE);
							}

							@Override
							public void onAnimationRepeat(Animation arg0) {

							}

							@Override
							public void onAnimationStart(Animation arg0) {

							}
						});
			} else {

				vertical_menu_items_container
						.startAnimation(expandingAnimation);
				vertical_menu_items_container.setVisibility(View.VISIBLE);
			}
			isExpanded = !isExpanded;
		}

	}

}
