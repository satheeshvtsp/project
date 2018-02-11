import java.math.*;
import java.util.*;

public class ECC {
	
	// Parts of one ECC system.
	private EllipticCurve curve;
	private Point generator;
	private Point publicKey;
	private BigInteger privateKey;
	
	// We need a curve, a generator point (x,y) and a private key, nA, that will
	// be used to generate the public key.
	public ECC(EllipticCurve c, BigInteger x, BigInteger y, BigInteger nA) {
		
		curve = c;
		generator = new Point(curve, x, y);
		privateKey = nA;
		publicKey = generator.multiply(privateKey);
		System.out.println("public key"+publicKey);
	}
	
	// Encryption.
	public Point[] encrypt(Point plain) {
		
		// First we must pick a random k, in range.
		int bits = curve.getP().bitLength();
		BigInteger k = new BigInteger(bits, new Random());
		System.out.println("Picked "+k+" as k for encrypting.");
		
		// Our output is an ordered pair, (k*generator, plain + k*publickey)
		Point[] ans = new Point[2];
		ans[0] = generator.multiply(k);
		ans[1] = plain.add(publicKey.multiply(k));
		return ans;
	}
	
	
	
	// Decryption - notice the similarity to El Gamal!!!
	public Point decrypt(Point[] cipher) {
		
		// This is what we subtract out.
		Point sub = cipher[0].multiply(privateKey);
		
		// Subtract out and return.
		return cipher[1].subtract(sub);
	}
	
	public String toString() {
		
		return "Gen: "+generator+"\n"+
			   "pri: "+privateKey+"\n"+
			   "pub: "+publicKey;
	}
	public static BigInteger nextRandomBigInteger(BigInteger n) {
    Random rand = new Random();
    BigInteger result = new BigInteger(n.bitLength(), rand);
    while( result.compareTo(n) >= 0 ) {
        result = new BigInteger(n.bitLength(), rand);
    }
    return result;
}
	public static void main(String[] args) {
		
		
		// Just use the book's curve and test.
		
		EllipticCurve myCurve = new EllipticCurve(new BigInteger("DB7C2ABF62E35E668076BEAD208B",16), new BigInteger("DB7C2ABF62E35E668076BEAD2088",16), new BigInteger("659EF8BA043916EEDE8911702B22",16));
		System.out.println(myCurve.getA() +" "+ myCurve.getB()+ " "+ myCurve.getP() );
		//Point g=myCurve.at(BigInteger.valueOf(571),myCurve);
		Point g=new Point(myCurve,new BigInteger("0409487239995A5EE76B55F9C2",16),new BigInteger("F098A89CE5AF8724C0A23E0E0FF77500",16));
		System.out.println("g=>"+ g.getX()+"     "+g.getY());
		
		BigInteger x = g.getX();
		BigInteger y = g.getY();
		Point temp=new Point(myCurve,x,y.add(BigInteger.ONE));
		BigInteger n=  new BigInteger("DB7C2ABF62E35E7628DFAC6561C5",16); //myCurve.order(g);
		System.out.println("  n=   "+n);
		BigInteger nA = new BigInteger("1327248163406186073551067778457151");//nextRandomBigInteger(n);
		System.out.println("PrivateKey ="+nA);
		ECC Alice = new ECC(myCurve, x, y, nA);
		
		// I have hard-coded my plaintext point.
		Point plain = new Point(myCurve, new BigInteger("3"), new BigInteger("13"));
		System.out.println("encrypting "+plain);
		
		// Encrypt and print.
		Point[] cipher = Alice.encrypt(plain);
		System.out.println("cipher first part "+cipher[0]);
		System.out.println("cipher second part "+cipher[1]);
		
		// Decrypt and verify.
		Point recover = Alice.decrypt(cipher);
		System.out.println("recovered "+recover);
		
	}
}