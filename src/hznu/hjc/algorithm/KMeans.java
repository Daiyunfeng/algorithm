package hznu.hjc.algorithm;

import java.util.ArrayList;
import java.util.List;

import hznu.hjc.entry.Point;

/**
 * 
 * @author lenovo
 * @date 2017年9月21日
 * 
 *       Kmeans 算法 http://blog.csdn.net/qll125596718/article/details/8243404
 * 		Kmeans 衍生 http://www.cnblogs.com/yixuan-xu/p/6272208.html
 *       测试使用二维
 */
public class KMeans
{
	private final double T = 0.1;
	private int n, k; // 点的个数 k个簇
	private List<Point> points, centers; // 数据集 中心
	private List<List<Point>> cluster;// 簇

	public void init()
	{
		n = 20;
		points = new ArrayList<Point>();
		/*
		 * 估计k=4 6,6,3,5
		 */
		points.add(new Point(0, 0)); // 1
		points.add(new Point(22.4, 21.8)); // 2
		points.add(new Point(20.7, 23.6)); // 2
		points.add(new Point(42.5, -20.5)); // 3
		points.add(new Point(-1.2, 1.0)); // 1
		points.add(new Point(-2.0, 2.5)); // 1
		points.add(new Point(-1.6, 2.0)); // 1
		points.add(new Point(-34.0, 60.8)); // 4
		points.add(new Point(-33.0, 64.3)); // 4
		points.add(new Point(24.8, 24.8)); // 2
		points.add(new Point(-2.0, 2.0)); // 1
		points.add(new Point(1.2, 1.1)); // 1
		points.add(new Point(-19.58, 63.5)); // 4
		points.add(new Point(-40.0, 58)); // 4
		points.add(new Point(-29, 66)); // 4
		points.add(new Point(-28, 61)); // 4
		points.add(new Point(44.2, -19.9)); // 3
		points.add(new Point(41.5, -23.3)); // 3
		points.add(new Point(48.8, -24.4)); // 3
		points.add(new Point(49.9, -22.8)); // 3
		Canopy canopy = new Canopy(points);
		canopy.cluster();
		k = canopy.getClusterNumber();
		centers = new ArrayList<Point>();
		cluster = new ArrayList<List<Point>>();
		for (int i = 0; i < k; i++)
		{
			cluster.add(new ArrayList<Point>());
		}
		List<Point> temp;
		temp = canopy.getClusterCenterPoints();
		for (int i = 0; i < k; i++)
		{
			for (Point point : temp)
			{
				this.centers.add(point);
			}
		}
	}

	private double getDistance(Point point1, Point point2)
	{
		double res = Math
				.sqrt((point1.x - point2.x) * (point1.x - point2.x) + (point1.y - point2.y) * (point1.y - point2.y));
		return res;
	}

	private void selectCenterPoints()
	{
		int size;
		double sumx, sumy;
		centers.clear();
		for (int i = 0; i < k; i++)
		{
			sumx = 0;
			sumy = 0;
			size = cluster.get(i).size();
			for (int j = 0; j < size; j++)
			{
				sumx += cluster.get(i).get(j).x;
				sumy += cluster.get(i).get(j).y;
			}
			sumx/=size;
			sumy/=size;
			centers.add(new Point(sumx,sumy));
		}
	}

	/**
	 * @return 平方误差
	 */
	private double devideClusters()
	{
		for (int j = 0; j < k; j++)
		{
			cluster.get(j).clear();
		}
		int index = 0;
		double min = 0, dis, sum = 0; // sum为簇集的平方误差
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < k; j++)
			{
				dis = getDistance(points.get(i), centers.get(j));
				if (j == 0)
				{
					min = getDistance(points.get(i), centers.get(j));
					index = j;
				}
				else if (j != 0 && min > dis)
				{
					min = dis;
					index = j;
				}
			}
			cluster.get(index).add(points.get(i));
			sum += min;
		}
		return sum;
	}

	/**
	 * 
	 * @return centers 簇的中心点
	 */
	public List<Point> getCenterPoints()
	{
		return centers;
	}
	
	/**
	 * 
	 * @return cluster 簇
	 */
	public List<List<Point>> getCluster()
	{
		return cluster;
	}
	
	/**
	 * 
	 * @return K 簇的个数
	 */
	public int getK()
	{
		return k;
	}
	
	public void KMeans()
	{
		double oldSse = -1, newSse; // 平方误差
		newSse = devideClusters();
		while (Math.abs(newSse - oldSse) > T)
		{
			oldSse = newSse;
			selectCenterPoints();
			newSse = devideClusters();
		}
	}
	
}
