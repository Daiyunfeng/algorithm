package hznu.hjc.algorithm.canopy;

import java.util.ArrayList;
import java.util.List;

import hznu.hjc.entry.Point;


/**
 * Canopy算法 借助canopy算法计算对应的Kmeans中的K值大小 只是将混乱的数据划分成大概的几个类别，所以它是不太准确的
 * 其中对于计算K值来说，canopy算法中的T1没有意义，只用设定T2(T1>T2) 我们这里将T2设置为平均距离
 * http://blog.csdn.net/dliyuedong/article/details/40711399
 * Cluster 簇
 * 在使用了T1后 对于距离小于T2的加入簇中 小于T1的从点集中删除。即各个簇之间会有重叠的点击
 * http://www.cnblogs.com/jamesf/p/4751565.html
 * 
 * Kmeans对噪声抗干扰较弱，通过Canopy对比较小的NumPoint的Cluster直接去掉 有利于抗干扰。
 * Canopy选择出来的每个Canopy的centerPoint作为Kmeans比较科学。
 * @author YD
 * 交叉检验就是不断划分training set和test set来训练模型，最后得到t1t2的最优/局部最优值
 */
public class Canopy
{
	private List<Point> points = new ArrayList<Point>(); // 进行聚类的点
	private List<List<Point>> clusters = new ArrayList<List<Point>>(); // 存储簇
	private double T2 = -1; // 阈值

	public Canopy(List<Point> points)
	{
		for (Point point : points)
			// 进行深拷贝
			this.points.add(point);
	}

	/**
	 * 进行聚类，按照Canopy算法进行计算，将所有点进行聚类
	 */
	public void cluster()
	{
		T2 = getAverageDistance(points);
		while (points.size() != 0)
		{
			List<Point> cluster = new ArrayList<Point>();
			Point basePoint = points.get(0); // 基准点
			cluster.add(basePoint);
			points.remove(0);
			int index = 0;
			while (index < points.size())
			{
				Point anotherPoint = points.get(index);
				double distance = Math.sqrt((basePoint.x - anotherPoint.x) * (basePoint.x - anotherPoint.x)
						+ (basePoint.y - anotherPoint.y) * (basePoint.y - anotherPoint.y));
				if (distance <= T2)
				{
					cluster.add(anotherPoint);
					points.remove(index);
				}
				else
				{
					index++;
				}
			}
			clusters.add(cluster);
		}
	}

	/**
	 * 得到Cluster的数目
	 * 
	 * @return 数目
	 */
	public int getClusterNumber()
	{
		return clusters.size();
	}

	/**
	 * 获取Cluster对应的中心点(各点相加求平均)
	 * 
	 * @return
	 */
	public List<Point> getClusterCenterPoints()
	{
		List<Point> centerPoints = new ArrayList<Point>();
		for (List<Point> cluster : clusters)
		{
			centerPoints.add(getCenterPoint(cluster));
		}
		return centerPoints;
	}

	/**
	 * 得到的中心点(各点相加求平均)
	 * 
	 * @return 返回中心点
	 */
	private double getAverageDistance(List<Point> points)
	{
		double sum = 0;
		int pointSize = points.size();
		for (int i = 0; i < pointSize; i++)
		{
			for (int j = 0; j < pointSize; j++)
			{
				if (i == j)
					continue;
				Point pointA = points.get(i);
				Point pointB = points.get(j);
				sum += Math.sqrt(
						(pointA.x - pointB.x) * (pointA.x - pointB.x) + (pointA.y - pointB.y) * (pointA.y - pointB.y));
			}
		}
		int distanceNumber = pointSize * (pointSize + 1) / 2;
		double T2 = sum / distanceNumber / 2; // 平均距离的一半
		return T2;
	}

	/**
	 * 得到的中心点(各点相加求平均)
	 * 
	 * @return 返回中心点
	 */
	private Point getCenterPoint(List<Point> points)
	{
		double sumX = 0;
		double sumY = 0;
		for (Point point : points)
		{
			sumX += point.x;
			sumY += point.y;
		}
		int clusterSize = points.size();
		Point centerPoint = new Point(sumX / clusterSize, sumY / clusterSize);
		return centerPoint;
	}

	/**
	 * 获取阈值T2
	 * 
	 * @return 阈值T2
	 */
	public double getThreshold()
	{
		return T2;
	}

	/**
	 * 测试9个点，进行操作
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		List<Point> points = new ArrayList<Point>();
		points.add(new Point(0, 0));
		points.add(new Point(0, 1));
		points.add(new Point(1, 0));

		points.add(new Point(5, 5));
		points.add(new Point(5, 6));
		points.add(new Point(6, 5));

		points.add(new Point(10, 2));
		points.add(new Point(10, 3));
		points.add(new Point(11, 3));

		Canopy canopy = new Canopy(points);
		canopy.cluster();

		// 获取canopy数目
		int clusterNumber = canopy.getClusterNumber();
		System.out.println(clusterNumber);

		// 获取canopy中T2的值
		System.out.println(canopy.getThreshold());
	}
}