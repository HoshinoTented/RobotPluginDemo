package top.tented.plugin

import com.saki.aidl.PluginMsg
import org.intellij.lang.annotations.Language
import java.util.regex.Matcher

/**
 * Handler
 * Created by hoshino on 18-2-18 下午6:29.
 */
abstract class Handler( val name : String , val version : String ) {
    /**
     * MessageHandler
     * 消息逻辑处理
     * @param regex 消息匹配的正则表达式
     * @param description 这个消息逻辑处理器的描述
     * @param `continue` 处理完此消息是否继续判断是否有其他消息需要处理
     * @param handle 消息处理的Lambda表达式
     */
    class MessageHandler(@Language("RegExp") val regex : String, val description : String?, val `continue` : Boolean, val handle : PluginMsg.(Matcher) -> Unit)

    val handlerList = ArrayList<MessageHandler>()

    protected fun message(@Language("RegExp") regex : String, description : String? = null, `continue` : Boolean = false, handle: PluginMsg.(Matcher) -> Unit) =
            handlerList.add(MessageHandler(regex, description, `continue`, handle))
}