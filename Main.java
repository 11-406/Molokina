//Теорема Ферма
import java.util.Scanner;
import java.lang.Math;
public class Main{
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int p = sc.nextInt();
		int count = 0;
		for(int n = 1; n<p-1; n++){
			if(((Math.pow(n, p-1))-1)%p==0){
				count+=1;
			}
		}
		if (count==p-2){
			System.out.println("Число p простое");
		}else{
			System.out.println("Число p не является простым");
		}
	}
}