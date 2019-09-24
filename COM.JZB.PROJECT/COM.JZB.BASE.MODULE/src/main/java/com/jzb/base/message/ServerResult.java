package com.jzb.base.message;

import java.io.Serializable;

/**
 * 结果集对象
 * 
 * @author steelchina
 *
 */
public class ServerResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7556295999658379023L;

	/**
	 * 结果码
	 */
	private int code;

	/**
	 * 结果消息
	 */
	private String message;

	/**
	 * 结果集
	 * 
	 * @param resultCode
	 */
	public ServerResult(int resultCode) {
		this(resultCode, "OK");
	} // End ServerResult

	/**
	 * 结果集
	 * 
	 * @param resultCode
	 * @param resultMessage
	 */
	public ServerResult(int resultCode, String resultMessage) {
		super();
		this.code = resultCode;
		this.message = resultMessage;
	} // End ServerResult

	/**
	 * 响应消息
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	} // End getMessage

	/**
	 * 设置消息
	 * 
	 * @param resultMessage
	 */
	public void setMessage(String resultMessage) {
		this.message = resultMessage;
	} // End setMessage

	/**
	 * 获取结果码
	 * 
	 * @return
	 */
	public int getResultCode() {
		return code;
	} // End getResultCode

	/**
	 * 设置结果码
	 * 
	 * @param resultCode
	 */
	public void setResultCode(int resultCode) {
		this.code = resultCode;
	} // End setResultCode
} // End class ServerResult
