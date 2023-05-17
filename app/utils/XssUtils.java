package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XssUtils {
    public static String cleanXSS(String str) {
        if(str==null || str.isEmpty()) return str;
        return removeTag(str);
    }

    public static String removeTag(String str) {
        Pattern p_script, p_style, p_html, p_special;
        Matcher m_script, m_style, m_html, m_special;

        //script
        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        m_script = p_script.matcher(str);
        str = m_script.replaceAll("");

        //style
        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        m_style = p_style.matcher(str);
        str = m_style.replaceAll("");

        //HTML
        String regEx_html = "<[^>]+>";
        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        m_html = p_html.matcher(str);
        str = m_html.replaceAll("");

        //special case
        String regEx_special = "\\&[a-zA-Z]{1,10};";
        p_special = Pattern.compile(regEx_special, Pattern.CASE_INSENSITIVE);
        m_special = p_special.matcher(str);
        str = m_special.replaceAll("");

        return str;
    }
}
