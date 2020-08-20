package tech.ikora.seleniumagent;

import net.bytebuddy.asm.Advice;

import org.openqa.selenium.remote.RemoteWebDriver;
import tech.ikora.seleniumagent.helpers.AgentSourcePageFetcher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FindElementInterceptor {
    @Advice.OnMethodEnter
    public static void log(@Advice.This RemoteWebDriver driver, @Advice.AllArguments Object[] args) {
        try (Socket socket = new Socket("localhost", 8085)){

            final String js_code = "Element.prototype.getDomWithInlineStyle = (function () {\n" +
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
                    "        return node.tagName.toUpperCase() == tagName.toUpperCase();\n" +
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


            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            final byte[] url = driver.getCurrentUrl().getBytes(StandardCharsets.UTF_8);
            out.writeChar('u');
            out.writeInt(url.length);
            out.write(url);

            final byte[] arguments = Arrays.toString(args).getBytes(StandardCharsets.UTF_8);
            out.writeChar('a');
            out.writeInt(arguments.length);
            out.write(arguments);

            final byte[] dom = ((String)driver.executeScript(js_code)).getBytes(StandardCharsets.UTF_8);
            out.writeChar('d');
            out.writeInt(dom.length);
            System.out.println("Size of the DOM: " + dom.length);
            out.write(dom);

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

            final byte[] stackTrace = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
            out.writeChar('s');
            out.writeInt(stackTrace.length);
            out.write(stackTrace);
        } catch (IOException e) {
            System.out.println("Something went terribly wrong: " + e.getMessage());
        }
/*
        try{
            System.out.println("about to call loaded class");

            final Class<?> fetcher = Class.forName("tech.ikora.seleniumagent.helpers.AgentSourcePageFetcher");
            Method method = fetcher.getMethod("getCurrentDom", RemoteWebDriver.class);
            final String domString = (String)method.invoke(null, driver);

            System.out.println("just called loaded class");
            System.out.println(domString);
        }
        catch (Throwable e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
 */
    }
}
