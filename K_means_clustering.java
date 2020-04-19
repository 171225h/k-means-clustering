package test;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class K_means_clustering {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);

		int k = Integer.parseInt(input.nextLine());					// 클러스터 개수
		ArrayList<int[]> c = new ArrayList<>();		// n-vector들을 저장할 그룹 c, c의 수는 k보다 크거나 같다.
		int[][] z = new int[k][];					// 각 클러스터의 대표값들
		boolean end = false;						// z_i값들이 바뀌면 false, 바뀌지않으면 false
		double[][] j = new double[k][2];			// j_i 0열은 ||x_i-z_j||^2값들 1열은 j_i에 속하는 n-vector 수

		while(input.hasNextLine()) {	// n-vector를 받아드림 
			int[] x = new int[50];
			String s = input.nextLine();
			if(s.equals(""))
				break;
			StringTokenizer st = new StringTokenizer(s);

			for(int i = 0; st.hasMoreTokens(); i++)
				x[i]=Integer.parseInt(st.nextToken());

			c.add(x);
		}

		int[] cIndex = new int[c.size()];			// n-vector x가 어느 그룹에 속해있는지 알려줌

		// 처음엔 아무 수나 대푯값으로 설정.
		for(int i = 0; i<k; i++)
			z[i]=c.get(i);

		while(!end) {
			chooseJ(c, cIndex, z, j);
			int[][] newZ = new int[k][];
			re_representation(newZ, c, cIndex, k);			

			
			end = !diffrent(z, newZ);
		}

		for(int i = 0; i<cIndex.length; i++)
			System.out.print(cIndex[i]+" ");

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
		int[][][] cluster = new int[k][][];

		for(int i = 0; i<cIndex.length; i++)
			System.out.print(cIndex[i]+" ");
		System.out.println();
		
		int n = 0;
		for(int i = 0; i<k; i++) {
			int[][] group = new int[c.size()][];
			for(int j = 0; j<cIndex.length; j++) {
				if(cIndex[j]==i)
					group[n++]=c.get(j);
			}
			cluster[i] = group;
		}

		makeMean(cluster, z);

	}

	public static void makeMean(int[][][] cluster, int[][] z) {

		for(int i = 0; i<cluster.length; i++) {
			int[] mean = new int[cluster[0][0].length];
			for(int j = 0; j<cluster[i].length; j++) {
				for(int k = 0; k<cluster[0][0].length; k++) {
					mean[j]+=cluster[i][j][k];
				}
			}
			z[i]=mean;
		}
	}

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
