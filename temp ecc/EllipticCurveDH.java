import java.util.*;
import java.math.*;

public class EllipticCurveDH {

	// Parts of one EC-Diffie Hellman system.
	private EllipticCurve curve;
	private Point generator;
	private Point publicKey;
	private BigInteger privateKey;

	// We need a curve, a generator point (x,y) and a private key, nA, that will
	// be used to generate the public key.
	public EllipticCurveDH(EllipticCurve c, BigInteger x, BigInteger y, BigInteger nA) {
		curve = c;
		generator = new Point(curve, x, y);
		privateKey = nA;
		publicKey = generator.multiply(privateKey);
	}

    public static void main(String[] args) {

    	Scanner stdin = new Scanner(System.in);

		// Set up curve and generator.
        EllipticCurve myCurve = new EllipticCurve(new BigInteger("23"), new BigInteger("1"), new BigInteger("1"));
		Point gen = new Point(myCurve, new BigInteger("3"), new BigInteger("13"));

		// Ask user for both private keys.
		System.out.println("Enter Alice's private key.");
		BigInteger nAlice = new BigInteger(stdin.next());
		System.out.println("Enter Bob's private key.");
		BigInteger nBob = new BigInteger(stdin.next());

		// Calculate & print public "keys" exchanged.
		Point aliceSend = gen.multiply(nAlice);
		Point bobSend = gen.multiply(nBob);
		System.out.println("Alice sends to Bob: "+aliceSend);
		System.out.println("Bob sends ot Alice: "+bobSend);

		// Make both calculations for the shared key.
		Point aliceGets = bobSend.multiply(nAlice);
		Point bobGets = aliceSend.multiply(nBob);

		// Print out what is hopefully the shared key.
		System.out.println("Alice gets "+aliceGets);
		System.out.println("Bob gets "+bobGets);
    }
}