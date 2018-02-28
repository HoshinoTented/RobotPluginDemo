package top.tented.util

import com.saki.aidl.PluginMsg

/**
 * Member
 * Created by hoshino on 18-2-25 上午4:47.
 */
class Member(val group : Group, val id : Long, val name : String?) {
    fun sendWith(type : PluginMsg.Type, apply : PluginMsg.() -> Unit = {}) = group.sendWith(type) {
        uin = this@Member.id
        apply()
    }

    /**
     * 禁言
     * @param time 禁言时长(分钟)
     */
    fun shutUp(time : Int) = sendWith(PluginMsg.Type.MemberShutUp) { value = time * 60 }

    /**
     * 踢人
     */
    fun remove() = sendWith(PluginMsg.Type.RemoveMember)

    /**
     * 重命名
     * @param newName 新名字
     */
    fun rename(newName : String) = sendWith(PluginMsg.Type.Rename) { title = newName }

    /**
     * 点赞
     * @param times 点赞次数, 必须在1~10之间才有效
     */
    fun favourite(times : Int) = sendWith(PluginMsg.Type.Favourite) { if (times in 1..10) value = times }

    override fun equals(other : Any?) = (other as? Member)?.hashCode() == hashCode()
    override fun hashCode() = group.hashCode() * 31 + id.hashCode()
    override fun toString() = "$name($id)"
}