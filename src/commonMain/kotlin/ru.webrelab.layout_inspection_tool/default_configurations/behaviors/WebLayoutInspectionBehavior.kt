package ru.webrelab.layout_inspection_tool.default_configurations.behaviors

import ru.webrelab.layout_inspection_tool.LayoutElement
import ru.webrelab.layout_inspection_tool.LitException
import ru.webrelab.layout_inspection_tool.ifaces.IAdapter
import ru.webrelab.layout_inspection_tool.ifaces.IDrawer
import ru.webrelab.layout_inspection_tool.screen_difference.ComparisonFault
import ru.webrelab.layout_inspection_tool.screen_difference.DefaultSifter
import ru.webrelab.layout_inspection_tool.screen_difference.DefaultWebScanner
import ru.webrelab.layout_inspection_tool.screen_utils.BoxDrawer

abstract class WebLayoutInspectionBehavior<E>(
    violation: Int,
    adapter: IAdapter<E>,
    actionBeforeTesting: () -> Unit,
    reportFailures: (List<ComparisonFault<E>>) -> Unit
) : AbstractLayoutInspectionBehavior<E>(DefaultSifter(violation), DefaultWebScanner(), adapter, actionBeforeTesting, reportFailures) {

    override val drawer: IDrawer<E> = BoxDrawer()

    override fun screenPreparation() {
        executeJs(measureText)
        executeJs(measurePseudoElements)
        executeJs(measureDecor)
    }

    override fun getStyles(element: E): Map<String, Any> {
        @Suppress("UNCHECKED_CAST")
        val data: List<Map<String, Any>> = executeJs(getStyles, element as Any) as List<Map<String, Any>>
        return data.filter { it.size == 2 }
            .associateTo(mutableMapOf()) { it["name"] as String to it["value"] as Any }
    }

    open fun executeJs(js: String, vararg element: Any): Any {
        throw LitException("Method doesn't implement")
    }

    fun drawCanvas() {
        executeJs(createCanvas)
    }

    fun greedDraw(element: LayoutElement<E>, state: String?) {
        val id = "${if (state == null) "" else "$state-"}${element.type} - ${element.id}"
        val data = mapOf<String, Any>(
            "top" to element.position.top + element.container?.top as Int,
            "left" to element.position.left + element.container?.left as Int,
            "width" to element.size.width,
            "height" to element.size.height,
            "className" to (state ?: element.type.toString()),
            "transform" to element.transform,
            "id" to id
        )
        executeJs(greedDraw, data)
    }

    private val createCanvas = """
        function handler(arg) {
            let styleText = [];
            let gridStyle = ".{TYPE}_grid{" +
                "position: absolute;" +
                "background-image: repeating-linear-gradient({DEG}deg, transparent 0 10px, {COLOR} 10.2px 11.5px);" +
                "border: 1px solid {COLOR};}";
            styleText.push('#visualisationContainer{' +
                'overflow:hidden;' +
                'z-index:9999999;' +
                'position:absolute;' +
                'top:0;' +
                'left:0;' +
                'right:0;' +
                'height:' + getMaxHeight(document.body) + 'px;}');
            for (let n in arg) {
                styleText.push(
                    gridStyle
                        .replaceAll('{TYPE}', arg[n]['type'])
                        .replaceAll('{DEG}', arg[n]['deg'])
                        .replaceAll('{COLOR}', arg[n]['color'])
                );
            }
            let style = document.createElement("style");
            style.innerText = styleText.join(" ");
            let div = document.createElement('div');
            div.id = 'visualisationContainer';
            div.appendChild(style);
            document.body.appendChild(div);
        }

        function getMaxHeight(node) {
            let max = 'scrollHeight' in node ? node.scrollHeight : node.clientHeight;
            [...node.children].forEach(e => max = Math.max(max, getMaxHeight(e)));
            return max;
        }
    """.trimIndent()

    private val getElementAttributes = """
        function handler(attributeToElementNameMap) {
            let keys = Object.keys(attributeToElementNameMap);
            return attributeToElementNameMap[keys[0]].getAttribute(keys[0]);
        }
    """.trimIndent()

    private val getElementSize = """
        function handler(element) {
            return {
                width: element.clientWidth === 0 ? element.offsetWidth : element.clientWidth,
                height: element.clientHeight === 0 ? element.offsetHeight : element.clientHeight
            }
        }
    """.trimIndent()

    private val getStyles = """
        function handler(element) {
            let v = window.getComputedStyle(element, null), d = [];
            for (let q in v) {
                if (!isNaN(parseFloat(q)) && isFinite(parseFloat(q))) continue;
                if (q === undefined || v[q] === null || typeof v[q] === 'function') continue;
                d.push({name: q, value: v[q]});
            }
            d.push({absoluteWidth: element.clientWidth === 0 ? element.offsetWidth : element.clientWidth})
            d.push({absoluteHeight: element.clientHeight === 0 ? element.offsetHeight : element.clientHeight})
            let rect = element.getBoundingClientRect()
            d.push({absoluteLeft: rect.left + window.scrollX})
            d.push({absoluteTop: rect.top + window.scrollY})
            return d;
        }
    """.trimIndent()

    private val getViewportSize = """
        function handler() {
           return  {
              height: window.innerHeight,
              width: window.innerWidth
           }
        }
    """.trimIndent()

    private val greedDraw = """
        function handler(arg) {
            let div = document.createElement('div');
            div.classList.add(arg['className'] + "_grid");
            div.id = arg['id'];
            div.style.cssText =
                'top:' + arg['top'] + 'px;' +
                'left:' + arg['left'] + 'px;' +
                'width:' + arg['width'] + 'px;' +
                'height:' + arg['height'] + 'px;' +
                'transform:' + arg['transform'] + ';';
            document.getElementById('visualisationContainer').appendChild(div);
        }
    """.trimIndent()

    private val measureDecor = """
        function handler() {
            let nodes = ['div', 'a', 'p', 'button', 'span', 'section', 'aside', 'input', 'textarea'];
            nodes.forEach(node => {
                [...document.body.getElementsByTagName(node)]
                    .forEach(e => {
                        let c = window.getComputedStyle(e);
                        if (c['display'] !== 'none' && (c['borderRadius'] !== '0px' || c['borderWidth'] !== '0px' ||
                            (c['backgroundColor'] !== 'rgba(0, 0, 0, 0)' || c['backgroundImage'] !== 'none') ||
                            c['boxShadow'] !== 'none')) {
                            e.classList.add('measuringTypeDecor');
                        }
                    });
            });
        }
    """.trimIndent()

    private val measurePseudoElements = """
        function handler() {
            walker(document.body);
        }

        function walker(node) {
            [...node.children].forEach(e => {
                try {
                    if (window.getComputedStyle(e).display === 'none') return;
                    let after = window.getComputedStyle(e, ':after')
                    if (
                        after.getPropertyValue('content') !== 'none' &&
                        after.getPropertyValue('height') !== '0px' &&
                        after.getPropertyValue('width') !== '0px'
                    ) {
                        e.classList.add('measuringAfterElement');
                    }
                    let before = window.getComputedStyle(e, ':before');
                    if (
                        before.getPropertyValue('content') !== 'none' &&
                        before.getPropertyValue('height') !== '0px' &&
                        before.getPropertyValue('width') !== '0px'
                    )  {
                        e.classList.add('measuringBeforeElement');
                    }
                } catch (f) {
                }
                walker(e);
            })
        }
    """.trimIndent()

    private val measureText = """
        function handler() {
            walker(document.body);
        }

        function walker(node) {
            if (node.tagName === 'STYLE' || node.tagName === 'SCRIPT') return
            let t = node.innerText.replace(/[\n\s]/g, '').toLowerCase();
            if (t === undefined || t.length === 0) return;
            if (window.getComputedStyle(node).display === 'none') return;
            [...node.children]
                .forEach(e => {
                    let r = e.innerText;
                    if (r === undefined || r.length === 0) return;
                    t = t.replace(r.replace(/[\n\s]/g, '').toLowerCase(), '');
                    walker(e);
                });
            if (t.trim().length > 0) {
                node.classList.add('measuringTypeText');
            }
        }
    """.trimIndent()

    private val pseudoElementsAttr = """
        function handler(element) {
            return {
                before: collect(element, ':before'),
                after: collect(element, ':after')
            };
        }

        function collect(element, type) {
            let elementStyles = window.getComputedStyle(element, null);
            let pseudoElementStyles = window.getComputedStyle(element, type);
            let mT = parseInt(pseudoElementStyles.marginTop);
            let mL = parseInt(pseudoElementStyles.marginLeft);
            let t = parseInt(pseudoElementStyles.top);
            if (isNaN(t)) t = parseInt(elementStyles.paddingTop);
            let l = parseInt(pseudoElementStyles.left);
            if (isNaN(l)) l = parseInt(elementStyles.paddingLeft);
            let height = pseudoElementStyles.height === 'auto' ?
                parseInt(pseudoElementStyles.lineHeight)
                : parseInt(pseudoElementStyles.height);
            let width = pseudoElementStyles.width === 'auto' ?
                parseInt(elementStyles.width)
                - parseInt(elementStyles.paddingLeft)
                - parseInt(elementStyles.paddingRight)
                - mL
                - parseInt(pseudoElementStyles.marginRight)
                : parseInt(pseudoElementStyles.width);
            return {
                content: pseudoElementStyles.content,
                height: height,
                width: width < 0 ? 0 : width,
                topOffset: Math.round(t + mT),
                leftOffset: Math.round(l + mL),
                background: pseudoElementStyles.background,
                color: pseudoElementStyles.color,
                transform: pseudoElementStyles.transform
            }
        }
    """.trimIndent()

    private val svgScan = """
        function handler(element) {
            let data = [];
            let tag = element.tagName.toLowerCase();
            switch (tag) {
                case 'filter':
                case 'lineargradient':
                    data.push({
                        type: tag,
                        defs: element.outerHTML.replace(/\n|\s{2}/g, '')
                    });
                    break;
                case 'g':
                case 'svg':
                case 'defs':
                    [...element.children].forEach(e => {
                        [...handler(e)].forEach(d => data.push(d));
                    })
                    break;
                case 'title':
                    break;
                case 'use':
                    let id = element.getAttribute("xlink:href").replace("#", "");
                    [...handler(document.getElementById(id))].forEach(d => data.push(d));
                    break;
                default:
                    recordData(data, element, tag);
            }
            return data;
        }

        function recordData(data, e, tag) {
            data.push({
                type: tag,
                fill: window.getComputedStyle(e, null)['fill'],
                stroke: window.getComputedStyle(e, null)['stroke'],
                strokeWidth: window.getComputedStyle(e, null)['strokeWidth'],
                d: e.getAttribute('d'),
                cx: e.getAttribute('cx'),
                cy: e.getAttribute('cy'),
                r: e.getAttribute('r'),
                width: e.getAttribute('width'),
                height: e.getAttribute('height'),
                rx: e.getAttribute('rx'),
                ry: e.getAttribute('ry'),
                x1: e.getAttribute('x1'),
                y1: e.getAttribute('y1'),
                x2: e.getAttribute('x2'),
                y2: e.getAttribute('y2'),
                points: e.getAttribute('points'),
                x: e.getAttribute('x'),
                y: e.getAttribute('y'),
                content: e === 'text' ? e.text() : ''
            })
        }
    """.trimIndent()
}