package com.cat.zhou.removtewebview.command

/**
 * @author: zjf
 * @data:2019/12/18
 */
interface ResultBack {
     fun onResult(status: Int, action: String, result: Any)
}