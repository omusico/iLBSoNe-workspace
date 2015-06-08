package com.ubiloc.simulate;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.model.GeoPoint;

import com.ubiloc.search.PoiClass;
import com.ubiloc.search.PoiObject;

/**
 * 模拟数据生成类，该类为单例
 * 
 * @author crazy
 * @Date 2015-6-6
 */
public class SimulatedDataManager {
	private static SimulatedDataManager mSimulatedDataManager;

	private SimulatedDataManager() {
	}

	public static SimulatedDataManager getInstance() {
		if (mSimulatedDataManager == null)
			mSimulatedDataManager = new SimulatedDataManager();
		return mSimulatedDataManager;
	}

	/**
	 * 获取消防栓的模拟数据
	 * 
	 * @return
	 */
	public List<PoiObject> getFireHydran() {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-1",
				new GeoPoint(-0.000270, 109.513593), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-2",
				new GeoPoint(-0.000406, 109.513594), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-3",
				new GeoPoint(-0.000410, 109.513863), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-4",
				new GeoPoint(-0.000517, 109.514208), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-5",
				new GeoPoint(-0.000722, 109.514330), 4, "4楼的消防栓"));
		return pois;
	}

	/**
	 * 获取会议室的模拟数据
	 * 
	 * @return
	 */
	public List<PoiObject> getBoadRoom() {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		pois.add(new PoiObject(PoiClass.POI_CLASS_BOARD_ROOM, "会议室",
				new GeoPoint(-0.000415, 109.514127), 4, "4楼的会议室"));
		return pois;
	}

	/**
	 * 获取电梯模拟数据
	 * 
	 * @return
	 */
	public List<PoiObject> getElevator() {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		pois.add(new PoiObject(PoiClass.POI_CLASS_ELEVATOR, "电梯", new GeoPoint(
				-0.000505, 109.513757), 4, "4楼电梯入口"));
		return pois;
	}

	/**
	 * 获取消防栓的模拟数据
	 * 
	 * @return
	 */
	public List<PoiObject> getExit() {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		pois.add(new PoiObject(PoiClass.POI_CLASS_EXIT, "消防栓-1", new GeoPoint(
				-0.000387, 109.513515), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_EXIT, "消防栓-2", new GeoPoint(
				-0.000406, 109.513594), 4, "4楼的消防栓"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_EXIT, "消防栓-3", new GeoPoint(
				-0.000410, 109.513863), 4, "4楼的消防栓"));
		return pois;
	}

	/**
	 * 获取厕所的模拟数据
	 * 
	 * @return
	 */
	public List<PoiObject> getWC() {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		pois.add(new PoiObject(PoiClass.POI_CLASS_WC, "男厕-1", new GeoPoint(
				-0.000464, 109.514206), 4, "4楼的男厕"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_WC, "男厕-2", new GeoPoint(
				-0.000327, 109.513511), 4, "4楼的男厕"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_WC, "女厕-1", new GeoPoint(
				-0.000475, 109.514232), 4, "4楼的女厕"));
		pois.add(new PoiObject(PoiClass.POI_CLASS_WC, "女厕-2", new GeoPoint(
				-0.000357, 109.513505), 4, "4楼的女厕"));
		return pois;
	}

	/**
	 * 获取模拟路径1
	 * 
	 * @return
	 */
	public List<GeoPoint> getRoute1() {
		List<GeoPoint> route1 = new ArrayList<GeoPoint>();
		// 1： -0.000715, 109.514151
		// 2：-0.000606 ,109.514149
		// 3：-0.000606, 109.514162
		// 4：-0.000531, 109.514215
		// 5：-0.000419, 109.514050
		// 6：-0.000420, 109.513585
		// 7：-0.000394 ,109.513585
		// 8：-0.000396,109.513541
		// route1.add(new GeoPoint(-0.000715, 109.514151));
		// route1.add(new GeoPoint(-0.000606, 109.514149));
		// route1.add(new GeoPoint(-0.000606, 109.514162));
		// route1.add(new GeoPoint(-0.000531, 109.514215));
		// route1.add(new GeoPoint(-0.000419, 109.514050));
		// route1.add(new GeoPoint(-0.000420, 109.513585));
		// route1.add(new GeoPoint(-0.000394, 109.513585));
		// route1.add(new GeoPoint(-0.000396, 109.513541));
		route1.addAll(calculateRoutesByEndPoints(-0.000715, 109.514151,
				-0.000606, 109.514149, 17));
		route1.addAll(calculateRoutesByEndPoints(-0.000606, 109.514149,
				-0.000606, 109.514162, 4));
		route1.addAll(calculateRoutesByEndPoints(-0.000606, 109.514162,
				-0.000531, 109.514215, 14));
		route1.addAll(calculateRoutesByEndPoints(-0.000531, 109.514215,
				-0.000419, 109.514050, 29));
		route1.addAll(calculateRoutesByEndPoints(-0.000419, 109.514050,
				-0.000420, 109.513585, 68));
		route1.addAll(calculateRoutesByEndPoints(-0.000420, 109.513585,
				-0.000394, 109.513585, 3));
		route1.addAll(calculateRoutesByEndPoints(-0.000394, 109.513585,
				-0.000396, 109.513541, 5));
		return route1;
	}

	/**
	 * 获取模拟路径2
	 * 
	 * @return
	 */
	public List<GeoPoint> getRoute2() {
		List<GeoPoint> route2 = new ArrayList<GeoPoint>();
		// 1： -0.000532,109.513759
		// 2： -0.000532,109.513794
		// 3： -0.000418,109.513792
		// 4： -0.000419,109.514050
		// 5： -0.000612,109.514323
		// 6： -0.000710,109.514323
		// 7： -0.000710,109.514299
		// 8： -0.000723,109.514299
		// 9： -0.000723,109.514224
		// route2.add(new GeoPoint(-0.000532, 109.513759));
		// route2.add(new GeoPoint(-0.000532, 109.513794));
		// route2.add(new GeoPoint(-0.000418, 109.513792));
		// route2.add(new GeoPoint(-0.000419, 109.514050));
		// route2.add(new GeoPoint(-0.000612, 109.514323));
		// route2.add(new GeoPoint(-0.000710, 109.514323));
		// route2.add(new GeoPoint(-0.000710, 109.514299));
		// route2.add(new GeoPoint(-0.000723, 109.514299));
		// route2.add(new GeoPoint(-0.000723, 109.514224));
		route2.addAll(calculateRoutesByEndPoints(-0.000532, 109.513759,
				-0.000532, 109.513794, 6));
		route2.addAll(calculateRoutesByEndPoints(-0.000532, 109.513794,
				-0.000418, 109.513792, 17));
		route2.addAll(calculateRoutesByEndPoints(-0.000418, 109.513792,
				-0.000419, 109.514050, 38));
		route2.addAll(calculateRoutesByEndPoints(-0.000419, 109.514050,
				-0.000612, 109.514323, 49));
		route2.addAll(calculateRoutesByEndPoints(-0.000612, 109.514323,
				-0.000710, 109.514323, 14));
		route2.addAll(calculateRoutesByEndPoints(-0.000710, 109.514323,
				-0.000710, 109.514299, 5));
		route2.addAll(calculateRoutesByEndPoints(-0.000710, 109.514299,
				-0.000723, 109.514299, 3));
		route2.addAll(calculateRoutesByEndPoints(-0.000723, 109.514299,
				-0.000723, 109.514224, 15));
		return route2;
	}

	/**
	 * 通过起点和终点的经纬度以及步数计算轨迹的所有点
	 * 
	 * @param s_lat
	 *            起点纬度
	 * @param s_lon
	 *            起点经度
	 * @param e_lat
	 *            终点纬度
	 * @param e_lon
	 *            终点经度
	 * @param step
	 *            步数
	 */
	public List<GeoPoint> calculateRoutesByEndPoints(double s_lat,
			double s_lon, double e_lat, double e_lon, int step) {
		List<GeoPoint> route = new ArrayList<GeoPoint>();
		double lat_step = (e_lat - s_lat) / step;
		double lon_step = (e_lon - s_lon) / step;
		for (int i = 0; i < step; i++) {
			double lat_noise = Math.random() / 600000;
			double lon_noise = Math.random() / 600000;
			route.add(new GeoPoint(s_lat + i * lat_step + lat_noise, s_lon + i
					* lon_step + lon_noise));
		}
		// route.add(new GeoPoint(e_lat, e_lon));
		return route;
	}
}
