package com.saki.aidl

import java.util.ArrayList
import java.util.HashMap

import android.os.Parcel
import android.os.Parcelable
import android.os.RemoteException
import saki.demo.Demo
import top.tented.util.Group
import top.tented.util.Member
import top.tented.utils.get

class PluginMsg : Parcelable {
    enum class Key(val key : String) {
        Message("msg"),
        Xml("xml"),
        Json("json"),
        At("at"),
        Image("img"),
        Group("troop"),
        Member("member")
    }

    enum class Type(val type : Int) {
        Data(-1),
        Group(0),
        Friend(1),
        Discussion(2),
        System(3),
        Temp(4),
        GroupList(5),
        GroupInfo(6),
        GroupMember(7),
        Favourite(8),
        Rename(9),
        MemberShutUp(10),
        GroupShutUp(11),
        RemoveMember(12),
        AgreeJoin(13),
        MemberInfo(14),
        RobotAccount(15),
        MemberExited(16),
        AdminChange(17),
        Reload(18)
    }

    companion object {
        //必须提供一个名为CREATOR的static final属性 该属性需要实现android.os.Parcelable.Creator<T>接口
        //这个对象必须被@JvmField注解
        @JvmField val CREATOR : Parcelable.Creator<PluginMsg> = object : Parcelable.Creator<PluginMsg> {
            override fun createFromParcel(source : Parcel) : PluginMsg = PluginMsg(source)
            override fun newArray(size : Int) : Array<PluginMsg?> = arrayOfNulls(size)
        }
    }

    @JvmField var type : Type = Type.Group
    @JvmField var group : Long = 0
    @JvmField var code : Long = 0
    @JvmField var uin : Long = 0
    @JvmField var time : Long = 0
    @JvmField var value : Int = 0
    @JvmField var groupName : String? = null
    @JvmField var uinName : String? = null
    @JvmField var title : String? = null

    var groupid : Long
        get() = group
        set(value) { group = value }
    var data = HashMap<String, ArrayList<String>>()
    val textMsg : String get() = buildString { data["msg"]?.forEach { append(it) } }


    lateinit var msg : String
    lateinit var ats : List<Member>
    val member by lazy { Member(Group(group), uin, uinName) }

    constructor(type : Type = Type.Group) {
        this.type = type
    }

    constructor(source : Parcel) {
        readFromParcel(source)
    }

    override fun describeContents() : Int = 0

    override fun writeToParcel(dest : Parcel, flags : Int) {
        dest.writeInt(type.type)
        dest.writeLong(groupid)
        dest.writeLong(code)
        dest.writeLong(uin)
        dest.writeLong(time)
        dest.writeInt(value)
        dest.writeString(groupName)
        dest.writeString(uinName)
        dest.writeString(title)
        dest.writeMap(data)
    }

    private fun readFromParcel(source : Parcel) {
        type = Type.values()[source.readInt() + 1]      //This array starts from -1
        groupid = source.readLong()
        code = source.readLong()
        uin = source.readLong()
        time = source.readLong()
        value = source.readInt()
        groupName = source.readString()
        uinName = source.readString()
        title = source.readString()
        source.readMap(data, javaClass.classLoader)

        msg = textMsg
        ats = data[Key.At.key]?.map {
            it.indexOf('@').run {
                Member(
                        Group(group),
                        it[0, this].toLong(),
                        it.substring(this + 1)
                )
            }
        } ?: emptyList()
    }

    fun clearMsg() {
        data = HashMap()
    }

    fun addMsg(key : Key, msg : String) =
            (data["index"] ?: ArrayList<String>().apply { data["index"] = this }).run {
                add(key.key)
                (data["key"] ?: ArrayList<String>().apply { data[key.key] = this }).run { add(msg) }
            }

    @Throws(RemoteException::class)
    fun send() = Demo.connection?.service?.handlerMessage(this@PluginMsg)

    @Throws(RemoteException::class)
    fun send(key : Key, context : String) = PluginMsg().apply {
        group = this@PluginMsg.group
        addMsg(key, context)
    }.send()

}
