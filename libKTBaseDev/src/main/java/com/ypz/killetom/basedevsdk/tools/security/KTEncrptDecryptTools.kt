package com.ypz.killetom.basedevsdk.tools.security

import com.ypz.killetom.basedevsdk.tools.KTDataTools
import java.io.File
import java.io.FileInputStream
import java.nio.channels.FileChannel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class KTEncrptDecryptTools private constructor() {
    /*********************** 哈希加密相关  */
    private val DES_Algorithm = "DES"
    private val TripleDES_Algorithm = "DESede"
    private val AES_Algorithm = "AES"

    /**
     * DES转变
     *
     * 法算法名称/加密模式/填充方式
     *
     * 加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB
     *
     * 填充方式有：NoPadding、ZerosPadding、PKCS5Padding
     */
    var DES_Transformation = "DES/ECB/NoPadding"

    /**
     * 3DES转变
     *
     * 法算法名称/加密模式/填充方式
     *
     * 加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB
     *
     * 填充方式有：NoPadding、ZerosPadding、PKCS5Padding
     */
    var TripleDES_Transformation = "DESede/ECB/NoPadding"

    /**
     * AES转变
     *
     * 法算法名称/加密模式/填充方式
     *
     * 加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB
     *
     * 填充方式有：NoPadding、ZerosPadding、PKCS5Padding
     */
    var AES_Transformation = "AES/ECB/NoPadding"

    /**
     * MD2加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptMD2ToString(data: String): String {
        return encryptMD2ToString(data.toByteArray())
    }

    /**
     * MD2加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptMD2ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptMD2(data))
    }

    /**
     * MD2加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptMD2(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "MD2")
    }

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptMD5ToString(data: String): String {
        return encryptMD5ToString(data.toByteArray())
    }

    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @param salt 盐
     * @return 16进制加盐密文
     */

    fun encryptMD5ToString(data: String, salt: String): String {
        return KTDataTools.instants.bytes2HexString(encryptMD5((data + salt).toByteArray()))
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptMD5ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptMD5(data))
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @param salt 盐字节数组
     * @return 16进制加盐密文
     */

    fun encryptMD5ToString(data: ByteArray, salt: ByteArray): String {
        val dataSalt = ByteArray(data.size + salt.size)
        System.arraycopy(data, 0, dataSalt, 0, data.size)
        System.arraycopy(salt, 0, dataSalt, data.size, salt.size)
        return KTDataTools.instants.bytes2HexString(encryptMD5(dataSalt))
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptMD5(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "MD5")
    }

    /**
     * MD5加密文件
     *
     * @param filePath 文件路径
     * @return 文件的16进制密文
     */

    fun encryptMD5File2String(filePath: String?): String {
        return encryptMD5File2String(File(filePath))
    }

    /**
     * MD5加密文件
     *
     * @param filePath 文件路径
     * @return 文件的MD5校验码
     */

    fun encryptMD5File(filePath: String?): ByteArray? {
        return encryptMD5File(File(filePath))
    }

    /**
     * MD5加密文件
     *
     * @param file 文件
     * @return 文件的16进制密文
     */

    fun encryptMD5File2String(file: File): String {
        return if (encryptMD5File(file) != null) KTDataTools.instants.bytes2HexString(
            encryptMD5File(file)!!
        ) else ""
    }

    /**
     * MD5加密文件
     *
     * @param file 文件
     * @return 文件的MD5校验码
     */

    fun encryptMD5File(file: File): ByteArray? {

        try {
            var md: MessageDigest = MessageDigest.getInstance("MD5")

            FileInputStream(file).use { fileInputStream ->

                val channel = fileInputStream.channel

                val buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())

                md.update(buffer)
            }

            return md.digest()

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null

    }

    /**
     * SHA1加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptSHA1ToString(data: String): String {
        return encryptSHA1ToString(data.toByteArray())
    }

    /**
     * SHA1加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptSHA1ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptSHA1(data))
    }

    /**
     * SHA1加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptSHA1(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "SHA-1")
    }

    /**
     * SHA224加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptSHA224ToString(data: String): String {
        return encryptSHA224ToString(data.toByteArray())
    }

    /**
     * SHA224加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptSHA224ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptSHA224(data))
    }

    /**
     * SHA224加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptSHA224(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "SHA-224")
    }

    /**
     * SHA256加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptSHA256ToString(data: String): String {
        return encryptSHA256ToString(data.toByteArray())
    }

    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptSHA256ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptSHA256(data))
    }

    /**
     * SHA256加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptSHA256(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "SHA-256")
    }

    /**
     * SHA384加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptSHA384ToString(data: String): String {
        return encryptSHA384ToString(data.toByteArray())
    }
    /************************ DES加密相关  */
    /**
     * SHA384加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptSHA384ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptSHA384(data))
    }

    /**
     * SHA384加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptSHA384(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "SHA-384")
    }

    /**
     * SHA512加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */

    fun encryptSHA512ToString(data: String): String {
        return encryptSHA512ToString(data.toByteArray())
    }

    /**
     * SHA512加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */

    fun encryptSHA512ToString(data: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptSHA512(data))
    }

    /**
     * SHA512加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */

    fun encryptSHA512(data: ByteArray?): ByteArray {
        return encryptAlgorithm(data, "SHA-512")
    }

    /**
     * 对data进行algorithm算法加密
     *
     * @param data      明文字节数组
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private fun encryptAlgorithm(data: ByteArray?, algorithm: String): ByteArray {
        try {
            val md = MessageDigest.getInstance(algorithm)
            md.update(data)
            return md.digest()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ByteArray(0)
    }

    /**
     * @param data           数据
     * @param key            秘钥
     * @param algorithm      采用何种DES算法
     * @param transformation 转变
     * @param isEncrypt      是否加密
     * @return 密文或者明文，适用于DES，3DES，AES
     */

    fun DESTemplet(
        data: ByteArray?,
        key: ByteArray?,
        algorithm: String?,
        transformation: String?,
        isEncrypt: Boolean
    ): ByteArray? {
        try {
            val keySpec = SecretKeySpec(key, algorithm)
            val cipher = Cipher.getInstance(transformation)
            val random = SecureRandom()
            cipher.init(
                if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE,
                keySpec,
                random
            )
            return cipher.doFinal(data)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * DES加密后转为Base64编码
     *
     * @param data 明文
     * @param key  8字节秘钥
     * @return Base64密文
     */

    fun encryptDES2Base64(data: ByteArray?, key: ByteArray?): ByteArray {
        return KTEncodeDecodeTools.instant.base64Encode(encryptDES(data, key))
    }

    /**
     * DES加密后转为16进制
     *
     * @param data 明文
     * @param key  8字节秘钥
     * @return 16进制密文
     */

    fun encryptDES2HexString(data: ByteArray?, key: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptDES(data, key)!!)
    }
    /************************ 3DES加密相关  */
    /**
     * DES加密
     *
     * @param data 明文
     * @param key  8字节秘钥
     * @return 密文
     */

    fun encryptDES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, DES_Algorithm, DES_Transformation, true)
    }

    /**
     * DES解密Base64编码密文
     *
     * @param data Base64编码密文
     * @param key  8字节秘钥
     * @return 明文
     */

    fun decryptBase64DES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return decryptDES(KTEncodeDecodeTools.instant.base64Decode(data), key)
    }

    /**
     * DES解密16进制密文
     *
     * @param data 16进制密文
     * @param key  8字节秘钥
     * @return 明文
     */

    fun decryptHexStringDES(data: String?, key: ByteArray?): ByteArray? {
        return decryptDES(KTDataTools.instants.hexString2Bytes(data!!), key)
    }

    /**
     * DES解密
     *
     * @param data 密文
     * @param key  8字节秘钥
     * @return 明文
     */

    fun decryptDES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, DES_Algorithm, DES_Transformation, false)
    }

    /**
     * 3DES加密后转为Base64编码
     *
     * @param data 明文
     * @param key  24字节秘钥
     * @return Base64密文
     */

    fun encrypt3DES2Base64(data: ByteArray?, key: ByteArray?): ByteArray {
        return KTEncodeDecodeTools.instant.base64Encode(encrypt3DES(data, key))
    }

    /**
     * 3DES加密后转为16进制
     *
     * @param data 明文
     * @param key  24字节秘钥
     * @return 16进制密文
     */

    fun encrypt3DES2HexString(data: ByteArray?, key: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encrypt3DES(data, key)!!)
    }

    /**
     * 3DES加密
     *
     * @param data 明文
     * @param key  24字节密钥
     * @return 密文
     */

    fun encrypt3DES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, TripleDES_Algorithm, TripleDES_Transformation, true)
    }

    /**
     * 3DES解密Base64编码密文
     *
     * @param data Base64编码密文
     * @param key  24字节秘钥
     * @return 明文
     */

    fun decryptBase64_3DES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return decrypt3DES(KTEncodeDecodeTools.instant.base64Decode(data), key)
    }
    /************************ AES加密相关  */
    /**
     * 3DES解密16进制密文
     *
     * @param data 16进制密文
     * @param key  24字节秘钥
     * @return 明文
     */

    fun decryptHexString3DES(data: String?, key: ByteArray?): ByteArray? {
        return decrypt3DES(KTDataTools.instants.hexString2Bytes(data!!), key)
    }

    /**
     * 3DES解密
     *
     * @param data 密文
     * @param key  24字节密钥
     * @return 明文
     */

    fun decrypt3DES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, TripleDES_Algorithm, TripleDES_Transformation, false)
    }

    /**
     * AES加密后转为Base64编码
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return Base64密文
     */

    fun encryptAES2Base64(data: ByteArray?, key: ByteArray?): ByteArray {
        return KTEncodeDecodeTools.instant.base64Encode(encryptAES(data, key))
    }

    /**
     * AES加密后转为16进制
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return 16进制密文
     */

    fun encryptAES2HexString(data: ByteArray?, key: ByteArray?): String {
        return KTDataTools.instants.bytes2HexString(encryptAES(data, key)!!)
    }

    fun encryptCRC(){

    }

    /**
     * AES加密
     *
     * @param data 明文
     * @param key  16、24、32字节秘钥
     * @return 密文
     */

    fun encryptAES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, AES_Algorithm, AES_Transformation, true)
    }

    /**
     * AES解密Base64编码密文
     *
     * @param data Base64编码密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    fun decryptBase64AES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return decryptAES(KTEncodeDecodeTools.instant.base64Decode(data), key)
    }

    /**
     * AES解密16进制密文
     *
     * @param data 16进制密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    fun decryptHexStringAES(data: String?, key: ByteArray?): ByteArray? {
        return decryptAES(KTDataTools.instants.hexString2Bytes(data!!), key)
    }

    /**
     * AES解密
     *
     * @param data 密文
     * @param key  16、24、32字节秘钥
     * @return 明文
     */
    fun decryptAES(data: ByteArray?, key: ByteArray?): ByteArray? {
        return DESTemplet(data, key, AES_Algorithm, AES_Transformation, false)
    }



    companion object {
        val instant by lazy { KTEncrptDecryptTools() }
    }
}

