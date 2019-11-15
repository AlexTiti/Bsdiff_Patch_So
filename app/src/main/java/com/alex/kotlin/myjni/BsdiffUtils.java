package com.alex.kotlin.myjni;

/**
 * @author Alex
 * @date 2019-11-14 17:10
 * @email 18238818283@sina.cn
 * @desc ...
 */
public class BsdiffUtils {

	static {
		System.loadLibrary("bsdiff-lib");
	}

	public native static int diffApk(String oldAPk,String newApk,String patch);

	public native static int combineNewApk(String oldAPk,String destApk,String patch);



	public native static int add(int first ,int second);

}
