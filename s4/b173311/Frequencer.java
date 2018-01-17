package s4.b173311; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID. 
import java.lang.*;
import s4.specification.*;

/*
interface FrequencerInterface {     // This interface provides the design for frequency counter.
    void setTarget(byte[]  target); // set the data to search.
    void setSpace(byte[]  space);  // set the data to be searched target from.
    int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
                    //Otherwise, it return 0, when SPACE is not set or Space's length is zero
                    //Otherwise, get the frequency of TAGET in SPACE
    int subByteFrequency(int start, int end);
    // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
    // For the incorrect value of START or END, the behavior is undefined.
*/

/*
public class Frequencer implements FrequencerInterface{
    // Code to Test, *warning: This code  contains intentional problem*
    byte [] myTarget;
    byte [] mySpace;
    
    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) { mySpace = space; }
    
    public int frequency() {
        // 各値が設定されていない場合
        if(myTarget == null) {
            return -1;
        }
        if(mySpace == null) {
            return 0;
        }
        
        int targetLength = myTarget.length;
        int spaceLength = mySpace.length;
        int count = 0;
        
        // TAGETの長さが0の場合
        if(targetLength == 0) {
            return -1;
        }
        
        for(int start = 0; start <= (spaceLength - targetLength); start++) { // Is it OK?
            boolean abort = false;
            for(int i = 0; i < targetLength; i++) {
                if(myTarget[i] != mySpace[start+i]) {
                    abort = true;
                    break;
                }
            }
            if(abort == false) {
                count++;
                start += (targetLength - 1);
            }
        }
        return count;
    }

    // I know that here is a potential problem in the declaration.
    public int subByteFrequency(int start, int length) { 
	// Not yet, but it is not currently used by anyone.
	return -1;
    }

    public static void main(String[] args) {
	Frequencer myObject;
	int freq;
	try {
	    System.out.println("checking my Frequencer");
	    myObject = new Frequencer();
	    myObject.setSpace("Hi Ho Hi Ho".getBytes());
	    myObject.setTarget("H".getBytes());
	    freq = myObject.frequency();
	    System.out.print("\"H\" in \"Hi Ho Hi Ho\" appears "+freq+" times. ");
	    if(4 == freq) { System.out.println("OK"); } else {System.out.println("WRONG"); }
	}
	catch(Exception e) {
	    System.out.println("Exception occurred: STOP");
	}
    }
}
*/
//演習3&4
public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace.
    // The following is the code to print the variable
    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                //    System.out.println(suffixArray[i]);
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }
    private int suffixCompare(int i, int j) {
        // comparing two suffixes by dictionary order.
        // i and j denoetes suffix_i, and suffix_j
        // if suffix_i > suffix_j, it returns 1
        // if suffix_i < suffix_j, it returns -1
        // if suffix_i = suffix_j, it returns 0;
        // It is not implemented yet,
        // It should be used to create suffix array.
        // Example of dictionary order
        // "i" < "o" : compare by code
        // "Hi" < "Ho" ; if head is same, compare the next element
        // "Ho" < "Ho " ; if the prefix is identical, longer string is big
        
        /*
         int si = suffixArray[i];
         int sj = suffixArray[j];
         
         //比較用の配列を作成する
         byte compare1[] = new byte[mySpace.length-si];
         int count = 0;
         for(int x = si;x<mySpace.length;x++){
         compare1[count] = mySpace[x];
         count++;
         }
         
         byte compare2[] = new byte[mySpace.length-sj];
         count = 0;
         for(int y = sj;y<mySpace.length;y++){
         compare2[count] = mySpace[y];
         count++;
         }
         
         count =0;
         //配列を比較する
         while(true){
         if(compare1[count]==compare2[count]){
         if(count+1 > compare1.length && count+1 > compare1.length){//1と2が同じなら0を返す
         return  0;
         }
         }
         else if(compare1[count]>compare2[count]){//iがjより大きかったら1を返す
         return 1;
         }else{//それ以外は-1を返す
         return -1;
         }
         count ++;
         }
         */
        
        // 指導書より
        int si = suffixArray[i];
        int sj = suffixArray[j];
        int s = 0;
        if(si > s) s= si;
        if(sj > s) s= sj;
        int n = mySpace.length - s;
        for(int k=0;k<n;k++) {
            if(mySpace[si+k]>mySpace[sj+k]) return 1;
            if(mySpace[si+k]<mySpace[sj+k]) return -1;
        }
        // 接頭辞が等しいので、文字列の長さ（インデックス）で大小比較
        if(si < sj) return 1;
        if(si > sj) return -1;
        return 0;
    }
    
    public void setSpace(byte []space) {
        mySpace = space;
        if(mySpace.length>0) spaceReady = true;
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        for(int k = 0; k< space.length; k++) {
            suffixArray[k] = k;
        }
        // Sorting is not implmented yet.
        // ここでバブルソートを実行
        for(int i = 0; i< space.length; i++){
            for(int j = i+1; j < space.length; j++){
                int tmp;
                int flag = suffixCompare(i,j);
                if(flag == 1){
                    tmp = suffixArray[i];
                    suffixArray[i] = suffixArray[j];
                    suffixArray[j] = tmp;
                }
            }
        }
        
        printSuffixArray();
        
        /* Example from "Hi Ho Hi Ho"
         0: Hi Ho
         1: Ho
         2: Ho Hi Ho
         3:Hi Ho
         4:Hi Ho Hi Ho
         5:Ho
         6:Ho Hi Ho
         7:i Ho
         8:i Ho Hi Ho
         9:o
         A:o Hi Ho
         */
    }
    
    private int targetCompare(int i, int start, int end) {
        // comparing suffix_i and target_j_end by dictonary order with limitation of length;
        // if the beginning of suffix_i matches target_i_end, and suffix is longer than target it returns 0;
        // if suffix_i > target_start_end it return 1;
        // if suffix_i < target_start_end it return -1
        // It is not implemented yet.
        // It should be used to search the apropriate index of some suffix.
        // Example of search
        // suffix target
        // "o" > "i"
        // "o" < "z"
        // "o" = "o"
        // "o" < "oo"
        // "Ho" > "Hi"
        // "Ho" < "Hz"
        // "Ho" = "Ho"
        // "Ho" < "Ho " : "Ho " is not in the head of suffix "Ho"
        // "Ho" = "H" : "H" is in the head of suffix "Ho"
        
        int si = suffixArray[i];
        int s = 0;
        if(si > s) s = si;
        int n = mySpace.length - s;
        
        // suffix_iの文字列の長さがtargetの文字列の長さより長い場合、比較回数をtargetの長さにする
        if(n > end) n = end;
        
        // 接頭辞の大きさを比較
        for(int k=0; k<n; k++) {
            if(mySpace[si+k] > myTarget[k]) return 1;
            if(mySpace[si+k] < myTarget[k]) return -1;
        }
        
        // 接頭辞が等しいので、文字列の長さで大小比較
        int spaLeng = mySpace.length - s; //suffixの文字列の長さ
        int tarLeng = end; // targetの文字列の長さ、myTarget.length
        
        /* デバッグ用 */
        //System.out.println("Index:"+i);
        //System.out.println("suffix:"+spaLeng);
        //System.out.println("target:"+tarLeng);
        
        if(spaLeng < tarLeng) return -1; // targetの方が長い場合
        
        // 上のどの条件にも該当しなかったので、targetは接頭辞であると言える
        return 0;
    }
    
    private int subByteStartIndex(int start, int end) {
        // It returns the index of the first suffix which is equal or greater than subBytes;
        // not implemented yet;
        // For "Ho", it will return 5 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 6 for "Hi Ho Hi Ho".
        
        int sl = suffixArray.length;
        int result = -1;
        
        for(int i = 0; i < sl; i++){
            result = targetCompare(i,start,end);
            // suffix_iとtargetが初めて等しくなった時のインデックスを返す
            //System.out.println("スタートコンペア"+result); //デバッグ用
            if(result == 0) return i;
        }
        // suffix_iとtargetが一度も等しくならなかった
        return suffixArray.length;
    }
    
    private int subByteEndIndex(int start, int end) {
        // It returns the next index of the first suffix which is greater than subBytes;
        // not implemented yet
        // For "Ho", it will return 7 for "Hi Ho Hi Ho".
        // For "Ho ", it will return 7 for "Hi Ho Hi Ho".
        
        int sl = suffixArray.length;
        int result = 0;
        
        for(int i = 0; i < sl; i++){
            result = targetCompare(i,start,end);
            // suffix_iが初めてtargetより大きくなった時のインデックスを返す
            //System.out.println("エンドコンペア"+result); //デバッグ用
            if(result == 1) return i;
        }
        // suffix_iが一度もtargetより大きくならなかった
        return suffixArray.length;
    }
    
    //検索に引っかかった要素の数を返す
    public int subByteFrequency(int start, int end) {
        //This method could be defined as follows though it is slow.
        int spaceLength = mySpace.length;
        int count = 0;
        for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i< (end - start); i++) {
                if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) {
                count++;
            }
        }
        int first = subByteStartIndex(start,end);
        int last1 = subByteEndIndex(start, end);
        //inspection code
        for(int k=start;k<end;k++) {
            System.out.write(myTarget[k]);
        }
        System.out.printf(": first=%d last1=%d\n", first, last1);
        return last1 - first;
    }
    
    public void setTarget(byte [] target) {
        myTarget = target;
        if(myTarget.length>0) targetReady = true;
    }
    
    //条件がそろっていたらオブジェクトを作成
    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }
    
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.setTarget("H".getBytes());
            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) {
                System.out.println("OK");
            }else{
                System.out.println("WRONG");
            }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}
