package com.ubiloc.search;

import com.donal.wechat.R;

/**
 * POI工具类，该类不可进行初始化操作
 * 
 * @author crazy
 * @Date 2015-6-5
 */
public class PoiTools {

	private PoiTools() {
	}

	public static int getDrawableByClass(int poi_class) {
		int drawable = R.drawable.draw_bitmap_default;
		switch (poi_class) {
		case PoiClass.POI_CLASS_AIR_VENT: {// 通风口,没找到合适的图标
			// drawable = R.drawable.dra
			break;

		}
		case PoiClass.POI_CLASS_BOARD_ROOM: {// 会议室
			drawable = R.drawable.draw_bitmap_meetingroom;
			break;

		}
		case PoiClass.POI_CLASS_ELEVATOR: {// 电梯
			drawable = R.drawable.draw_bitmap_elevator;
			break;

		}
		case PoiClass.POI_CLASS_EXIT: {// 安全出口
			drawable = R.drawable.draw_bitmap_safe_entry;
			break;

		}
		case PoiClass.POI_CLASS_FIRE_HYDRAN: {// 消防栓

			drawable = R.drawable.draw_bitmap_fire_hydrant;
			break;

		}
		case PoiClass.POI_CLASS_LAB: {// 实验室
			drawable = R.drawable.draw_bitmap_lab;
			break;

		}
		case PoiClass.POI_CLASS_STAIR: {
			drawable = R.drawable.draw_bitmap_stair;
			break;

		}
		case PoiClass.POI_CLASS_WC: {
			drawable = R.drawable.draw_bitmap_toilit;
			break;
		}
		default:
			drawable = R.drawable.draw_bitmap_default;
			break;
		}
		return drawable;
	}

}
