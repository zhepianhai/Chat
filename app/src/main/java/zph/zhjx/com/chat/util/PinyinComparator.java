package zph.zhjx.com.chat.util;

import java.util.Comparator;

import zph.zhjx.com.chat.dao.People;

public class PinyinComparator implements Comparator<People> {

	@Override
	public int compare(People lhs, People rhs) {
		// TODO Auto-generated method stub
		return sort(lhs, rhs);
	}

	private int sort(People lhs, People rhs) {
		// 获取ascii值
		int lhs_ascii = lhs.getPinyinname().toUpperCase().charAt(0);
		int rhs_ascii = rhs.getPinyinname().toUpperCase().charAt(0);
		// 判断若不是字母，则排在字母之后
		if (lhs_ascii < 65 || lhs_ascii > 90)
			return 1;
		else if (rhs_ascii < 65 || rhs_ascii > 90)
			return -1;
		else
			return lhs.getPinyinname().compareTo(rhs.getPinyinname());
	}

}
