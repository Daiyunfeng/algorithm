package hznu.hjc.algorithm.canopy;

import java.util.ArrayList;
import java.util.List;

import hznu.hjc.entry.Point;


/**
 * Canopy�㷨 ����canopy�㷨�����Ӧ��Kmeans�е�Kֵ��С ֻ�ǽ����ҵ����ݻ��ֳɴ�ŵļ�������������ǲ�̫׼ȷ��
 * ���ж��ڼ���Kֵ��˵��canopy�㷨�е�T1û�����壬ֻ���趨T2(T1>T2) �������ｫT2����Ϊƽ������
 * http://blog.csdn.net/dliyuedong/article/details/40711399
 * Cluster ��
 * ��ʹ����T1�� ���ھ���С��T2�ļ������ С��T1�Ĵӵ㼯��ɾ������������֮������ص��ĵ��
 * http://www.cnblogs.com/jamesf/p/4751565.html
 * 
 * Kmeans�����������Ž�����ͨ��Canopy�ԱȽ�С��NumPoint��Clusterֱ��ȥ�� �����ڿ����š�
 * Canopyѡ�������ÿ��Canopy��centerPoint��ΪKmeans�ȽϿ�ѧ��
 * @author YD
 * ���������ǲ��ϻ���training set��test set��ѵ��ģ�ͣ����õ�t1t2������/�ֲ�����ֵ
 */
public class Canopy
{
	private List<Point> points = new ArrayList<Point>(); // ���о���ĵ�
	private List<List<Point>> clusters = new ArrayList<List<Point>>(); // �洢��
	private double T2 = -1; // ��ֵ

	public Canopy(List<Point> points)
	{
		for (Point point : points)
			// �������
			this.points.add(point);
	}

	/**
	 * ���о��࣬����Canopy�㷨���м��㣬�����е���о���
	 */
	public void cluster()
	{
		T2 = getAverageDistance(points);
		while (points.size() != 0)
		{
			List<Point> cluster = new ArrayList<Point>();
			Point basePoint = points.get(0); // ��׼��
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
	 * �õ�Cluster����Ŀ
	 * 
	 * @return ��Ŀ
	 */
	public int getClusterNumber()
	{
		return clusters.size();
	}

	/**
	 * ��ȡCluster��Ӧ�����ĵ�(���������ƽ��)
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
	 * �õ������ĵ�(���������ƽ��)
	 * 
	 * @return �������ĵ�
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
		double T2 = sum / distanceNumber / 2; // ƽ�������һ��
		return T2;
	}

	/**
	 * �õ������ĵ�(���������ƽ��)
	 * 
	 * @return �������ĵ�
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
	 * ��ȡ��ֵT2
	 * 
	 * @return ��ֵT2
	 */
	public double getThreshold()
	{
		return T2;
	}

	/**
	 * ����9���㣬���в���
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

		// ��ȡcanopy��Ŀ
		int clusterNumber = canopy.getClusterNumber();
		System.out.println(clusterNumber);

		// ��ȡcanopy��T2��ֵ
		System.out.println(canopy.getThreshold());
	}
}