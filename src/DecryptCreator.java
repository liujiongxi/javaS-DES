//1.使用构造函数生成实例
//2.调用此实例的encrypt() 方法，并用boolean[]接收返回值
package des;
public class DecryptCreator {
	private boolean isAscii = false;
    private String pin;
    private String keyin;
    private boolean[] key;
    private String cin;
    private boolean[] p;
    private boolean[][] pAscii;
    // private boolean[] c;
    private int asciiNum;
    
    private boolean[] c;
    private boolean[][] cAscii;
    // private int[] IPin;
    private int[] IPout;
    private int[] VerseIPout;
    private int[] f_EP_Boxout;

    private String[][] sbox1;
    private String[][] sbox2;

    private int[] Directout;
    private int[] kfirstout;
    private int[] ksecongout;

    // 构造函数，需引用明文与密钥

	public DecryptCreator(String cin, String keyin) {
		
		for(int i=0;i<cin.length();i++) {
			if(cin.charAt(i)!='0'&&cin.charAt(i)!='1')
				this.isAscii = true;
		}
		if(this.isAscii) {
			byte[] bytes = cin.getBytes();
			this.asciiNum = bytes.length;
			this.cAscii = new boolean[this.asciiNum][8];			 
			for (int i = 0; i < this.asciiNum; i++) {
				int temp =bytes[i];
				for (int j = 7; j>=0; j--) {
					if(temp%2==0) {
						this.cAscii[i][j]=false;
					}
					else if(temp%2==1) {
						this.cAscii[i][j]=true;
					}
					temp = temp/2;//temp为0后用0补位（使开头均为false）
				}	
	        }
		}
		else {
			this.c = new boolean[cin.length()];
	        
	        for (int i = 0; i < cin.length(); i++) {
	            if (cin.charAt(i) == '0')
	                this.c[i] = false;
	            else if (cin.charAt(i) == '1')
	                this.c[i] = true;
	        }
	       
		}
		this.key = new boolean[keyin.length()];
		for (int i = 0; i < keyin.length(); i++) {
	         if (keyin.charAt(i) == '0')
	             this.key[i] = false;
	         else if (keyin.charAt(i) == '1')
	             this.key[i] = true;
	     }
	    init();
	}

    public String decrypt() {
        boolean[] key = this.key;
        String pOut = new String();
		if(this.isAscii) {
			
			this.pAscii = new boolean[this.asciiNum][8];
			for(int i=0;i<this.asciiNum;i++) {
				this.pAscii[i]=DesFunction2(this.cAscii[i], key);
				char[][] temp = new char[this.pAscii[i].length][8];
				 for (int j = 0; j < this.pAscii[i].length; j++) {
			            if (!this.pAscii[i][j])
			                temp[i][j] = '0';
			            else if (this.pAscii[i][j])
			                temp[i][j] = '1';
			        }
				String tempBin = new String(temp[i], 0, temp[i].length);
				int g=0;
				int charAscii=0;
				for(int f=tempBin.length()-1;f>=0;f--) {
					if(tempBin.charAt(f)=='1') {
						charAscii += Math.pow(2,g);
					}
					g++;
				}
                // 确保只使用7位数据
                charAscii &= 0x7F;
                char thischar = (char)charAscii;
                pOut += thischar;
			}
			
		}
		else {
			this.p= DesFunction2(this.c, key);
			char[] temp = new char[this.p.length];
			 for (int i = 0; i < this.p.length; i++) {
		            if (!this.p[i])
		                temp[i] = '0';
		            else if (this.p[i])
		                temp[i] = '1';
		        }
			 pOut = new String(temp, 0, temp.length);
		}
 
        return pOut;
    }

    public void init() {
        // String IPin ="12345678";
        String IPout = "26314857";
        // this.IPin=stringToArray(IPin);
        this.IPout = stringToArray(IPout);

        String VerseIPout = "41357286";
        this.VerseIPout = stringToArray(VerseIPout);

        String f_EP_Boxout = "41232341";
        this.f_EP_Boxout = stringToArray(f_EP_Boxout);
        this.sbox1 = new String[][] {
                { "01", "00", "11", "10" },
                { "11", "10", "01", "00" },
                { "00", "10", "01", "11" },
                { "11", "01", "00", "10" } };
        this.sbox2 = new String[][] {
                { "00", "01", "10", "11" },
                { "10", "11", "01", "00" },
                { "11", "00", "01", "10" },
                { "10", "01", "00", "11" } };

        String Directout = "2431";
        this.Directout = stringToArray(Directout);

        String kfirstout = "3527401986";
        this.kfirstout = stringToArray(kfirstout);

        String ksecongout = "63748509";
        this.ksecongout = stringToArray(ksecongout);
    }

    // 解密密过程
    public boolean[] DesFunction2(boolean[] P, boolean[] Key) {
        boolean[] p = P;
        boolean[] k = Key;

        boolean[] k1 = k1Creator(k);
        boolean[] k2 = k2Creator(k);
        boolean[] c = new boolean[8];
        p = IP(p);
        p = f(p, k2);
        p = sw(p);
        p = f(p, k1);
        p = ViseIP(p);
        c = p;
        return c;
    }

    public boolean[] k1Creator(boolean[] k) {
        boolean[] k1 = new boolean[8];
        boolean[] ka = new boolean[10];
        boolean[] kb = new boolean[10];

        ka = replacement(k, this.kfirstout);
        for (int i = 0; i < 4; i++) {
            kb[i] = ka[i + 1];
        }
        kb[4] = ka[0];
        for (int i = 5; i < 8; i++) {
            kb[i] = ka[i + 1];
        }
        kb[9] = ka[5];
        k1 = replacement(kb, ksecongout);
        return k1;
    }

    public boolean[] k2Creator(boolean[] k) {
        boolean[] k2 = new boolean[8];
        boolean[] ka = new boolean[10];
        boolean[] kb = new boolean[10];
        ka = replacement(k, this.kfirstout);
        for (int i = 0; i < 3; i++) {
            kb[i] = ka[i + 1];
        }
        kb[3] = ka[0];
        kb[4] = ka[1];
        for (int i = 5; i < 7; i++) {
            kb[i] = ka[i + 1];
        }
        kb[8] = ka[5];
        kb[9] = ka[6];
        k2 = replacement(kb, ksecongout);
        return k2;
    }

    public boolean[] IP(boolean[] P) {
        boolean[] p = P;
        p = replacement(p, this.IPout);
        return p;
    }

    public boolean[] ViseIP(boolean[] P) {
        boolean[] p = P;
        p = replacement(p, this.VerseIPout);
        return p;
    }

    public boolean[] f(boolean[] P, boolean[] k1) {
        boolean[] p = new boolean[8];
        p = P;
        boolean[] pl = new boolean[p.length / 2];
        boolean[] pr = new boolean[p.length / 2];
        for (int i = 0; i < p.length / 2; i++) {
            pl[i] = p[i];
        }
        for (int i = 0; i < p.length / 2; i++) {
            pr[i] = p[i + p.length / 2];
        }
        // EP-box
        boolean[] p1 = new boolean[8];
        p1 = replacement(pr, this.f_EP_Boxout);
        // 异或
        boolean[] p2 = new boolean[8];
        for (int i = 0; i < 8; i++) {
            p2[i] = p1[i] ^ k1[i];
        }
        // S-box
        boolean[] p3 = new boolean[4];
        p3 = sbox(p2);
        // 直接置换
        boolean[] p4 = new boolean[4];
        p4 = replacement(p3, this.Directout);
        // 异或
        boolean[] newpl = new boolean[4];
        for (int i = 0; i < 4; i++) {
            newpl[i] = pl[i] ^ p4[i];
        }
        // 组合
        for (int i = 0; i < 4; i++) {
            p[i] = newpl[i];
        }
        for (int i = 4; i < 8; i++) {
            p[i] = pr[i - 4];
        }

        return p;
    }

    public boolean[] sw(boolean[] P) {
        // boolean[] temp = P;
        boolean[] p = new boolean[8];
        for (int i = 0; i < 4; i++) {
            p[i] = P[i + 4];
        }
        for (int i = 4; i < 8; i++) {
            p[i] = P[i - 4];
        }
        return p;
    }

    public boolean[] replacement(boolean[] R, int[] Out) {
        boolean[] r = new boolean[Out.length];
        for (int i = 0; i < Out.length; i++) {
            if (Out[i] != 0)
                r[i] = R[Out[i] - 1];
            else
                r[i] = R[9];
        }
        return r;
    }

    public boolean[] sbox(boolean[] origin) {
        boolean[] r = new boolean[4];
        int parameter0 = chooseParam(origin[0], origin[3]);
        int parameter1 = chooseParam(origin[1], origin[2]);
        int parameter2 = chooseParam(origin[4], origin[7]);
        int parameter3 = chooseParam(origin[5], origin[6]);
        r[0] = intToBoolean(Character.getNumericValue(this.sbox1[parameter0][parameter1].charAt(0)));
        r[1] = intToBoolean(Character.getNumericValue(this.sbox1[parameter0][parameter1].charAt(1)));
        r[2] = intToBoolean(Character.getNumericValue(this.sbox2[parameter2][parameter3].charAt(0)));
        r[3] = intToBoolean(Character.getNumericValue(this.sbox2[parameter2][parameter3].charAt(1)));

        return r;
    }

    public static boolean intToBoolean(int i) {
        if (i == 0)
            return false;
        else
            return true;
    }

    public int[] stringToArray(String s) {
        int[] r = new int[s.length()];
        for (int x = 0; x < s.length(); x = x + 1) {
            r[x] = Character.getNumericValue(s.charAt(x));
        }
        return r;
    }

    public int chooseParam(boolean a, boolean b) {
        if (!a) {
            if (!b)
                return 0;
            else
                return 1;
        } else {
            if (!b)
                return 2;
            else
                return 3;
        }

    }
}