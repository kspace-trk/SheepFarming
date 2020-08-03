
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

        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        /*
         //　行動例１　ランダムに走る例
         action = "run:" + (int) (Math.random() * 360);
         // 行動例１　の　終わり
         */
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //
        ///////////////////////////////////////
        //
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        /*
         //　行動例２　必ず羊の群れの重心に向かう場合
         double gx = 0, gy = 0;
         for (int i = 0; i < numSheep; i++) {
         gx += myLocX + sheepDistance[i] * Math.cos(sheepAngle[i] * Math.PI / 180.);
         gy += myLocY + sheepDistance[i] * Math.sin(sheepAngle[i] * Math.PI / 180.);
         }
         gx /= numSheep;
         gy /= numSheep;

         double tx = gx - myLocX;
         double ty = gy - myLocY;
         double ta = Math.atan2(ty, tx) * 180. / Math.PI;
         if (ta < 0) {
         ta += 360.;
         }
         action = "run:" + (int) (ta);
         // 行動例２　の　終わり
         */
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //
        ///////////////////////////////////////
        //
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        /*
        //行動例３　牧羊犬が右回りに四角形を描いて動く例
        if (0 <= myLocX && myLocX <= 200 && myLocY <= 600) {
            action = "run:" + 90;
        } else if (500 < myLocX && myLocX <= 700 && myLocY >= 200) {
            action = "run:" + 270;
        } else if (0 <= myLocY && myLocY <= 200 && myLocX >= 200) {
            action = "run:" + 180;
        } else if (700 <= myLocY && myLocY <= 800 && myLocX <= 500) {
            action = "run:" + 0;
        } else {
            action = "run:" + 0;
        }
        //行動例３　の終わり
         */
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //
        ///////////////////////////////////////
        //
        //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        /*
        //　行動例４　牧羊犬が、自分の番号に応じて別の行動をとる例
        if (this.getMyNum() == 0) {
            //　牧羊犬が０番なら、左回りに四角形を描いて動く
            if (0 <= myLocX && myLocX <= 200 && myLocY >= 200) {
                action = "run:" + 270;
            } else if (500 < myLocX && myLocX <= 700 && myLocY <= 600) {
                action = "run:" + 90;
            } else if (0 <= myLocY && myLocY <= 200 && myLocX <= 500) {
                action = "run:" + 0;
            } else if (600 <= myLocY && myLocY <= 800 && myLocX >= 200) {
                action = "run:" + 180;
            } else {
                action = "run:" + 0;
            }
        } else if (this.getMyNum() == 1) {
            //　牧羊犬が１番なら、右回りに四角形を描いて動く

            if (0 <= myLocX && myLocX <= 200 && myLocY <= 600) {
                action = "run:" + 90;
            } else if (500 < myLocX && myLocX <= 700 && myLocY >= 200) {
                action = "run:" + 270;
            } else if (0 <= myLocY && myLocY <= 200 && myLocX >= 200) {
                action = "run:" + 180;
            } else if (600 <= myLocY && myLocY <= 800 && myLocX <= 00) {
                action = "run:" + 0;
            } else {
                action = "run:" + 0;
            }
        } else {
            action = "rest";
        }
        //行動例４　の終わり
        */
        
        
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 工夫例 (1)
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        // 自分の犬番号を受け取る
        int myNum = this.getMyNum();
        //  羊の重心座標取得 (gx, gy)
        double gx = 0, gy = 0;
         for (int i = 0; i < numSheep; i++) {
         gx += myLocX + sheepDistance[i] * Math.cos(sheepAngle[i] * Math.PI / 180.);
         gy += myLocY + sheepDistance[i] * Math.sin(sheepAngle[i] * Math.PI / 180.);
         }
         gx /= numSheep;
         gy /= numSheep;
         double dogStart = gy - (550 - gy);
         //
        if (step < 50) {
            //最初の50ステップで、左辺（180度方向）に「走る」。
            action = "run:180";
        } else if (step < 100) {
            //次に、100ステップまでの間に、出発点に移動する。
            double angle = 0;
            switch (myNum) {
                case 0:
                    // 犬０は、座標 (50, dogStart+100)へ
                    angle = (Math.atan2(dogStart + 100 - myLocY, 50 - myLocX)) * 180. / Math.PI;
                    break;
                case 1:
                    // 犬１は、座標 (50, dogStart)へ
                    angle = (Math.atan2(dogStart - myLocY, 50 - myLocX)) * 180. / Math.PI;
                    break;
                case 2:
                    // 犬２は、座標 (50, dogStart-100)へ
                    angle = (Math.atan2(dogStart - 100 - myLocY, 50 - myLocX)) * 180. / Math.PI;
                    break;
                default:
            }
            action = "run:" + (int) angle;
        } else {
            // step が100以降では、
            //　各犬の目標地点に移動する。
            //　ただし、ゆっくりと移動する　move
            //　また、一番近い羊に近づきすぎたら休む　rest
            //  成功時 367
            action = "rest";
            double angle = 0;
            switch (myNum) {
                case 0:
                    // 犬０は、座標 (670, 750)へ
                    angle = (Math.atan2(750 - myLocY, 670 - myLocX)) * 180. / Math.PI;
                    break;
                case 1:
                    // 犬１は、座標 (670, 400)へ
                    angle = (Math.atan2(400 - myLocY, 670 - myLocX)) * 180. / Math.PI;
                    break;
                case 2:
                    // 犬２は、座標 (670, 300)へ
                    angle = (Math.atan2(300 - myLocY, 670 - myLocX)) * 180. / Math.PI;
                    break;
                default:
            }
            // 一番近い羊に200ドット以内に近づいたら休む。そうでなければ目標方向に動く。
            if (sheepDistance[0] > 200) {
                action = "move:" + (int) angle;
            } else {
                action = "rest";
            }
        }
        
        return action;
    }
}
