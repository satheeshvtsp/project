
import java.math.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class EllipticCurve {
	
	// The parameters of an EC.
	private BigInteger p;
	private BigInteger a;
	private BigInteger b;
	
	public EllipticCurve(BigInteger prime, BigInteger myA, BigInteger myB) {
		p = prime;
		a = myA;
		b = myB;
	}
	
	// Copy constructor.
	public EllipticCurve(EllipticCurve copy) {
		p = new BigInteger(copy.p.toString());
		a = new BigInteger(copy.a.toString());
		b = new BigInteger(copy.b.toString());	
	}
	public  Point at(BigInteger x,EllipticCurve curve){
		BigInteger x3=(x.multiply(x.multiply(x))); 
		BigInteger ax=(curve.getA()).multiply(x);
		BigInteger b=curve.getB();
		BigInteger p=curve.getP();
		
		Point result=new Point(curve,BigInteger.ZERO,BigInteger.ZERO);
	BigInteger ys=((x3.add(ax)).add(b)).mod(p);
	
	BigInteger res=BigInteger.ZERO;
for(BigInteger i=BigInteger.ZERO;i.compareTo(p)==-1;i=i.add(BigInteger.ONE)){
	BigInteger i2=i.multiply(i);
if((i2.mod(p)).compareTo(ys)==0){
res=i;
Point result1=new Point(curve,x,i);

return result1;

}
System.out.println(" x3"+x3+" ax"+ax+" b"+b+" p"+p+" ys"+ys+"i "+i+" x "+x+" i2mod(p) "+i2.mod(p));
}
//result[0]=BigInteger.ZERO;result[1]=BigInteger.ZERO;
return result;
}
	// All three components must be equal for the curves to be the same.
	public boolean equals(EllipticCurve other) {
		return p.equals(other.p) && a.equals(other.a) && b.equals(other.b);
	}
	
	public BigInteger getP() {
		return p;
	}
	
	public BigInteger getA() {
		return a;
	}	
	public BigInteger getB() {
		return b;
	}
public BigInteger order(Point g){
	
	int flag=0;
Point res=new Point(g);
BigInteger result=BigInteger.ONE;
BigInteger i=BigInteger.valueOf(2);
	try{
	File statText = new File("statsTest.txt");
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);


for(;;i=i.add(BigInteger.ONE)){
	Point multi=g.multiply(i);
		
if(i.compareTo(BigInteger.valueOf(2))==0){
	 res=new Point(multi);
	
}
w.write(" "+i+" g=>"+multi.getX()+" "+multi.getY()+"\n"+" "+res.getX()+" "+res.getY());//."p1".$p1." ".$p2;
if(i.compareTo(BigInteger.valueOf(2))!=0&&(res.getX()).compareTo(multi.getX())==0&&(res.getY()).compareTo(multi.getY())==0){
w.close();
return i;
}
}


//echo "<br/>flag".$flag;
	}catch(Exception e){}


return result;
	
	
}	
}