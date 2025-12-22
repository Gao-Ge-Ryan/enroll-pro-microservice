package top.gaogle.framework.commons.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * XSS过滤工具类，使用Jsoup库对输入的字符串进行XSS攻击防护
 *
 * @author gaogle
 * @since 1.0.0
 */
public class XssUtil {

    /**
     * 使用自带的 basicWithImages 白名单
     */
    private static final Safelist WHITE_LIST = Safelist.relaxed();
    /**
     * 定义输出设置，关闭prettyPrint（prettyPrint=false），目的是避免在清理过程中对代码进行格式化
     * 从而保持输入和输出内容的一致性。
     */
    private static final Document.OutputSettings OUTPUT_SETTINGS = new Document.OutputSettings().prettyPrint(false);

    /*
      初始化白名单策略，允许所有标签拥有style属性。
      这是因为在富文本编辑中，样式通常通过style属性来定义，需要确保这些样式能够被保留。
     */
    static {
        // 富文本编辑时一些样式是使用 style 来进行实现的
        // 比如红色字体 style="color:red;"
        // 所以需要给所有标签添加 style 属性
        WHITE_LIST.addAttributes(":all", "style");
    }

    /**
     * 清理输入的字符串，移除潜在的XSS攻击代码。
     *
     * @param content 待清理的字符串，通常是用户输入的HTML内容。
     * @return 清理后的字符串，保证不包含XSS攻击代码。
     */
    public static String clean(String content) {
        // 使用定义好的白名单策略和输出设置清理输入的字符串
        return Jsoup.clean(content, "", WHITE_LIST, OUTPUT_SETTINGS);
    }
}

