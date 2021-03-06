
/**
 * 「コンピュータ・シミュレーション」 2020年用プログラム
 *  牧羊犬プロジェクト ver. 2.0　(c)　Toshiaki Yokoi
 */
public class Dog {

    //　犬の通し番号。犬の識別に使うクラス変数（static変数）。
    public static int sn = 0;
    //
    int mynum;          //自分の犬番号
    int step;           //現在のステップ数
    String action = ""; //自分の行動を代入して返すための変数
    //自由に使える変数
    int m1, m2, m3;
    double v1, v2, v3;
    //前ステップでの行動内容
    String prevAct = "";
    int prevDeg = 0;
    double dogStartX = 0;
    double dogStartY = 0;
    boolean flag = false;
    double dogStatus = 0;
    int dogWaitCount = 0;
    boolean stop = false;
    int nowStep = 0;
    int dogRest = 200;

    //
    // 集団行動を組みむ際に使う可能性のある変数
    public static boolean goForward = false;
    public static boolean[] readyToGo = new boolean[40];
    //

    public Dog() {
        //犬が生成されたときに、通し番号を付加。
        mynum = sn++;
        step = 0;
    }

    public int getMyNum() {
        return mynum;
    }

    public String think(double myLocX, double myLocY, double myTheta,
            Service service,
            int numSheep, double sheepAngle[], double sheepDistance[],
            int numDog, double dogAngle[], double dogDistance[]) {

        // 現在の計算ステップを表す変数を1増やす。
        step++;
        /////////////////////
        // 受け取る情報
        /////////////////////
        //
        //　自分の現在位置の取得
        //  myLocX   : 自分のｘ座標
        //  myLocY   : 自分のｙ座標
        //  myTheta  : 自分の向いている方向
        //   ※　これらの値は、受け取るだけなので、値を変えても移動等はできません。
        //
        //
        //　草原の情報
        //　　サイズは700ドット×800ドット
        //  小屋の情報
        //  　草原の右側情報で、高さ200ドット、幅200ドット
        //
        //
        //　必要な情報を提供するオブジェクト service
        //   （例）「牧羊犬が速く走るときの速さ[dot/step]」
        //     dogFastVelocity = service.getDogFastVelocity();
        //
        //
        // 周囲の「羊」の情報
        //  (1) 羊の数:  numSheep
        //  (2) 羊の「方向」と「距離」が，近い方から順番に，下記の配列の形で与えられる
        //  　　　近い順の「羊の方向(deg)」：　sheepAngle[0]～sheepAngle[numSheep-1]
        //  　　　近い順の「羊の距離(dot)」：　sheepDistance[0]～sheepDistance[numSheep-1]
        //
        // 周囲の「牧羊犬」の情報
        //  (1) 牧羊犬の数:  numDog (自分は除く）
        //  (2) 牧羊犬の「方向」と「距離」が，近い方から順番に，下記の配列の形で与えられる
        //  　　　近い順の「牧羊犬の方向(deg)」：　dogAngle[0]～dogAngle[numDog-1]
        //  　　　近い順の「牧羊犬の距離(dot)」：　dogDistance[0]～dogDistance[numDog-1]
        //
        //
        //　＜＜行動の決定＞＞
        //
        //　下記の（１），（２），（３）のいずれかの行動を文字列で表現し，
        //　それをこのメソッドthink　の戻り値として返す．
        //
        //--------------------------------
        // （１）移動する　書式　"move:方向(deg)"
        //　　　　ただし，方向は右が０(deg)で反時計回りに360(deg)　（実数型で与えること）
        // 　　（例）　action="move:90";
        //--------------------------------
        // （２）走る　　　書式　"run:方向(deg)"
        //　　　　ただし，方向は右が０(deg)で反時計回りに360(deg)　（実数型で与えること）
        // 　　（例）　action="run:30";
        //--------------------------------
        // （３）休む　　　書式　"rest"
        // 　　（例）　action="rest";
        //--------------------------------
        //
        //
        // ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        // ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        // ＝＝＝＝＝  改訂版の　牧羊犬　行動パターンサンプル　　　  　＝＝＝＝＝＝
        // ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
        // ＝＝＝＝＝  行動例のいずれかのブロックの上下のコメント行を　＝＝＝＝＝＝
        // ＝＝＝＝＝　削除して試してください　  　　　　　　　　　　　＝＝＝＝＝＝
        // ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝

        
        //  羊の重心座標取得 (gx, gy)
        double gx = 0, gy = 0;
         gx = 0;
         gy = 0;
         for (int i = 0; i < numSheep; i++) {
         gx += myLocX + sheepDistance[i] * Math.cos(sheepAngle[i] * Math.PI / 180.);
         gy += myLocY + sheepDistance[i] * Math.sin(sheepAngle[i] * Math.PI / 180.);
         }
         gx /= numSheep;
         gy /= numSheep;
         
         
         double x = 700 - gx;
         double y = 575 - gy;

         dogStartX = gx - x/3;
         if(gy >= 575){
            dogStartY = gy + y/3;
         }else{
            dogStartY = gy - y/3;
         }
          
         //
        if (step < 20) {
            //最初の20ステップで、座標 (0, dogStartY)へ
            double angle = 0;
            angle = (Math.atan2(dogStartY - myLocY, 0 - myLocX)) * 180. / Math.PI;
            action = "run:" + (int) angle;
        } else if (step < 35) {
            //次に、35ステップまでの間に、出発点に移動する。
            double angle = 0;

            // 犬は、座標 (godStartX * 0.95, dogStartY * 0.95)へ
            angle = (Math.atan2(dogStartY * 0.95 - myLocY, dogStartX * 0.95 - myLocX)) * 180. / Math.PI;

            action = "run:" + (int) angle;
        } else if(1000 < step && step < 1030){
            double angle = 0;
            //1000stepを超えたら、(0 , dogStartY)に戻る
            angle = (Math.atan2(dogStartY - myLocY, 0 - myLocX)) * 180. / Math.PI;
            action = "run:" + (int) angle;
            System.out.println("1000超えました");
        }
        else {
            // step が35以降では、
            //　羊小屋と羊の重心地点の対角線上を進む
            //　また、一番近い羊に近づきすぎたら休む　rest
            action = "run";
            double angle = 0;

            // 犬は、座標 (dogStartX - 190, dogStartY)へ
            //////////////この部分を、羊の重心を常にチェックして知的な挙動をしたい
            if(!stop){
                if(myLocX > 400){
                    //羊に近づいていく
                    angle = (Math.atan2(dogStartY - myLocY , dogStartX - 190 - myLocX)) * 180./ Math.PI ;
                }else{
                    //羊小屋に近づいてきたら羊とすこし距離を取る
                    angle = (Math.atan2(dogStartY - myLocY , dogStartX - 150 - myLocX)) * 180./ Math.PI ;
                }
            }else if(stop){
                if(step < nowStep){
                    dogRest = 0;
                    if(myLocX <= 700){
                        if(gy < 575){
                            //羊が上にいたら右下に移動
                            angle = (Math.atan2(dogStartY - 600 - myLocY , dogStartX + 300 - myLocX)) * 180./ Math.PI ;
                        }else if(gy >= 575){
                            //羊が下にいたら右上に移動
                            angle = (Math.atan2(dogStartY + 600 - myLocY , dogStartX + 300 - myLocX)) * 180./ Math.PI ;
                        }
                    }else if(myLocX > 700 ){
                        //犬が羊小屋に入っちゃったら脱出する  
                        angle = (Math.atan2(dogStartY - myLocY , 0 - myLocX)) * 180./ Math.PI ;
                    }
                }else{
                    stop = false;
                    dogRest = 200;
                }
            }
            flag = !flag;
            if(flag){
                dogStatus += myLocX;
            }else{
                dogStatus -= myLocX;
                if(-3 < dogStatus && dogStatus < 3){
                    dogWaitCount++;
                    if( dogWaitCount > 40){
                        stop = true;
                        nowStep = step + 30;
                        dogWaitCount = 0;
                    }
                }else{
                    dogWaitCount = 0;
                    dogStatus = 0;
                }
            }

            // 一番近い羊に200ドット以内に近づいたら休む。そうでなければ目標方向に動く。
            if (sheepDistance[0] > dogRest) {
                action = "run:" + (int) angle;
            } else {
                action = "rest";
            }
        }
        
        return action;
    }
}
