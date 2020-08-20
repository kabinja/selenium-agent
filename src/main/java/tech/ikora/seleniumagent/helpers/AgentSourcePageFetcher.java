package tech.ikora.seleniumagent.helpers;

import org.openqa.selenium.remote.RemoteWebDriver;

public class AgentSourcePageFetcher {
    private final static String js_code;

    static {
        js_code = "Element.prototype.getDomWithInlineStyle = (function () {\n" +
                "    const noStyleTags = {\"BASE\":true,\"HEAD\":true,\"HTML\":true,\"META\":true,\"NOFRAME\":true,\"NOSCRIPT\":true,\"PARAM\":true,\"SCRIPT\":true,\"STYLE\":true,\"TITLE\":true};\n" +
                "    const ignoreTags = new Set(['SCRIPT']);\n" +
                "    const tagNames = [\"A\",\"ABBR\",\"ADDRESS\",\"AREA\",\"ARTICLE\",\"ASIDE\",\"AUDIO\",\"B\",\"BASE\",\"BDI\",\"BDO\",\"BLOCKQUOTE\",\"BODY\",\"BR\",\"BUTTON\",\"CANVAS\",\"CAPTION\",\"CENTER\",\"CITE\",\"CODE\",\"COL\",\"COLGROUP\",\"COMMAND\",\"DATALIST\",\"DD\",\"DEL\",\"DETAILS\",\"DFN\",\"DIV\",\"DL\",\"DT\",\"EM\",\"EMBED\",\"FIELDSET\",\"FIGCAPTION\",\"FIGURE\",\"FONT\",\"FOOTER\",\"FORM\",\"H1\",\"H2\",\"H3\",\"H4\",\"H5\",\"H6\",\"HEAD\",\"HEADER\",\"HGROUP\",\"HR\",\"HTML\",\"I\",\"IFRAME\",\"IMG\",\"INPUT\",\"INS\",\"KBD\",\"KEYGEN\",\"LABEL\",\"LEGEND\",\"LI\",\"LINK\",\"MAP\",\"MARK\",\"MATH\",\"MENU\",\"META\",\"METER\",\"NAV\",\"NOBR\",\"NOSCRIPT\",\"OBJECT\",\"OL\",\"OPTION\",\"OPTGROUP\",\"OUTPUT\",\"P\",\"PARAM\",\"PRE\",\"PROGRESS\",\"Q\",\"RP\",\"RT\",\"RUBY\",\"S\",\"SAMP\",\"SCRIPT\",\"SECTION\",\"SELECT\",\"SMALL\",\"SOURCE\",\"SPAN\",\"STRONG\",\"STYLE\",\"SUB\",\"SUMMARY\",\"SUP\",\"SVG\",\"TABLE\",\"TBODY\",\"TD\",\"TEXTAREA\",\"TFOOT\",\"TH\",\"THEAD\",\"TIME\",\"TITLE\",\"TR\",\"TRACK\",\"U\",\"UL\",\"VAR\",\"VIDEO\",\"WBR\"];\n" +
                "\n" +
                "    let defaultStylesByTagName = {};\n" +
                "    \n" +
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
                "    function deepCloneWithStyles (node) {\n" +
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
                "        return deepCloneWithStyles(this);\n" +
                "    }\n" +
                "})();\n" +
                "\n" +
                "return document.body.getDomWithInlineStyle();";
    }

    public static String getCurrentDom(RemoteWebDriver driver){
        return (String)driver.executeScript(js_code);
    }
}
