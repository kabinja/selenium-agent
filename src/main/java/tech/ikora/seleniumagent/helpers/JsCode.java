package tech.ikora.seleniumagent.helpers;

public class JsCode {
    public static final String getCurrentDomWithStyles =
            "const getCurrentDomWithStyles = () => {\n" +
            "    const noStyleTags = new Set(['BASE', 'HEAD', 'HTML', 'META', 'NOFRAME', 'NOSCRIPT', 'PARAM', 'SCRIPT', 'STYLE', 'TITLE']);\n" +
            "    const ignoreTags = new Set(['SCRIPT', 'NOSCRIPT']);\n" +
            "    let defaultStylesCache = new Map();\n" +
            "\n" +
            "    function hasStyle(node) {\n" +
            "        return window.getComputedStyle(node, null) !== undefined;\n" +
            "    }\n" +
            "\n" +
            "    function getPropertyName(node, index) {\n" +
            "        return window.getComputedStyle(node, null).item(index);\n" +
            "    }\n" +
            "\n" +
            "    function getPropertyValue(node, property) {\n" +
            "        return window.getComputedStyle(node, null).getPropertyValue(property);\n" +
            "    }\n" +
            "\n" +
            "    function numberProperties(node) {\n" +
            "        return window.getComputedStyle(node, null).length;\n" +
            "    }\n" +
            "\n" +
            "    function isEmpty(value) {\n" +
            "        return value === undefined || value === null || value === '';\n" +
            "    }\n" +
            "\n" +
            "    function isDefaultStyle(cssPropName, propertyValue, defaultStyle) {\n" +
            "        if(!defaultStyle){\n" +
            "            return false;\n" +
            "        }\n" +
            "        \n" +
            "        return propertyValue === defaultStyle.getPropertyValue(cssPropName);\n" +
            "    }\n" +
            "\n" +
            "    function getDefaultStyle(node) {\n" +
            "        const key = JSON.stringify({\n" +
            "            tag: node.tagName,\n" +
            "            id: node.getAttribute('id'),\n" +
            "            classes: node.getAttribute('class') ? [node.getAttribute('class').split(' ')].sort() : []\n" +
            "        });\n" +
            "\n" +
            "        if (!defaultStylesCache.has(key)) {\n" +
            "            defaultStylesCache.set(key, window.getComputedStyle(node, null));\n" +
            "        }\n" +
            "\n" +
            "        return defaultStylesCache.get(key);\n" +
            "    }\n" +
            "\n" +
            "    function hasTagName(node, tagName) {\n" +
            "        if (node.tagName === undefined) {\n" +
            "            return false;\n" +
            "        }\n" +
            "\n" +
            "        return node.tagName.toUpperCase() === tagName.toUpperCase();\n" +
            "    }\n" +
            "\n" +
            "    function isIgnored(node) {\n" +
            "        if (node === undefined) {\n" +
            "            return true;\n" +
            "        }\n" +
            "\n" +
            "        if (node.tagName === undefined) {\n" +
            "            return false;\n" +
            "        }\n" +
            "\n" +
            "        return ignoreTags.has(node.tagName.toUpperCase());\n" +
            "    }\n" +
            "\n" +
            "    function isComputeStyle(node) {\n" +
            "        if (node.tagName === undefined) {\n" +
            "            return false;\n" +
            "        }\n" +
            "\n" +
            "        return node instanceof Element && !noStyleTags.has(node.tagName.toUpperCase());\n" +
            "    }\n" +
            "\n" +
            "    function computeImageNode(node) {\n" +
            "        const img = document.createElement('img');\n" +
            "\n" +
            "        if (node.alt) {\n" +
            "            img.alt = node.alt;\n" +
            "        }\n" +
            "\n" +
            "        if (node.id) {\n" +
            "            img.id = node.id;\n" +
            "        }\n" +
            "\n" +
            "        img.width = node.width;\n" +
            "        img.height = node.height;\n" +
            "        img.class = node.class;\n" +
            "\n" +
            "        return Promise.resolve(img);\n" +
            "    }\n" +
            "\n" +
            "    function computeLinkNode(node, importCss) {\n" +
            "        if (importCss && node.rel.toLowerCase() === 'stylesheet') {\n" +
            "            return loadCss(node);\n" +
            "        }\n" +
            "\n" +
            "        return Promise.resolve(null);\n" +
            "    }\n" +
            "\n" +
            "    async function loadCss(node) {\n" +
            "        try {\n" +
            "            const response = await fetch(node.href);\n" +
            "\n" +
            "            const styleNode = document.createElement('style');\n" +
            "            styleNode.innerText = await response.text();\n" +
            "\n" +
            "            return styleNode;\n" +
            "        }\n" +
            "        catch {\n" +
            "            return loadCssWithCorsAnywhere(node);\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    async function loadCssWithCorsAnywhere(node){\n" +
            "        const proxyUrl = 'https://cors-anywhere.herokuapp.com/';\n" +
            "        const response = await fetch(proxyUrl + node.href);\n" +
            "\n" +
            "        const styleNode = document.createElement('style');\n" +
            "        styleNode.innerText = await response.text();\n" +
            "\n" +
            "        return styleNode;\n" +
            "    }\n" +
            "\n" +
            "    function updateStyle(clone, node, importCss) {\n" +
            "        let defaultStyle = null;\n" +
            "        \n" +
            "        if(importCss){\n" +
            "            defaultStyle = getDefaultStyle(node);\n" +
            "        }\n" +
            "    \n" +
            "        const cssLength = numberProperties(node);\n" +
            "\n" +
            "        clone.style = {};\n" +
            "\n" +
            "        if (!hasStyle(node)) {\n" +
            "            return;\n" +
            "        }\n" +
            "\n" +
            "        for (let i = 0, l = cssLength; i < l; ++i) {\n" +
            "            const cssPropName = getPropertyName(node, i);\n" +
            "            const cssPropValue = getPropertyValue(node, cssPropName);\n" +
            "\n" +
            "            if (!isEmpty(cssPropValue) && !isDefaultStyle(cssPropName, cssPropValue, defaultStyle)) {\n" +
            "                clone.style[cssPropName] = cssPropValue;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    async function deepCloneWithStyles(node, importCss) {\n" +
            "        if (isIgnored(node)) {\n" +
            "            return Promise.resolve(null);\n" +
            "        }\n" +
            "\n" +
            "        if(hasTagName(node, 'style') && !importCss){\n" +
            "            console.log('ignore style node');\n" +
            "            return Promise.resolve(null);\n" +
            "        }\n" +
            "\n" +
            "        if (hasTagName(node, 'img')) {\n" +
            "            return computeImageNode(node);\n" +
            "        }\n" +
            "\n" +
            "        if (hasTagName(node, 'link')) {\n" +
            "            return computeLinkNode(node, importCss);\n" +
            "        }\n" +
            "\n" +
            "        const clone = node.cloneNode(false);\n" +
            "\n" +
            "        if (isComputeStyle(node)) {\n" +
            "            updateStyle(clone, node, importCss);\n" +
            "        }\n" +
            "\n" +
            "        for (let child of node.childNodes) {\n" +
            "            const cloneChild = await deepCloneWithStyles(child, importCss);\n" +
            "            \n" +
            "            if(cloneChild){\n" +
            "                clone.appendChild(cloneChild);\n" +
            "            }\n" +
            "\n" +
            "        }\n" +
            "\n" +
            "        return Promise.resolve(clone);\n" +
            "    }\n" +
            "\n" +
            "    return (async () => {\n" +
            "        let clone = null;\n" +
            "\n" +
            "        try{\n" +
            "            clone = await deepCloneWithStyles(document.documentElement, true);\n" +
            "        }\n" +
            "        catch(e){\n" +
            "            clone = await deepCloneWithStyles(document.documentElement, false);\n" +
            "        }\n" +
            "\n" +
            "        return clone.outerHTML;\n" +
            "    })();\n" +
            "};\n" +
            "\n" +
            "const callback = arguments[arguments.length - 1];\n" +
            "\n" +
            "getCurrentDomWithStyles()\n" +
            ".then(callback)\n" +
            ".catch(callback);";

}
