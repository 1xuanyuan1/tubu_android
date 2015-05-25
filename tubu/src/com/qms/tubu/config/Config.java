package com.qms.tubu.config;

public class Config {
	//商户PID
	public static final String PARTNER = "2088912257849883";
	//商户收款账号
	public static final String SELLER = "service@arqm.cn";
	//商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALZKFTWEJGgeI4A4waTu2qWXQO1j39eODNzKtYIkgRHDQU0Np6Cwwb8LAicuBagP88UrO7svxZcqwzDf+nTRxP7cNpTF5PSQ1E7WRhv8E7pZ0C+Gy4lSBwiMncDhr8joN2hVQr1paftbrz/Bo9hJv4u0kmqjTri90mSCr8HrQfjTAgMBAAECgYEAkdJNiRP+M7P4XjgI4DGaguCuNbixdbPSokUeUc912InDkSodMDR1qKfbvw/XwouQS+e7zo/2lmjE4DIsHos38/TwO2RnVRHGtuA5pw70PipeAOwkc9GFD17jEiWnF5qLxhrPVE3BTTaC6WATjycfBxSAaMChtYskj55xYVmsuPECQQDxA2M1HsbQhnqKpbQMbR9s/qVZkqvx/VwYgmOo93YEZWtRycmxAC6fUWDk/Je7Yef5Bj/KG2IjeIHgiqO0Vn8lAkEAwZ/jo2oHJmP+yIh0AQ63cvIpgrfQqOYSWpD2iWlgvXY98iFsIbQ7ix2Kn8RAaoNsf2F7soE7cuo3J4uxYBzylwJAf/Z4ef+5N4XvEiYgjmhxwwEA8an6OSV3/FteZpvcwUDH1kj5GU0rc9NVSr8CIK8+5uz2eWxIkuSssHdrq5gxMQJAC6uFXt4PHEM+ofuHcmDesbDiy5wkmWl0hbzi/xk42XJ8/VECYW2pgRMAvexoLYNKUMshV3ruiFiRhnKm+DIGdQJBAM6BxnARVtnwiGHtFZOzKU30O7oDtAJUupBmjNgUjuocYZTojwqBECFc3JFXbju9tfmMo1dE52gLiz2iWd/UPMs=";
	//支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	
	
	public static String APP_URL = "http://115.28.144.169/tubu/";
	public static String BASE_URL = APP_URL + "index.php/Client/";
	/**
	 * 登录
	 */
	public static String LOGIN_URL = BASE_URL + "Account/login";
	/**
	 * 注册
	 */
	public static String REGISTER_URL = BASE_URL + "Account/register";
	/**
	 * 添加定点
	 */
	public static String ADD_POINT_URL = BASE_URL + "Point/add";
	/**
	 * 添加定点
	 */
	public static String QUERY_POINT_URL = BASE_URL + "Point/query";
	/**
	 * 景区列表
	 */
	public static String QUERY_SCENIC_URL = BASE_URL + "Scenic/query";
	/**
	 * 景区列表
	 */
	public static String GETLOCATION_URL = BASE_URL + "Scenic/getlocation";
	/**
	 * 景区详情
	 */
	public static String GET_SCENICDETAILS_URL = BASE_URL + "Scenic/getScenic";
	/**
	 * 添加订单
	 */
	public static String ADD_ORDER_URL = BASE_URL + "ScenicOrder/addOrder";
	/**
	 * 获取订单
	 */
	public static String QUERY_ORDER_URL = BASE_URL + "ScenicOrder/getOrder";
	/**
	 * 查询地址（一个）
	 */
	public static String FIND_ADDRESS_URL = BASE_URL + "AccountAddress/find";
	/**
	 * 查询地址（多个）
	 */
	public static String SELECT_ADDRESS_URL = BASE_URL + "AccountAddress/query";
	/**
	 * 添加地址
	 */
	public static String ADD_ADDRESS_URL = BASE_URL + "AccountAddress/add";
}
