package example

import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.*
import kotlin.browser.document

@JsName("require")
external fun require(lib: String): dynamic


external interface VNode {
    val tagName: String
    val properties: dynamic
    val children: Array<VNode>
}

//val x = require("virtual-dom").VNode as VirtualNode

val diff = require("virtual-dom/diff") as (VNode, VNode) -> dynamic

fun main(args: Array<String>) {
    val node1 = document.create.div {
        p { +"hello1" }
    }
    val vnode1 = toVNode(node1)

    val node2 = document.create.div {
        p { +"hello2" }
    }
    val vnode2 = toVNode(node2)

    val patches = diff(vnode1, vnode2)
    console.log("patches:")
    console.dir(patches)

}

fun toVNode(node: Element): VNode {
    val children = list(node.children).map(::toVNode).toTypedArray()
    return object : VNode {
        override val tagName: String
            get() = node.tagName
        override val properties: dynamic
            get() = toMap(node.attributes)
        override val children: Array<VNode>
            get() = children
    }
}

private fun <T> list(elements: ItemArrayLike<T>): List<T> {
    val result = mutableListOf<T>()
    for (i in 0 until elements.length) {
        val item = elements.item(i)
        item?.run {
            result.add(this)
        }
    }
    return result
}

fun toMap(attributes: NamedNodeMap): dynamic {
    val obj = js("{}")
    list(attributes).forEach { obj[it.name] = it.value }
    return obj
}


