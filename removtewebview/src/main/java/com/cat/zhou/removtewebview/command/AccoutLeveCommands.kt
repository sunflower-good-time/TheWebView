package com.cat.zhou.removtewebview.command

import com.cat.zhou.removtewebview.utils.WebConstants

/**
 * @author: zjf
 * @data:2019/12/19
 */
class AccoutLeveCommands : Commands() {
    override fun getCommandLevel(): Int {
        return WebConstants.LEVEL_ACCOUNT
    }
}