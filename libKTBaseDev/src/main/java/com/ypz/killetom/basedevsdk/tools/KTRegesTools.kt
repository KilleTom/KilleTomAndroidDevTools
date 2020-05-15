@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.ypz.killetom.basedevsdk.tools

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object KTRegesTools {

    /******************** 正则相关常量  */
    //--------------------------------------------正则表达式-----------------------------------------
    /**
     * 原文链接：http://caibaojian.com/regexp-example.html
     * 提取信息中的网络链接:(h|H)(r|R)(e|E)(f|F) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
     * 提取信息中的邮件地址:\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*
     * 提取信息中的图片链接:(s|S)(r|R)(c|C) *= *('|")?(\w|\\|\/|\.)+('|"| *|>)?
     * 提取信息中的IP地址:(\d+)\.(\d+)\.(\d+)\.(\d+)
     * 提取信息中的中国电话号码（包括移动和固定电话）:(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}
     * 提取信息中的中国邮政编码:[1-9]{1}(\d+){5}
     * 提取信息中的中国身份证号码:\d{18}|\d{15}
     * 提取信息中的整数：\d+
     * 提取信息中的浮点数（即小数）：(-?\d*)\.?\d+
     * 提取信息中的任何数字 ：(-?\d*)(\.\d+)?
     * 提取信息中的中文字符串：[\u4e00-\u9fa5]*
     * 提取信息中的双字节字符串 (汉字)：[^\x00-\xff]*
     */
    /**
     * 正则：电话号码
     */
    const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"

    /**
     * 正则：身份证号码15位
     */
    const val REGEX_CH_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"

    /**
     * 正则：身份证号码18位
     */
    const val REGEX_CH_IDCARD18 =
        "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

    /**
     * 正则：身份证号码15或18位 包含以x结尾
     */
    const val REGEX_CH_IDCARD =
        "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|x|X)$)"

    /**
     * 正则：邮箱
     */
    const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

    /**
     * 正则：URL
     */
    const val REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?"

    /**
     * 正则：汉字
     */
    const val REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$"

    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    const val REGEX_DATE =
        "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"

    /**
     * 正则：IP地址
     */
    const val REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

    /**
     * 正则：手机号（简单）
     */
    const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"

    /**
     * 正则：手机号（精确）
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * 联通：130、131、132、145、155、156、175、176、185、186
     * 电信：133、153、173、177、180、181、189
     * 全球星：1349
     * 虚拟运营商：170
     */
    const val REGEX_MOBILE_EXACT =
        "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$"

    const val REGEX_CH_BANK_CARD =
        "^\\d{16,19}$|^\\d{6}[- ]\\d{10,13}$|^\\d{4}[- ]\\d{4}[- ]\\d{4}[- ]\\d{4,7}$"

    const val REGEX_SINGLE_NUMBER = "[0-9]*"

    /**
     * @desc 校验手机号码是否合法
     * @param mobileValue 手机号码
     * @param mobileRegex 手机正则
     * @return
     * */
    @JvmStatic
    fun validateMobile(
        mobileValue: CharSequence?,
        mobileRegex: String = REGEX_MOBILE_EXACT
    ): Boolean {
        return isMatch(mobileRegex, mobileValue)
    }

    /**
     * @param bankCardValue 银行卡号
     * @param bankCardRex 银行卡正则
     * */
    @JvmStatic
    fun validateBankCard(
        bankCardValue: CharSequence?,
        bankCardRex: String = REGEX_CH_BANK_CARD
    ): Boolean {
        return isMatch(bankCardRex, bankCardValue)
    }


    /**
     * @desc 正则判断是否通过校验，但并不一定是准确的身份证号码
     * @param idCardValue id号码
     * @param idCardRegex id正则
     * */
    @JvmStatic
    fun matchIdCard(idCardValue: CharSequence?, idCardRegex: String = REGEX_CH_IDCARD): Boolean {
        return isMatch(idCardRegex, idCardValue)
    }


    @JvmStatic
    fun matchNumber(numberValue: CharSequence?): Boolean {
        return isMatch(REGEX_SINGLE_NUMBER, numberValue)
    }

    /**
     * 身份证号码验证 1、号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，
     * 八位数字出生日期码，三位数字顺序码和一位数字校验码。 2、地址码(前六位数）
     * 表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。 3、出生日期码（第七位至十四位）
     * 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 4、顺序码（第十五位至十七位）
     * 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号， 顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数）
     * （1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
     * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2
     * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
     */
    /**
     * 功能：身份证的有效验证
     *
     * @param idCard 身份证号
     * @return <br> true 校验通过 </br> <br> false 校验失败 </br>
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun ChIDCardValidate(idCard: CharSequence): IDCardValidateResult {
        var errorInfo = "" // 记录错误信息

        if (!matchIdCard(idCard)) {
            errorInfo = "身份证不符合规则"
            return IDCardValidateResult(true, errorInfo)
        }

        val ValCodeArr = arrayOf(
            "1", "0", "x", "9", "8", "7", "6", "5", "4",
            "3", "2"
        )
        val Wi = arrayOf(
            "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
            "9", "10", "5", "8", "4", "2"
        )
        var Ai = ""
        // ================ 号码的长度 15位或18位 ================
        if (idCard.length != 15 && idCard.length != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。"
            return IDCardValidateResult(false, errorInfo)
        }
        // =======================(end)========================

        // ================ 数字 除最后以为都为数字 ================
        if (idCard.length == 18) {
            Ai = idCard.substring(0, 17)
        } else if (idCard.length == 15) {
            Ai = idCard.substring(0, 6) + "19" + idCard.substring(6, 15)
        }
        if (matchNumber(Ai)) {
            errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。"
            return IDCardValidateResult(false, errorInfo)
        }
        // =======================(end)========================

        // ================ 出生年月是否有效 ================
        val strYear = Ai.substring(6, 10) // 年份
        val strMonth = Ai.substring(10, 12) // 月份
        val strDay = Ai.substring(12, 14) // 月份
        if (validateDay("$strYear-$strMonth-$strDay") == false) {
            errorInfo = "身份证生日无效。"
            return IDCardValidateResult(false, errorInfo)
        }
        val gc = GregorianCalendar()
        val s = SimpleDateFormat("yyyy-MM-dd")
        try {
            if (gc[Calendar.YEAR] - strYear.toInt() > 150
                || gc.time.time - s
                    .parse("$strYear-$strMonth-$strDay")
                    .time < 0
            ) {
                errorInfo = "身份证生日不在有效范围。"
                return IDCardValidateResult(false, errorInfo)
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (strMonth.toInt() > 12 || strMonth.toInt() == 0) {
            errorInfo = "身份证月份无效"
            return IDCardValidateResult(false, errorInfo)
        }

        if (strDay.toInt() > 31 || strDay.toInt() == 0) {
            errorInfo = "身份证日期无效"
            return IDCardValidateResult(false, errorInfo)
        }
        // =====================(end)=====================

        // ================ 地区码时候有效 ================
        val h = GetAreaCode()
        if (h[Ai.substring(0, 2)] == null) {
            errorInfo = "身份证地区编码错误。"
            return IDCardValidateResult(false, errorInfo)
        }
        // ==============================================

        // ================ 判断最后一位的值 ================
        var TotalmulAiWi = 0
        for (i in 0..16) {
            TotalmulAiWi = (TotalmulAiWi
                    + Ai[i].toString().toInt() * Wi[i].toInt())
        }
        val modValue = TotalmulAiWi % 11
        val strVerifyCode = ValCodeArr[modValue]
        Ai += strVerifyCode
        if (idCard.length == 18) {
            if (!(Ai == idCard)) {
                errorInfo = "身份证无效，不是合法的身份证号码"
                return IDCardValidateResult(false, errorInfo)
            }
        } else {
            return IDCardValidateResult(false, errorInfo)
        }
        // =====================(end)=====================
        return IDCardValidateResult(false, errorInfo)
    }

    @JvmStatic
    fun validateDay(dayValue: String?, dayRegex: String = REGEX_DATE): Boolean {
        return isMatch(REGEX_DATE, dayValue)
    }

    /**
     * @desc 功能：设置地区编码
     * @return Hashtable 对象
     */
    private fun GetAreaCode(): Hashtable<String, String> {
        val hashtable = Hashtable<String, String>()
        hashtable["11"] = "北京"
        hashtable["12"] = "天津"
        hashtable["13"] = "河北"
        hashtable["14"] = "山西"
        hashtable["15"] = "内蒙古"
        hashtable["21"] = "辽宁"
        hashtable["22"] = "吉林"
        hashtable["23"] = "黑龙江"
        hashtable["31"] = "上海"
        hashtable["32"] = "江苏"
        hashtable["33"] = "浙江"
        hashtable["34"] = "安徽"
        hashtable["35"] = "福建"
        hashtable["36"] = "江西"
        hashtable["37"] = "山东"
        hashtable["41"] = "河南"
        hashtable["42"] = "湖北"
        hashtable["43"] = "湖南"
        hashtable["44"] = "广东"
        hashtable["45"] = "广西"
        hashtable["46"] = "海南"
        hashtable["50"] = "重庆"
        hashtable["51"] = "四川"
        hashtable["52"] = "贵州"
        hashtable["53"] = "云南"
        hashtable["54"] = "西藏"
        hashtable["61"] = "陕西"
        hashtable["62"] = "甘肃"
        hashtable["63"] = "青海"
        hashtable["64"] = "宁夏"
        hashtable["65"] = "新疆"
        hashtable["71"] = "台湾"
        hashtable["81"] = "香港"
        hashtable["82"] = "澳门"
        hashtable["91"] = "国外"
        return hashtable
    }

    /**
     * @desc 校验邮箱是否合法
     * @param emailValue 邮箱账号
     * @param emailRegex 邮箱正则
     * @return <br> true 校验通过 </br> <br> false 校验失败 </br>
     * */
    @JvmStatic
    fun validateEmail(emailValue: CharSequence?, emailRegex: String = REGEX_EMAIL): Boolean {
        return isMatch(emailRegex, emailValue)
    }

    /**
     * @desc 校验信息能容是否通过正则校验
     * @param regexValue 正则规则
     * @param messageValue 信息内容
     * @return <br> true 校验通过 </br> <br> false 校验失败 </br>
     * */
    @JvmStatic
    fun isMatch(regexValue: String?, messageValue: CharSequence?): Boolean {

        val message = messageValue ?: return false
        val regex = regexValue ?: return false

        return Pattern.matches(regex, message)
    }

    /**
     * @desc 校验url是否合法
     * @param urlValue url 值
     * @return <br> true 校验通过 </br> <br> false 校验失败 </br>
     * */
    @JvmStatic
    fun validateURL(urlValue: CharSequence?): Boolean {
        return isMatch(REGEX_URL, urlValue)
    }

    /**
     * @desc 判断是否存中文
     * @param chValue 字符串
     * @return <br> true 校验通过 </br> <br> false 校验失败 </br>
     * */
    @JvmStatic
    fun validateSingleChString(chValue: CharSequence?): Boolean {
        return isMatch(REGEX_CHZ, chValue)
    }

    class IDCardValidateResult(val result: Boolean, val info: String = "")
}