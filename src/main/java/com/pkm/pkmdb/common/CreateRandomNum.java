package com.pkm.pkmdb.common;

/**
 * 
 * @author lizg 生成随机数
 *
 */
public class CreateRandomNum {

	/**
	 * 将新获得的随机数与已产生的其它随机数相比较，若有重复数据，则丢弃，并重来一遍； 否则，将新数存入数组。
	 * 
	 * @param i  数组的大小
	 * @return 随机数组
	 */
	public int[] getRandomArray(int i) {
		int[] a = new int[i]; // a 随机数数组
		for (int m = 0; m < i; m++) { // m 已产生的随机数个数
			int temp = random();
			if (m == 0)
				a[0] = temp;
			else {
				for (int n = 0; n < m; n++) { // n 遍历已产生的随机数
					if (temp == a[n]) {
						temp = random();
						n = -1;
					}
				}
				a[m] = temp;
			}
		}
		return a;
	}

	/**
	 * 随机数发生器 0 <= Math.random() < 1
	 * 
	 * @return 1至10之间的随机整数
	 */
	private int random() {
		return (int) (10 * Math.random() + 1);
	}


	/**
	 * 生成随机数
	 * @param count 位数
	 * @return
	 */
	public  static String getRandomNum (int count) {
		com.pkm.pkmdb.common.CreateRandomNum ra = new com.pkm.pkmdb.common.CreateRandomNum();

		int[] randomNum = ra.getRandomArray(count-1);

		StringBuffer sb = new StringBuffer();

		for (int i=0;i<randomNum.length;i++) {
			sb.append(randomNum[i]);

		}
		return sb.toString();

	}

	/**
	 * 测试主程序
	 * 
	 * @param agrs
	 */
	public static void main(String[] agrs) {

		System.out.println(com.pkm.pkmdb.common.CreateRandomNum.getRandomNum(10));
	}

}
