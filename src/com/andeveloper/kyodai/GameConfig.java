package com.andeveloper.kyodai;

import android.content.Context;
import android.content.SharedPreferences;

public class GameConfig {
	private static final String CONFIG = "config";
	private static final String P_RECORD = "p_record";
	private static final String P_LEVEL = "p_level";
	private static final String P_MUSIC = "p_music";
	private static final String P_SOUND = "p_sound";
	private static final String P_CRAZY_MODE_RECORD = "p_crazy_mode_record";
	public static int MODE_NORMAL = 1;
	public static int MODE_CRAZY = 2;
	public static int[][] LEVEL;
	public static int MAX_LEVEL = 0;
	private static final String SPLIT_CHAR = ",";
	public static boolean[] levelFlag;
	public static boolean music;
	public static int[] record;
	public static boolean sound = true;
	public static int SCREEN_WIDTH = 720;
	public static int SCREEN_HEIGHT = 1280;
	public static int GAME_MODE;
	public static int CRAZY_MODE_LEVEL = 10;
	public static long crazyModeRecord;
	public static long crazyModeTime;
	static {
		music = true;
		LEVEL = new int[][] { { 6, 5 }, { 6, 6 }, { 7, 6 }, { 8, 6 }, { 8, 7 },
				{ 8, 8 }, { 9, 8 }, { 10, 8 }, { 10, 9 }, { 10, 10 },
				{ 11, 10 }, { 12, 10 }, { 13, 10 }, { 14, 10 }, { 15, 10 },
				{ 16, 10 }};
		MAX_LEVEL = LEVEL.length;
		GAME_MODE = MODE_NORMAL;
		crazyModeTime = 0;
	}

	private static SharedPreferences getSharedPreferences(Context paramContext) {
		return paramContext.getSharedPreferences(CONFIG, 0);
	}

	public static void load(Context paramContext) {
		SharedPreferences localSharedPreferences = getSharedPreferences(paramContext);
		sound = localSharedPreferences.getBoolean(P_SOUND, true);
		music = localSharedPreferences.getBoolean(P_MUSIC, true);
		crazyModeRecord = localSharedPreferences.getLong(P_CRAZY_MODE_RECORD, 0);
		try {
			String[] strLevel = localSharedPreferences.getString(P_LEVEL, null)
					.split(SPLIT_CHAR);
			levelFlag = new boolean[strLevel.length];
			String[] strRecord = localSharedPreferences.getString(P_RECORD,
					null).split(SPLIT_CHAR);
			record = new int[strRecord.length];
			for (int k = 0; k < strLevel.length; k++) {
				levelFlag[k] = Boolean.parseBoolean(strLevel[k]);
				record[k] = Integer.parseInt(strRecord[k]);
			}
		} catch (Exception exception) {
			levelFlag = new boolean[MAX_LEVEL];
			record = new int[MAX_LEVEL];
			for (int i = 0; i < MAX_LEVEL; i++) {
				levelFlag[i] = false;
				record[i] = 0;
			}
			levelFlag[0] = true;
		}
	}

	public static void save(Context paramContext) {
		SharedPreferences.Editor localEditor = getSharedPreferences(
				paramContext).edit();
		localEditor.putBoolean(P_SOUND, sound);
		localEditor.putBoolean(P_MUSIC, music);
		localEditor.putLong(P_CRAZY_MODE_RECORD, crazyModeRecord);
		StringBuffer sbLevel = new StringBuffer();
		StringBuffer sbRecord = new StringBuffer();
		for (int i = 0; i < levelFlag.length; i++) {
			sbLevel.append(String.valueOf(levelFlag[i]) + SPLIT_CHAR);
			sbRecord.append(String.valueOf(record[i]) + SPLIT_CHAR);
		}
		localEditor.putString(P_LEVEL, sbLevel.toString());
		localEditor.putString(P_RECORD, sbRecord.toString());
		localEditor.commit();
	}
}
