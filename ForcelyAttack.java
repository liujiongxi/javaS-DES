//package des;
//先将该类实例化，每次添加明密文队都需调用run函数并输入参数，
//求重叠部分只需调用find函数
//两个函数均返回String，记得接受返回值并将其输出
import des.EncryptCreator;

import java.util.ArrayList;
import java.util.List;

public class ForcelyAttack {
	private String pin;
	private String cin;
	private int runtime;
	private ArrayList<ArrayList<String>> keyBox;
	private ArrayList<String> repeatingKeys;
	ForcelyAttack(){

		this.runtime = 0;
		this.keyBox=new ArrayList<ArrayList<String>>();
	}
	public String run(String pin, String cin) {
		
		ArrayList<String> availableKeys =new ArrayList<String>();
		String text = "Possible keys for this time:"+"\n";
		char[] keyChar=new char[10];
		for(int i=0;i<1024;i++) {
			int temp =i;
			for (int j = 9; j>=0; j--) {
				if(temp%2==0) {
					keyChar[j]='0';
				}
				else if(temp%2==1) {
					keyChar[j]='1';
				}
				temp = temp/2;
			}
			String key = new String(keyChar);
			EncryptCreator dir = new EncryptCreator(pin,key);

			
			if(cin.equals(dir.encrypt())) {
				availableKeys.add(key);
				//System.out.println(dir.encrypt() + " _");
				text += key+'\n';
			}				
		}
		this.keyBox.add(availableKeys);
		if(runtime==0) {
			this.repeatingKeys = availableKeys;
		}
		
		this.runtime++;
		
		return text;
	}
	public String find() {
		ArrayList<String> temp = new ArrayList<String>();
		for(int i=0;i<this.repeatingKeys.size();i++) {
			
			for(int j=0;j<this.keyBox.get(this.runtime-1).size();j++) {
				if(this.repeatingKeys.get(i).equals(this.keyBox.get(this.runtime-1).get(j))) {
					temp.add(this.repeatingKeys.get(i));
					//System.out.print(this.repeatingKeys.get(i) + " !");
				}
			}
		}
		this.repeatingKeys=temp;
		if(this.repeatingKeys.size()==1) {
			return "Final Key : "+"\n"+this.repeatingKeys.get(0);
		}
		else if(this.repeatingKeys.size()>1){
			String resrlt ="Shared Keys :"+"\n";
			for(int i=0;i<this.repeatingKeys.size();i++) {
				resrlt+=this.repeatingKeys.get(i)+' ';
			}
			return resrlt;
		}
		else{
			
			return "Unable"+"\n";
		}
	}
	
}
