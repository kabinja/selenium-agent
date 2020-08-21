package tech.ikora.seleniumagent.helpers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AgentHelper {
    private final static String jsCode;

    static {
        jsCode = "Element.prototype.getDomWithInlineStyle = (function () {\n" +
                "    let defaultStylesByTagName = {};\n" +
                "\n" +
                "    const noStyleTags = {\"BASE\":true,\"HEAD\":true,\"HTML\":true,\"META\":true,\"NOFRAME\":true,\"NOSCRIPT\":true,\"PARAM\":true,\"SCRIPT\":true,\"STYLE\":true,\"TITLE\":true};\n" +
                "    const ignoreTags = new Set(['SCRIPT']);\n" +
                "    const tagNames = [\"A\",\"ABBR\",\"ADDRESS\",\"AREA\",\"ARTICLE\",\"ASIDE\",\"AUDIO\",\"B\",\"BASE\",\"BDI\",\"BDO\",\"BLOCKQUOTE\",\"BODY\",\"BR\",\"BUTTON\",\"CANVAS\",\"CAPTION\",\"CENTER\",\"CITE\",\"CODE\",\"COL\",\"COLGROUP\",\"COMMAND\",\"DATALIST\",\"DD\",\"DEL\",\"DETAILS\",\"DFN\",\"DIV\",\"DL\",\"DT\",\"EM\",\"EMBED\",\"FIELDSET\",\"FIGCAPTION\",\"FIGURE\",\"FONT\",\"FOOTER\",\"FORM\",\"H1\",\"H2\",\"H3\",\"H4\",\"H5\",\"H6\",\"HEAD\",\"HEADER\",\"HGROUP\",\"HR\",\"HTML\",\"I\",\"IFRAME\",\"IMG\",\"INPUT\",\"INS\",\"KBD\",\"KEYGEN\",\"LABEL\",\"LEGEND\",\"LI\",\"LINK\",\"MAP\",\"MARK\",\"MATH\",\"MENU\",\"META\",\"METER\",\"NAV\",\"NOBR\",\"NOSCRIPT\",\"OBJECT\",\"OL\",\"OPTION\",\"OPTGROUP\",\"OUTPUT\",\"P\",\"PARAM\",\"PRE\",\"PROGRESS\",\"Q\",\"RP\",\"RT\",\"RUBY\",\"S\",\"SAMP\",\"SCRIPT\",\"SECTION\",\"SELECT\",\"SMALL\",\"SOURCE\",\"SPAN\",\"STRONG\",\"STYLE\",\"SUB\",\"SUMMARY\",\"SUP\",\"SVG\",\"TABLE\",\"TBODY\",\"TD\",\"TEXTAREA\",\"TFOOT\",\"TH\",\"THEAD\",\"TIME\",\"TITLE\",\"TR\",\"TRACK\",\"U\",\"UL\",\"VAR\",\"VIDEO\",\"WBR\"];\n" +
                "\n" +
                "    for (let i = 0; i < tagNames.length; i++) {\n" +
                "        if(!noStyleTags[tagNames[i]]) {\n" +
                "            defaultStylesByTagName[tagNames[i]] = computeDefaultStyleByTagName(tagNames[i]);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    function computeDefaultStyleByTagName(tagName) {\n" +
                "        let defaultStyle = {};\n" +
                "\n" +
                "        const element = document.body.appendChild(document.createElement(tagName));\n" +
                "        const computedStyle = getComputedStyle(element);\n" +
                "\n" +
                "        for (let i = 0; i < computedStyle.length; i++) {\n" +
                "            defaultStyle[computedStyle[i]] = computedStyle[computedStyle[i]];\n" +
                "        }\n" +
                "\n" +
                "        document.body.removeChild(element);\n" +
                "        return defaultStyle;\n" +
                "    }\n" +
                "\n" +
                "    function hasTagName(node, tagName){\n" +
                "        if(node.tagName === undefined){\n" +
                "            return false;\n" +
                "        }\n" +
                "\n" +
                "        return node.tagName.toUpperCase() === tagName.toUpperCase();\n" +
                "    }\n" +
                "\n" +
                "    function getDefaultStyleByTagName(tagName) {\n" +
                "        tagName = tagName.toUpperCase();\n" +
                "\n" +
                "        if (!defaultStylesByTagName[tagName]) {\n" +
                "            defaultStylesByTagName[tagName] = computeDefaultStyleByTagName(tagName);\n" +
                "        }\n" +
                "\n" +
                "        return defaultStylesByTagName[tagName];\n" +
                "    }\n" +
                "\n" +
                "    function isIgnored(node){\n" +
                "        return ignoreTags.has(node.tagName);\n" +
                "    }\n" +
                "\n" +
                "    function isComputeStyle(node){\n" +
                "        return !noStyleTags[node.tagName] && node instanceof Element;\n" +
                "    }\n" +
                "\n" +
                "    function computeImageNode(node){\n" +
                "        let img = document.createElement(\"img\");\n" +
                "\n" +
                "        img.alt = node.alt;\n" +
                "        img.style.width = node.width;\n" +
                "        img.style.height = node.height;\n" +
                "        img.id = node.id;\n" +
                "        img.class = node.class;\n" +
                "\n" +
                "        return img;\n" +
                "    }\n" +
                "\n" +
                "    function deepCloneWithStyles (node) {\n" +
                "        if(hasTagName(node, \"img\")){\n" +
                "            return computeImageNode(node);\n" +
                "        }\n" +
                "\n" +
                "        const clone = node.cloneNode(false);\n" +
                "\n" +
                "        if (isComputeStyle(node)) {\n" +
                "            const computedStyle = getComputedStyle(node);\n" +
                "            const defaultStyle = getDefaultStyleByTagName(node.tagName);\n" +
                "\n" +
                "            for (let j = 0; j < computedStyle.length; j++) {\n" +
                "                const cssPropName = computedStyle[j];\n" +
                "\n" +
                "                if (computedStyle[cssPropName] !== defaultStyle[cssPropName]) {\n" +
                "                    clone.style[cssPropName] = computedStyle[cssPropName];\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        for (let child of node.childNodes){\n" +
                "            if(!isIgnored(child)){\n" +
                "                clone.appendChild(deepCloneWithStyles(child));\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        return clone;\n" +
                "    }\n" +
                "\n" +
                "    return function serializeWithStyles() {\n" +
                "        if (this.nodeType !== Node.ELEMENT_NODE) {\n" +
                "            throw new TypeError();\n" +
                "        }\n" +
                "\n" +
                "        return deepCloneWithStyles(this).outerHTML;\n" +
                "    }\n" +
                "})();\n" +
                "\n" +
                "return document.body.getDomWithInlineStyle();";
    }

    public static String getJsCode(){
        return jsCode;
    }

    public static String getStackTrace(){
        StringBuilder stringBuilder = new StringBuilder();
        for(StackTraceElement st: Thread.currentThread().getStackTrace()){
            final String name = String.format("%s:%s", st.getClassName(), st.getMethodName());

            if(name.startsWith("org.codehaus.plexus.")
                    || name.startsWith("org.apache.maven.")
                    || name.startsWith("org.testng.")
                    || name.startsWith("org.junit.")
                    || name.startsWith("jdk.internal.reflect.")
                    || name.startsWith("java.lang.Thread")
                    || name.startsWith("java.lang.reflect.Method")){
                continue;
            }

            stringBuilder.append(name);
            stringBuilder.append(";");
        }

        return stringBuilder.toString();
    }

    public static void sendMessage(DataOutputStream out, char type, String payload) throws IOException {
        final byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);

        out.writeChar(type);
        out.writeInt(bytes.length);
        out.write(bytes);
    }
}
