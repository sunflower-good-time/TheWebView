package com.cat.zhou.removtewebview.command

/**
 * @author: zjf
 * @data:2019/12/19
 */
open abstract  class Commands {

    private var hashMap : HashMap<String,Command>? = null

    init {
        hashMap = HashMap<String,Command>()
    }

    abstract fun getCommandLevel():Int

    fun getCommands ():HashMap<String,Command> = hashMap!!

    /**
     * 注册Command
     */
    fun registerCommand(command: Command){
        hashMap!![command.name()] = command
    }
}