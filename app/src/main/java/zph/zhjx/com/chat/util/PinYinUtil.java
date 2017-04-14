package zph.zhjx.com.chat.util;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/**
 *Created by adminZPH on 2017/3/14.
 * */
public class PinYinUtil {
	//莱比锡-莱比东
	//中文(老王) 转大写的汉语拼音LAOWANG
	//英文(LaoWang) 转大写LAOWANG
	//火星文( ⊙ o ⊙ 啊！) 转###A#
	//数字(123) 转 ###

	//如何判断？
	//利用正则表达式
	//中文[\u4E00-\U9FFF]
	//英文[a-zA-Z]
	//其余的一律转成 #
	
	public static String getPinYin(String username){
		
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			
			StringBuilder sb = new StringBuilder();
			
			for(int i=0;i<username.length();i++){
				String str = username.substring(i, i+1);
				if(str.matches("[\u4E00-\u9FFF]")){
					//说明str是中文，转拼音
					char c = str.charAt(0);
					String[] py = PinyinHelper.toHanyuPinyinStringArray(c,format);
					sb.append(py[0]);
				}else if(str.matches("[a-zA-Z]")){
					//说明str是英文，转大写
					sb.append(str.toUpperCase());
				}else{
					//说明是火星文或数字，转#
					sb.append("#");
				}
			}
			
			return sb.toString();
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			throw new RuntimeException("不正确的拼音格式");
		}
	}
}
