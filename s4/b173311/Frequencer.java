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
//suffixarray無しバージョン
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

//演習3&4にて追加したコード
public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    // Code with start：このコードは機能しませんが、良いスタートポイントがあります。
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;
    int [] suffixArray;
    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.
    // Each suffix is expressed by a interger, which is the starting position in mySpace.
    // The following is the code to print the variable
    // 変数 "suffixArray"は、mySpaceのすべての接尾辞のソートされた配列です。
    // 各接尾辞はintergerで表され、これはmySpaceの開始位置です。
    // 以下は変数を出力するコードです
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
    
    // suffixを表示
    private void printMySpace(int num) {
        int i = suffixArray[num];
        System.out.print(num+":");
        for(int j=i; j<mySpace.length; j++) {
            System.out.write(mySpace[j]);
        }
        System.out.write('\n');
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
        
        // 辞書順で2つの接尾辞を比較する。
        // iとjはsuffix_iとsuffix_jをデノートします
        // suffix_i > suffix_jの場合、1を返します
        // suffix_i < suffix_jの場合、-1を返します。
        // suffix_i = suffix_jの場合、0を返します。
        // まだ実装されていませんが、
        // 接尾辞配列の作成に使用する必要があります。
        // 辞書順の例
        // "i" <"o"：コードで比較する
        // "Hi" <"Ho"; headが同じなら、次の要素を比較する
        // "Ho" <"Ho"; 接頭辞が同一であれば、長い文字列が大きい
        
        /*
         //以下、途中で諦めたコード
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
            if(compare1[count]==compare2[count]) {
                if(count+1 > compare1.length && count+1 > compare1.length) { //1と2が同じなら0を返す
                    return  0;
                }
            } else if(compare1[count]>compare2[count]) { //iがjより大きかったら1を返す
                return 1;
            } else {//それ以外は-1を返す
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
            if(mySpace[si+k] > mySpace[sj+k]) return 1; //suffix_i > suffix_j
            if(mySpace[si+k] < mySpace[sj+k]) return -1; //suffix_i < suffix_j
        }
        // 接頭辞が等しいので、文字列の長さ（インデックス）で大小比較
        if(si < sj) return 1; //suffix_i > suffix_j
        if(si > sj) return -1; //suffix_i < suffix_j
        return 0; //suffix_i = suffix_j
    }
    
    /** クイックソート用 **/
    /* mySpaceの中身を取得 */
    private byte[] getMySpace(int num) {
        // suffixArrayで指定したmySpaceから中身を取得し、配列に格納する
        int i = suffixArray[num];
        int k = 0;
        byte[] contents = new byte[mySpace.length-i];
        for(int j=i; j<mySpace.length; j++) {
            contents[k] = mySpace[j];
            k++;
        }
        return contents;
    }
    
    /* ピボットとmySpaceを比較 */
    private int suffixComparePivot(int i, byte []j) {
        // suffixCompareを特定のsuffixと比較するように変更したもの
        // 返り値はsuffixCompareと同じ
        int si = suffixArray[i];
        int sj = mySpace.length - j.length;
        int s = 0;
        if(si > s) s= si;
        if(sj > s) s= sj;
        int n = mySpace.length - s;
        
        for(int k=0;k<n;k++) {
            if(mySpace[si+k] > j[k]) return 1; //suffix_i > suffix_j
            if(mySpace[si+k] < j[k]) return -1; //suffix_i < suffix_j
        }
        // 接頭辞が等しいので、文字列の長さ（インデックス）で大小比較
        if(si < sj) return 1; //suffix_i > suffix_j
        if(si > sj) return -1; //suffix_i < suffix_j
        return 0; //suffix_i = suffix_j
    }
    
    /* ピボット選択 */
    private int pivot(int i,int j){
        // iからjの範囲で先頭と次の異なる値を比較し、大きい方を返す
        int k = i+1;
        while(k<=j && suffixCompare(i,k)==0) k++; //先頭と異なる値を探す
        if(k > j) return -1; // iからjまでの範囲の値が全て同じ値だった場合
        if(suffixCompare(i,k)==0 || suffixCompare(i,k)==1) {
            return i; //suffix_i >= suffix_k の場合
        } else {
            return k;
        }
    }
    
    /* パーティション分割 */
    private int partition(int i,int j,int x){
        // iからjまでの範囲を軸要素xでソートし分割
        int l=i;
        int r=j;
        byte []pivot = getMySpace(x); //軸要素の取得
        
        /*
        //渡されたピボットを表示（デバッグ用）
        printMySpace(x);
        //取得したピボットの配列を表示（デバッグ用）
        System.out.print("pivot[]:");
        for(int poi=0; poi<pivot.length; poi++) {
            System.out.write(pivot[poi]);
        }
        System.out.write('\n');
        */
        
        // 検索が交差するまで繰り返す
        while(l<=r){
            
            // 軸要素以上のデータを探す
            while(l<=j && suffixComparePivot(l,pivot)==-1)  { //l<=j && suffix_r < suffix_x
                l++;
            }
            
            // 軸要素未満のデータを探す
            while(r>=i && ( suffixComparePivot(r,pivot)==1 || suffixComparePivot(r,pivot)==0 )) { //r>=i && suffix_r >= suffix_x
                r--;
            }
            
            // 探索位置が交差しているので終了
            if(l>r) break;
            
            // 値の入れ替え
            int temp = suffixArray[l];
            suffixArray[l] = suffixArray[r];
            suffixArray[r] = temp;
            
            //探索位置を進める
            l++;
            r--;
        }
        //System.out.println("RETURN_l:"+l); //返り値を表示
        return l;
    }
    
    /* クイックソート */
    private void quickSort(int i,int j){
        if(i==j) return;
        int p = pivot(i,j); //軸を求める
        /*
        // ピボットを表示
        System.out.print("ピボット:");
        printMySpace(p);
        System.out.println();
        */
        if(p != -1){
            int k = partition(i,j,p); //分割する
            /*
            // パーティション結果を表示
            System.out.print("パーティション:"); //パーティションを表示
            printMySpace(k);
            System.out.println("パーティション後のarray");
            printSuffixArray();
            */
            // 分割された左側と右側を再帰的にソートする
            quickSort(i,k-1); //左側
            quickSort(k,j); //右側
        }
    }
    /** クイックソート終わり **/

    public void setSpace(byte []space) {
        spaceReady = false; //フラグの初期化
        mySpace = space;
        if(mySpace.length > 0) {
            spaceReady = true;
        } else { //Spaceの長さが0 or 設定されていない場合は何もせずに終わる
            return;
        }
        
        /* デバッグ用 */
        //System.out.println("spaceReady = "+spaceReady); //フラグの確認
        //System.out.println("mySpace.length = "+mySpace.length); //判定条件に用いられる値を確認
        
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray. Each suffix is expressed by one interger.
        // すべての接尾辞を接尾辞Arrayに入れます。 各接尾辞は整数で表されます。
        for(int k=0; k<space.length; k++) {
            suffixArray[k] = k;
        }
        
        /*
        // ソート前のsuffixarrayを表示
        System.out.println("suffixarray is not sorting");
        printSuffixArray(); //suffixarrayの表示
        */
        
        /*
        // 比較用（デバッグ用）
        int test1 = 3; //比較するsuffix
        int test2 = 1; //比較するsuffix
        int test = suffixCompare(test1,test2);
        System.out.println();
        printMySpace(test1);
        printMySpace(test2);
        System.out.println("比較結果:"+test);
        System.out.println();
        */
        
        // クイックソートを実行
        quickSort(0,suffixArray.length-1);
        
        /*
        // バブルソートを実行
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
        */
        
        /*
        // ソート後のsuffixarrayを表示
        System.out.println("suffixarray is sorting");
        printSuffixArray(); //suffixarrayの表示
        */
        
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
        
        // 上のどの条件にも該当しなかったので、target=接頭辞であると言える
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
        /*
        //This method could be defined as follows though it is slow.
        //このメソッドは遅いですが、以下のように定義することができます。
        int spaceLength = mySpace.length;
        int count = 0;
        for(int offset = 0; offset < spaceLength - (end - start); offset++) {
            boolean abort = false;
            for(int i = 0; i < (end - start); i++) { //ターゲットの長さ分だけ繰り返す
                if(myTarget[start+i] != mySpace[offset+i]) { //ターゲットと文字列の比較
                    abort = true;
                    break;
                }
            }
            if(abort == false) {
                count++;
                offset += (end - start) -1;
            }
        }
        */
        
        int first = subByteStartIndex(start,end);
        int last1 = subByteEndIndex(start, end);
        
        /*
        //inspection code
        //検査コード
        for(int k=start;k<end;k++) {
            System.out.write(myTarget[k]);
        }
        System.out.printf(": first=%d last1=%d\n", first, last1);
        */
        
        /*
        if( count < (last1 - first)){
            return count;
        } else {
            return last1 - first;
        }
         */
        
        return last1 - first;
        
    }
    
    public void setTarget(byte [] target) {
        targetReady = false; //フラグを初期化
        myTarget = target;
        if(myTarget.length > 0) targetReady = true;
        
        /* デバック用 */
        //System.out.println("targetReady = "+targetReady); //フラグの確認
        //System.out.println("myTarget.length = "+myTarget.length); //判定条件に用いられる値を確認
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
