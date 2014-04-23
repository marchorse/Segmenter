import java.io.IOException;


public class NGramSegmenter {

	public static NGram g;
	
	public static void readNGram() throws IOException {
		g = new NGram("/home/zc/workspace/Segmenter/zuoye2/3grams.arpa");
	}
	
	public static String [] segment(String s) {
		s = "\5"+s+"\6";
		
		int n = s.length();
		int h,i,j,k,t;
		
		double [][][][] v = new double [n][n][n][n];
		int [][][][] p = new int [n][n][n][n];
		
		for (i=0;i<n;i++)
			for (j=0;j<n;j++)
				for (k=0;k<n;k++) {
					print(s.substring(0,k));
					print("\n");
					print(s.substring(0,1));
					v[0][i][j][k]=
							g.cal1(s.substring(0,k));
					v[1][i][j][k]=
							g.cal2(s.substring(0,j), s.substring(j,k))+
							g.cal1(s.substring(0,j));
					v[2][i][j][k]=
							g.cal3(s.substring(0,i), s.substring(i,j), s.substring(j,k))+
							g.cal2(s.substring(0,i), s.substring(i,j))+
							g.cal1(s.substring(0,i));
				}
		
		for (i=0;i<n;i++)
			for (j=0;j<n;j++)
				for (k=0;k<n;k++) 
					for (t=0;t<n;t++) {
						v[t][i][j][k]=-Double.MAX_VALUE;
						p[t][i][j][k]=-1;
					}
		
		
		
		for (t=3;t<n;t++)
			for (k=2;k<n;k++)
				for (j=1;j<k;j++)
					for (i=0;i<k;i++) {
						for (h=0;h<i;h++) {
							if (v[t-1][h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k))>v[t][i][j][k]) {
								v[t][i][j][k] = v[t-1][h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k));
								p[t][i][j][k] = h;
							}
						}
					}
		
		int ri,rj,rt;
		ri=rj=rt=0;
		for (i=0;i<n;i++)
			for (j=0;j<n;j++)
				for (t=0;t<n;t++) {
					if (v[t][i][j][n]>v[rt][ri][rj][n]) {
						ri = i;
						rj = j;
						rt = t;
					}
				}
		
		int [] bestp = new int [rt];
		i = ri;
		j = rj;
		k = n;
		t = rt;
		int count = 1;
		while(p[t][i][j][k]!=0) {
			bestp[rt-count] = p[t][i][j][k];
			k = j;
			j = i;
			t = t-1;
			i = bestp[rt-count];
			count++;
		}
		
		String [] result = new String [rt];
		
		for (i=0;i<rt-1;i++) {
			result[i] = s.substring(bestp[i],bestp[i+1]);
		}
					
		return result;
	}
	
	//tmp
	public static void print(Object o) {
		System.out.print(o);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		readNGram();
		String [] r = segment("展现滨海魅力");
		for (int i=0;i<r.length;i++) {
			print(r[i]+" ");
		}
		print("\n");
	}

}
