package hznu.hjc.algorithm.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hznu.hjc.algorithm.*;
import hznu.hjc.entry.Point;

public class KMeansTest
{
	private KMeans kmeans;
	@Before
	public void setUp() throws Exception
	{
		kmeans = new KMeans();
		kmeans.init();
		kmeans.KMeans();
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test()
	{
		List<Point> centers = kmeans.getCenterPoints();
		List<List<Point>> cluster = kmeans.getCluster();
		int k =kmeans.getK();
		System.out.println("�ظ���:"+k);
		System.out.println("���ĵ�:");
		for(int i=0;i<centers.size();i++)
		{
			System.out.println("��"+i+"����    x:"+centers.get(i).x+"    y:"+centers.get(i).y);
		}
		for(int i=0;i<cluster.size();i++)
		{
			System.out.println("��"+i+"����:");
			for(int j=0;j<cluster.get(i).size();j++)
			{
				System.out.println("��"+j+"����    x:"+cluster.get(i).get(j).x+"    y:"+cluster.get(i).get(j).y);
			}
			System.out.println();
		}
	}

}
