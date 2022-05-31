import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

public interface TestInterfaceDynamicTestsDemo {

    @TestFactory
    default Stream<DynamicTest> dynamicTestsForPalindromes() {
        return Stream.of("racecar", "radar", "mom", "dad")
                .map(text -> dynamicTest(text, () -> assertTrue(isPalindrome(text))));
    }

    default boolean isPalindrome(String s){
        //将原字符串转换为字符数组并判断是否为数字字符或者字母后
        //统一转成小写字符append进stringbuilder
        StringBuilder stringBuilder = new StringBuilder();
        char[] ch = s.toCharArray();
        for(int i = 0; i < s.length();i++) {
            if (Character.isLetterOrDigit(ch[i])) {
                stringBuilder.append(Character.toLowerCase(ch[i]));
            }
        }
        //通过双指针对比首位字符，如果相同count++
        //当count的值与stringBuffer的长度的一半相同，则为回文串
        int count = 0;
        int count2 = stringBuilder.length() / 2;
        for (int j = 0; j < count2; j++) {
            if(stringBuilder.charAt(j) == stringBuilder.charAt(stringBuilder.length() - j - 1)){
                count++;
            }
        }

        return count == count2;
    }

}