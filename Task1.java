//input p э N, p - простое?
//Сложность: sqrt(n)
import java.util.Scanner;
public class Task1{
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		int p = sc.nextInt();
		double n = Math.sqrt(p);
		for(int i = 3; i*i<(n+0.5); i+=2){
			if(p%i==0){
				return false;
			}
		}
		return true;
	}
}

//Нахождение сложности: lim f(x)/f2(x)  x->infinity