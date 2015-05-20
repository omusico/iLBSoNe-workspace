package org.mapsforge.android.bitmapUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.util.Log;

public final class BitMapUtil {

	private static final Size ZERO_SIZE = new Size(0, 0);
	private static final Options OPTIONS_GET_SIZE = new Options();
	private static final Options OPTIONS_DECODE = new Options();
	private static final byte[] LOCKED = new byte[0];

	// 姝ゅ璞＄敤鏉ヤ繚鎸丅itmap鐨勫洖鏀堕『搴�淇濊瘉鏈�悗浣跨敤鐨勫浘鐗囪鍥炴敹
	private static final LinkedList CACHE_ENTRIES = new LinkedList();

	// 绾跨▼璇锋眰鍒涘缓鍥剧墖鐨勯槦鍒�
	private static final Queue TASK_QUEUE = new LinkedList();

	// 淇濆瓨闃熷垪涓鍦ㄥ鐞嗙殑鍥剧墖鐨刱ey,鏈夋晥闃叉閲嶅娣诲姞鍒拌姹傚垱寤洪槦鍒�

	private static final Set TASK_QUEUE_INDEX = new HashSet();

	// 缂撳瓨Bitmap
	private static final Map IMG_CACHE_INDEX = new HashMap(); // 閫氳繃鍥剧墖璺緞,鍥剧墖澶у皬

	private static int CACHE_SIZE = 20; // 缂撳瓨鍥剧墖鏁伴噺

	static {
		OPTIONS_GET_SIZE.inJustDecodeBounds = true;
		// 鍒濆鍖栧垱寤哄浘鐗囩嚎绋�骞剁瓑寰呭鐞�
		new Thread() {
			{
				setDaemon(true);
			}

			public void run() {
				while (true) {
					synchronized (TASK_QUEUE) {
						if (TASK_QUEUE.isEmpty()) {
							try {
								TASK_QUEUE.wait();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					QueueEntry entry = (QueueEntry) TASK_QUEUE.poll();
					String key = createKey(entry.path, entry.width,
							entry.height);
					TASK_QUEUE_INDEX.remove(key);
					createBitmap(entry.path, entry.width, entry.height);
				}
			}
		}.start();

	}

	public static Bitmap getBitmap(String path, int width, int height) {
		if (path == null) {
			return null;
		}
		Bitmap bitMap = null;
		try {
			if (CACHE_ENTRIES.size() >= CACHE_SIZE) {
				destoryLast();
			}
			bitMap = useBitmap(path, width, height);
			if (bitMap != null && !bitMap.isRecycled()) {
				return bitMap;
			}
			bitMap = createBitmap(path, width, height);
			String key = createKey(path, width, height);
			synchronized (LOCKED) {
				IMG_CACHE_INDEX.put(key, bitMap);
				CACHE_ENTRIES.addFirst(key);
			}
		} catch (OutOfMemoryError err) {
			destoryLast();
			System.out.println(CACHE_SIZE);
			return createBitmap(path, width, height);
		}
		return bitMap;
	}

	public static Size getBitMapSize(String path) {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				BitmapFactory.decodeStream(in, null, OPTIONS_GET_SIZE);
				return new Size(OPTIONS_GET_SIZE.outWidth,
						OPTIONS_GET_SIZE.outHeight);
			} catch (FileNotFoundException e) {
				return ZERO_SIZE;
			} finally {
				closeInputStream(in);
			}
		}
		return ZERO_SIZE;
	}

	// ------------------------------------------------------------------
	// private Methods
	// 灏嗗浘鐗囧姞鍏ラ槦鍒楀ご
	private static Bitmap useBitmap(String path, int width, int height) {
		Bitmap bitMap = null;
		String key = createKey(path, width, height);
		synchronized (LOCKED) {
			bitMap = (Bitmap) IMG_CACHE_INDEX.get(key);
			if (null != bitMap) {
				if (CACHE_ENTRIES.remove(key)) {
					CACHE_ENTRIES.addFirst(key);
				}
			}
		}
		return bitMap;
	}

	// 鍥炴敹鏈�悗涓�紶鍥剧墖
	private static void destoryLast() {
		synchronized (LOCKED) {
			String key = (String) CACHE_ENTRIES.removeLast();
			if (key.length() > 0) {
				Bitmap bitMap = (Bitmap) IMG_CACHE_INDEX.remove(key);
				if (bitMap != null && !bitMap.isRecycled()) {
					bitMap.recycle();
					bitMap = null;
				}
			}
		}
	}

	// 鍒涘缓閿�
	private static String createKey(String path, int width, int height) {
		if (null == path || path.length() == 0) {
			return "";
		}
		return path + "_" + width + "_" + height;
	}

	// 閫氳繃鍥剧墖璺緞,瀹藉害楂樺害鍒涘缓涓�釜Bitmap瀵硅薄
	private static Bitmap createBitmap(String path, int width, int height) {
		File file = new File(path);
		if (file.exists()) {
			InputStream in = null;
			try {
				in = new FileInputStream(file);
				Size size = getBitMapSize(path);
				if (size.equals(ZERO_SIZE)) {
					return null;
				}
				int scale = 1;
				int a = size.getWidth() / width;
				int b = size.getHeight() / height;
				scale = Math.max(a, b);
				synchronized (OPTIONS_DECODE) {
					OPTIONS_DECODE.inSampleSize = scale;
					Bitmap bitMap = BitmapFactory.decodeStream(in, null,
							OPTIONS_DECODE);
					return bitMap;
				}
			} catch (FileNotFoundException e) {
				Log.v("BitMapUtil", "createBitmap==" + e.toString());
			} finally {
				closeInputStream(in);
			}
		}
		return null;
	}

	// 鍏抽棴杈撳叆娴�
	private static void closeInputStream(InputStream in) {
		if (null != in) {
			try {
				in.close();
			} catch (IOException e) {
				Log.v("BitMapUtil", "closeInputStream==" + e.toString());
			}
		}
	}

	// 鍥剧墖澶у皬
	static class Size {
		private int width, height;

		Size(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}
	}

	// 闃熷垪缂撳瓨鍙傛暟瀵硅薄
	static class QueueEntry {
		public String path;
		public int width;
		public int height;
	}

	public static Bitmap getVideoThumbnail(String videoPath, int width,
			int height, int kind) {
		Bitmap bitmap = null;
		// 鑾峰彇瑙嗛鐨勭缉鐣ュ浘
		bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
		System.out.println("w" + bitmap.getWidth());
		System.out.println("h" + bitmap.getHeight());
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

}
