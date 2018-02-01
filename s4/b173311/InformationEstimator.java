package s4.b173311; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    boolean targetReady = false;
    boolean spaceReady = false;
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency
    
    /*
    byte [] subBytes(byte [] x, int start, int end) {
        // corresponding to substring of String for  byte[] ,
        // It is not implement in class library because internal structure of byte[] requires copy.
        // byte []のStringの部分文字列に対応し、
        // byte []の内部構造がコピーを必要とするため、クラスライブラリに実装されていません。
        byte [] result = new byte[end - start];
        for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
        return result;
    }
    */

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    // IQ：カウントの情報量、-log2（count / sizeof（space））
    double iq(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) {
        myTarget = target;
        if(target.length>0) targetReady = true;
    }
    public void setSpace(byte []space) { 
        myFrequencer = new Frequencer();
        mySpace = space;
        myFrequencer.setSpace(space);
        spaceReady = true;
    }

    public double estimation(){
        /* 解答 */
        if(targetReady == false) return (double) 0.0;
        if(spaceReady == false) return Double.MAX_VALUE;
        
        myFrequencer.setTarget(myTarget);
        
        double [] prefixEstimation = new double[myTarget.length+1];
        
        prefixEstimation[0] = (double) 0.0; //IE("") = 0.0;
        
        for(int n=1;n<=myTarget.length;n++) {
            // target = "abcdef..", n = 4 for example, subByte(0, 4) = "abcd",
            // IE("abcd") = min( IE("")+iq(#"abcd"),
            //                   IE("a") + iq(#"bcd"),
            //                   IE("ab")+iq(#"cd"),
            //                   IE("abc")+iq(#"d") )
            // prefixEstimation[0] = IE(""), subByte(0,4) = "abcd",
            // prefixEstimation[1] = IE("a");  subByte(1,4)= "bcd",
            // prefixEstimation[2] = IE("ab");  subByte(2,4)= "cd",
            // prefixEstimation[3] = IE("abc");  subByte(3,4)= "d",
            // prefixEstimation[4] = IE("abcd");
            //
            double value = Double.MAX_VALUE;
            for(int start=n-1;start>=0;start--) {
                int freq = myFrequencer.subByteFrequency(start, n);
                if(freq != 0) {
                    // update "value" if it is needed.
                    double value1 = prefixEstimation[start]+iq(freq);
                    if(value>value1) value = value1;
                } else {
                    // here freq ==0. This means iq(freq) is infinite.
                    // freq is monotonically descreasing in this loop.
                    // Now the current "value" is the minimum.
                    break; 
                }
            }
            prefixEstimation[n]=value;
        }
        return prefixEstimation[myTarget.length];
        
        /*
        //以下、自分の元のやつ
        // TAGETが設定されていない場合
        if(myTarget == null) {
            return 0.0;
        }
        
        boolean [] partition = new boolean[myTarget.length+1];
        int np;
        np = 1<<(myTarget.length-1);
        // System.out.println("np="+np+" length="+myTarget.length);
        double value = Double.MAX_VALUE; // value = mininimum of each "value1".
        // value =各 "value1"の最小値。
        
        // SPACEが設定されいない場合
        if(mySpace == null) {
            return value;
        }
        
        // TAGETの長さが0の場合
        int targetLength = myTarget.length;
        if(targetLength == 0) {
            value = 0.0;
            return value;
        }

        for(int p=0; p < np; p++) { // There are 2^(n-1) kinds of partitions.
            // binary representation of p forms partition.
            // for partition {"ab" "cde" "fg"}
            // a b c d e f g   : myTarget
            // T F T F F T F T : partition:
            // pのバイナリ表現はパーティションを形成します。
            //パーティションのために {"ab" "cde" "fg"}
            // a b c d e f g   : myTarget
            // T F T F F T F T : partition:
            partition[0] = true; // I know that this is not needed, but..
            for(int i=0; i < myTarget.length -1;i++) {
                partition[i+1] = (0 !=((1<<i) & p));
            }
            partition[myTarget.length] = true;

            // Compute Information Quantity for the partition, in "value1"
            // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
            // "value1"にあるパーティションの情報量を計算する
            // 上記の例の場合、value1 = IQ（＃ "ab"）+ IQ（＃ "cde"）+ IQ（＃ "fg"）
            double value1 = (double) 0.0;
            int end = 0;;
            int start = end;
            while(start < myTarget.length) {
                // System.out.write(myTarget[end]);
                end++;;
                while(partition[end] == false) {
                    // System.out.write(myTarget[end]);
                    end++;
                }
                // System.out.print("("+start+","+end+")");
                myFrequencer.setTarget(subBytes(myTarget, start, end));
                value1 = value1 + iq(myFrequencer.frequency());
                start = end;
            }
            // System.out.println(" "+ value1);

            // Get the minimal value in "value"
            if(value1 < value) value = value1;
        }
        return value;
        */
    }

    public static void main(String[] args) {
	InformationEstimator myObject;
	double value;
	myObject = new InformationEstimator();
	myObject.setSpace("3210321001230123".getBytes());
	myObject.setTarget("0".getBytes());
	value = myObject.estimation();
	System.out.println(">0 "+value);
	myObject.setTarget("01".getBytes());
	value = myObject.estimation();
	System.out.println(">01 "+value);
	myObject.setTarget("0123".getBytes());
	value = myObject.estimation();
	System.out.println(">0123 "+value);
	myObject.setTarget("00".getBytes());
	value = myObject.estimation();
	System.out.println(">00 "+value);
    }
}
				  
			       

	
    
