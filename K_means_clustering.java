package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class K_means_clustering {
	static Scanner input = new Scanner(System.in);
	static int k;
	static Cluster clus;
	
	public static void main(String[] args) {


		k = Integer.parseInt(input.nextLine());		// Ŭ������ ����
		ArrayList<int[]> c = new ArrayList<>();		// n-vector���� ������ �׷� c, c�� ���� k���� ũ�ų� ����.
		int[][] z = new int[k][];					// �� Ŭ�������� ��ǥ����
		boolean end = false;						// z_i������ �ٲ�� false, �ٲ��������� false
		double[][] j = new double[k][2];			// j_i 0���� ||x_i-z_j||^2���� 1���� j_i�� ���ϴ� n-vector ��
		clus = new Cluster(k);
		
		while(input.hasNextLine()) {	// n-vector�� �޾Ƶ帲 
			String s = input.nextLine();
			if(s.equals(""))
				break;
			
			int vector_size = s.split(" ").length;
			StringTokenizer st = new StringTokenizer(s);
			
			int[] x = new int[vector_size];
			
			for(int i = 0; st.hasMoreTokens(); i++)
				x[i]=Integer.parseInt(st.nextToken());

			c.add(x);
		}

		int[] cIndex = new int[c.size()];			// n-vector x�� ��� �׷쿡 �����ִ��� �˷���

		// ó���� �ƹ� ���� ��ǩ������ ����.
		for(int i = 0; i<k; i++)
			z[i]=c.get(i);

		// z_i���� ������ ���������� ������.
		while(!end) {
			chooseJ(c, cIndex, z, j);
			int[][] newZ = new int[k][];
			re_representation(newZ, c, cIndex, k);			


			end = !diffrent(z, newZ);
			if(diffrent(z, newZ))
				z = newZ.clone();
		}

		// �� cluster�� ����ִ� x���͵��� ������
		for(int i = 0; i<k; i++) {
			System.out.print("Cluster "+i+" = ");
			ArrayList<int[]> cluster = clus.getClusterEntry(i);
			
			for(int n = 0; n<cluster.size(); n++) {
				int[] vector = cluster.get(n);
				System.out.print(Arrays.toString(vector));
			}
			System.out.println();
		}
	}


	public static void chooseJ(ArrayList<int[]> c, int[] cIndex, int[][] z, double[][] j) {
		for(int i = 0; i<c.size(); i++) {
			double min=Double.MAX_VALUE;
			int index = 0;

			for(int a = 0; a<z.length; a++) {
				double m = meanSquare(c.get(i), z[a]);

				if(m<min) {
					min = m;
					index = a;
				}
			}

			cIndex[i] = index;
			j[index][0] += min;
			j[index][1]++;
		}
	}

	public static double meanSquare(int[] x, int[] z) {

		double result = 0;

		for(int i = 0; i<x.length; i++) {
			result += Math.pow(x[i]-z[i], 2);
		}

		result = Math.sqrt(result);

		return result;
	}

	public static void re_representation(int[][] z, ArrayList<int[]> c, int[] cIndex, int k) {

		clus = new Cluster(k);
		
		for(int i = 0; i<k; i++) {
			for(int j = 0; j<cIndex.length; j++) {
				if(cIndex[j]==i)
					clus.chooseCluster(i, c.get(j));
			}
		}

		clus.makeMean(z);
			
	}

//	public static void makeMean(int[][][] cluster, int[][] z) {
//
//		for(int i = 0; i<cluster.length; i++) {
//			int[] mean = new int[cluster[0][0].length];
//			for(int j = 0; j<cluster[i].length; j++) {
//				for(int k = 0; k<cluster[0][0].length; k++) {
//					mean[j]+=cluster[i][j][k];
//				}
//			}
//			z[i]=mean;
//		}
//	}

	public static boolean diffrent (int[][] z, int[][] newZ) {
		for(int i = 0; i<z.length; i++) {
			for(int j = 0; j<z[i].length; j++) {
				if(z[i][j]!=newZ[i][j])
					return true;
			}
		}

		return false;
	}

}

class Cluster{

	ArrayList<int[]>[] clusters;

	public Cluster(int k){
		clusters = new ArrayList[k];
	
		for(int i = 0; i<k; i++) {
			ArrayList<int[]> a = new ArrayList<>();
			clusters[i]=a;
		}
	}
	
	
	public void chooseCluster(int i, int[] x) {
		clusters[i].add(x);
	}
	
	public void makeMean(int[][] z) {
		
		int vector_size = clusters[0].get(0).length;
		
		for(int i = 0; i<clusters.length; i++) {
			
			
			int[] mean = new int[vector_size];
			
			// ������ Cluster�ȿ� x������ ����� ��
			for(int j = 0; j<clusters[i].size(); j++) {
				for(int k = 0; k<vector_size; k++) {
					mean[k]+=clusters[i].get(j)[k];
				}
			}
			
			// Cluster mean ���ϱ�
			for(int j = 0; j<vector_size; j++) {
				mean[j] /= clusters[i].size();
			}			
			
			z[i] = mean;
		}
		
		
//		for(int i = 0; i<clusters.length; i++) {
//			int[] mean = new int[cluster[0][0].length];
//			for(int j = 0; j<cluster[i].length; j++) {
//				for(int k = 0; k<cluster[0][0].length; k++) {
//					mean[j]+=cluster[i][j][k];
//				}
//			}
//			z[i]=mean;
//		}
	}


	public ArrayList<int[]> getClusterEntry(int n){
		return clusters[n];
	}
	
	@Override
	public String toString() {
		String result = "";
		for(int i = 0; i < clusters.length; i++) {
			ArrayList<int[]> cluster = clusters[i];
			
			result += String.format("Cluster %d : ", i);
			for(int n = 0; n<cluster.size(); n++) {
				int[] vector = cluster.get(n);
				result += Arrays.toString(vector);
			}
			result += "\n";
		}
		
		return result;
	}
}
