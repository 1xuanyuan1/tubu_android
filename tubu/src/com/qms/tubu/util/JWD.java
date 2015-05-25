package com.qms.tubu.util;

public class JWD {
	static double Rc = 6378137;  // 赤道半径

	static double Rj = 6356725;  // 极半径 
	double m_LoDeg, m_LoMin, m_LoSec;  // longtitude 经度
	double m_LaDeg, m_LaMin, m_LaSec;
	double m_Longitude, m_Latitude;
	double m_RadLo, m_RadLa;
	double Ec;
	double Ed;
	public JWD(double longitude, double latitude)
	{
		m_LoDeg = (int)longitude;
		m_LoMin = (int)((longitude - m_LoDeg)*60);
		m_LoSec = (longitude - m_LoDeg - m_LoMin/60.)*3600;

		m_LaDeg = (int)latitude;
		m_LaMin = (int)((latitude - m_LaDeg)*60);
		m_LaSec = (latitude - m_LaDeg - m_LaMin/60.)*3600;

		m_Longitude = longitude;
		m_Latitude = latitude;
		m_RadLo = longitude * Math.PI/180.;
		m_RadLa = latitude * Math.PI/180.;
		Ec = Rj + (Rc - Rj) * (90.-m_Latitude) / 90.;
		Ed = Ec * Math.cos(m_RadLa);
	}
	public static JWD GetJWDB(JWD A, double distance, double angle)
	{
		double dx = distance*1000 * Math.sin(angle * Math.PI /180.);
		double dy = distance*1000 * Math.cos(angle * Math.PI /180.);

		//double dx = (B.m_RadLo - A.m_RadLo) * A.Ed;
		//double dy = (B.m_RadLa - A.m_RadLa) * A.Ec;



		double BJD = (dx/A.Ed + A.m_RadLo) * 180./Math.PI;
		double BWD = (dy/A.Ec + A.m_RadLa) * 180./Math.PI;
		JWD B = new JWD(BJD, BWD);
		return B;
	}



	//! 已知点A经纬度，根据B点据A点的距离，和方位，求B点的经纬度
	/*!
	 * /param longitude 已知点A经度
	 * /param latitude 已知点A纬度
	 * /param distance B点到A点的距离
	 * /param angle B点相对于A点的方位
	 * /return B点的经纬度坐标
	 */
	public static JWD GetJWDB(double longitude, double latitude, double distance, double angle)
	{
		JWD A = new JWD(longitude,latitude);
		return GetJWDB(A, distance, angle);
	}

	public static double GetSensor(double lat1, double lng1, double lat2, double lng2){
		return angle(new JWD(lng1,lat1),new JWD(lng2,lat2));

	}
	//! 计算点A 和 点B的经纬度，求他们的距离和点B相对于点A的方位
	/*!
	 * \param A A点经纬度
	 * \param B B点经纬度
	 * \param angle B相对于A的方位, 不需要返回该值，则将其设为空
	 * \return A点B点的角度
	 */
	public static double angle(JWD A, JWD B)
	{
		double dx = (B.m_RadLo - A.m_RadLo) * A.Ed;
		double dy = (B.m_RadLa - A.m_RadLa) * A.Ec;
		//	  double out = Math.sqrt(dx * dx + dy * dy);
		double angle = 0.0;
		angle = Math.atan(Math.abs(dx/dy))*180./Math.PI;
		// 判断象限
		double dLo = B.m_Longitude - A.m_Longitude;
		double dLa = B.m_Latitude - A.m_Latitude;

		if(dLo > 0 && dLa <= 0) {
			angle = (90. - angle) + 90.;
		}
		else if(dLo <= 0 && dLa < 0) {
			angle = angle + 180.;
		}
		else if(dLo < 0 && dLa >= 0) {
			angle = (90. - angle) + 270;
		}
		return angle;
	}

}
