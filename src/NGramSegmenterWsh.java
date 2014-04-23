import java.io.IOException;


public class NGramSegmenterWsh {

	public static NGramWsh g;
	
	public static void readNGram() throws IOException {
		g = new NGramWsh("/home/zc/workspace/Segmenter/zuoye2/3grams.arpa");
	}
	
	public static final double punish = -10000000;
	
	public static String [] segment(String s) {
		
		int n = s.length();
		
		double [][][] v = new double [n+1][n+1][n+1];
		String [][][] p = new String [n+1][n+1][n+1];
		
		int i,j,k,h;
		
		for (j=2;j<=n;j++)
			for (i=1;i<j;i++) {
				v[0][i][j] = g.cal1("<s>")+g.cal2("<s>", s.substring(0,i))+g.cal3("<s>", s.substring(0,i), s.substring(i,j));
				p[0][i][j] = s.substring(0,i) + " " + s.substring(i,j);
				print(p[0][i][j]);
				print("->");
				println(v[0][i][j]);
			}
		println("#####");
		for (k=3;k<=n;k++)
			for (j=2;j<k;j++)
				for (i=1;i<j;i++) {
					v[i][j][k] = punish;//-Double.MAX_VALUE;//设置成无穷大的负值是有问题的。因为一会加入的也是负值，这样就会导致值不变都是服务穷。这样惩罚太严重。
					for (h=0;h<i;h++) {
						print(k);
						print(" ");
						print(j);
						print(" ");
						println(i);
						println("tmp");
						println(g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k)));
						println(v[h][i][j]);
						print("sum:");
						println(v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k)));
						println(v[i][j][k]);
						println(s.substring(h,i)+" "+s.substring(i,j)+" "+s.substring(j,k));
						println(v[i][j][k]<v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k)));
						
						if (v[i][j][k]<v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k))) {
							v[i][j][k] = v[h][i][j]+g.cal3(s.substring(h,i), s.substring(i,j), s.substring(j,k));
							p[i][j][k] = p[h][i][j]+" "+s.substring(j,k);
							print(p[i][j][k]);
							print("->");
							println(v[i][j][k]);
						}
					}
				}
		println("=====");
		double maxv = v[0][1][n]*g.cal3(s.substring(0,1), s.substring(1,n-1), "</s>");
		int ri=0,rj=1;
		for (j=1;j<=n-1;j++)
			for (i=0;i<j;i++) {
				if (maxv<v[i][j][n]*g.cal3(s.substring(i,j),s.substring(j,n-1),"</s>")) {
					print("old:");
					print(p[ri][rj][n]);
					print("</s>");
					print("->");
					println(v[ri][rj][n]);
					maxv = v[i][j][n]*g.cal3(s.substring(i,j),s.substring(j,n-1),"</s>");
					ri=i;
					rj=j;
					print("new");
					print(p[i][j][n]);
					print("</s>");
					print("->");
					println(v[i][j][n]);
				}
			}
		System.out.println(p[ri][rj][n]);
		return p[ri][rj][n].split(" ");
	}
	
	//tmp
	public static void print(Object o) {
		System.out.print(o);
	}
	public static void println(Object o) {
		System.out.println(o);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		readNGram();
		
		//println("abc".substring(0,2));
		//println("abc".substring(0,"abc".length()));
		//println(-Double.MAX_VALUE+100);
		System.out.println(g.dict1.size());
		System.out.println(g.dict2.size());
		System.out.println(g.dict3.size());
		println(g.cal3("展现", "滨海", "魅力"));
		String [] result = segment("展现滨海魅力");
		for (int i=0;i<result.length;i++) {
			print(result[i]);
			print(" ");
		}
		print("\n");
	}

}
