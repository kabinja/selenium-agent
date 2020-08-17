Element.prototype.serializeWithStyles = (function () {

    // Mapping between tag names and css default values lookup tables. This allows to exclude default values in the result.
    let defaultStylesByTagName = {};

    // Styles inherited from style sheets will not be rendered for elements with these tag names
    const noStyleTags = {"BASE":true,"HEAD":true,"HTML":true,"META":true,"NOFRAME":true,"NOSCRIPT":true,"PARAM":true,"SCRIPT":true,"STYLE":true,"TITLE":true};

    const ignoreTags = new Set(['SCRIPT']);

    // This list determines which css default values lookup tables are precomputed at load time
    // Lookup tables for other tag names will be automatically built at runtime if needed
    const tagNames = ["A","ABBR","ADDRESS","AREA","ARTICLE","ASIDE","AUDIO","B","BASE","BDI","BDO","BLOCKQUOTE","BODY","BR","BUTTON","CANVAS","CAPTION","CENTER","CITE","CODE","COL","COLGROUP","COMMAND","DATALIST","DD","DEL","DETAILS","DFN","DIV","DL","DT","EM","EMBED","FIELDSET","FIGCAPTION","FIGURE","FONT","FOOTER","FORM","H1","H2","H3","H4","H5","H6","HEAD","HEADER","HGROUP","HR","HTML","I","IFRAME","IMG","INPUT","INS","KBD","KEYGEN","LABEL","LEGEND","LI","LINK","MAP","MARK","MATH","MENU","META","METER","NAV","NOBR","NOSCRIPT","OBJECT","OL","OPTION","OPTGROUP","OUTPUT","P","PARAM","PRE","PROGRESS","Q","RP","RT","RUBY","S","SAMP","SCRIPT","SECTION","SELECT","SMALL","SOURCE","SPAN","STRONG","STYLE","SUB","SUMMARY","SUP","SVG","TABLE","TBODY","TD","TEXTAREA","TFOOT","TH","THEAD","TIME","TITLE","TR","TRACK","U","UL","VAR","VIDEO","WBR"];

    // Precompute the lookup tables.
    for (let i = 0; i < tagNames.length; i++) {
        if(!noStyleTags[tagNames[i]]) {
            defaultStylesByTagName[tagNames[i]] = computeDefaultStyleByTagName(tagNames[i]);
        }
    }

    function computeDefaultStyleByTagName(tagName) {
        let defaultStyle = {};

        const element = document.body.appendChild(document.createElement(tagName));
        const computedStyle = getComputedStyle(element);

        for (let i = 0; i < computedStyle.length; i++) {
            defaultStyle[computedStyle[i]] = computedStyle[computedStyle[i]];
        }

        document.body.removeChild(element);
        return defaultStyle;
    }

    function getDefaultStyleByTagName(tagName) {
        tagName = tagName.toUpperCase();

        if (!defaultStylesByTagName[tagName]) {
            defaultStylesByTagName[tagName] = computeDefaultStyleByTagName(tagName);
        }

        return defaultStylesByTagName[tagName];
    }

    function isIgnored(node){
        return ignoreTags.has(node.tagName);
    }

    function isComputeStyle(node){
        return !noStyleTags[node.tagName] && node instanceof Element;
    }

    function deepCloneWithStyles (node) {
        const clone = node.cloneNode(false);

        if (isComputeStyle(node)) {
            const computedStyle = getComputedStyle(node);
            const defaultStyle = getDefaultStyleByTagName(node.tagName);

            for (let j = 0; j < computedStyle.length; j++) {
                const cssPropName = computedStyle[j];

                if (computedStyle[cssPropName] !== defaultStyle[cssPropName]) {
                    clone.style[cssPropName] = computedStyle[cssPropName];
                }
            }
        }

        for (let child of node.childNodes){
            if(!isIgnored(child)){
                clone.appendChild(deepCloneWithStyles(child));
            }
        }

        return clone;
    }

    return function serializeWithStyles() {
        if (this.nodeType !== Node.ELEMENT_NODE) {
            throw new TypeError();
        }

        return deepCloneWithStyles(this);
    }
})();

return document.body.serializeWithStyles();