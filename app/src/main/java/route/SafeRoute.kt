package route

import android.util.Log
import utils.Constant
import utils.Constant.API.LOG
import utils.Constant.API.SAFEROUTE

//roadtype+facilityType이랑 turntype중 하나만 옴
object SafeRoute {
    infix fun Int.fdiv(i: Int): Double = this / i.toDouble();
    fun calcPartialScore(
        facilityType: Int?,
        distance: Int?,
        roadType: Int?,
        turnType: Int?
    ): Double {
        var safty: Double = 100.0
        var dist = distance?: 0


        // case: Point

        if (turnType != null) {
            //회전정보
            when (turnType) {
                in 1..7, 11 -> {
                    safty += 10
                    Log.d(SAFEROUTE, "+10/TT/안내없음또는 직진" );
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );

                }
                //안내없음, 직진
                in 12..19, 218 -> {
                    safty -= 10
                    Log.d(SAFEROUTE, "-10/TT/분기점 또는 엘리베이터" );
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                //분기점, 엘리베이터
                in 125..129 -> {
                    //육교, 지하보도, 계단진입, 경사로진입, +
                    //facility에서 계산
                    Log.d(SAFEROUTE, "0/TT/facility관련" );
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                in 211..217 -> {
                    //횡단보도, facility 에서 계산
                    Log.d(SAFEROUTE, "0/TT/횡단보도" );
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                else -> {

                }
            }
        }

        if (facilityType != null) {

            //구간 시설물 정보
            when (facilityType) {
                1, 2, 3 -> {
                    safty -= 30
                    Log.d(SAFEROUTE, "-30/FT/교량,터널,고가도로");
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                //교량, 터널, 고가도로
                12, 14, 17 -> {
                    safty -= 20
                    Log.d(SAFEROUTE, "-20/FT/육교, 지하보도, 계단");
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                //육교, 지하보도, 계단
                16 -> {
                    safty -= 100
                    Log.d(SAFEROUTE, "-100/FT/대형시설물이동통로");
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }

                //대형 시설물 이동 통로
                15 -> //횡단보도
                {
                    safty -= (dist * 10)
                    Log.d(SAFEROUTE, "횡단보도 * " + "${dist}");
                    Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                }
                //distance: 구간거리 (단위:m)
            }
        }


            if (roadType != null) {
                //도로타입정보
                when (roadType) {
                    21 -> {
                        safty += (dist * 5)
                        Log.d(SAFEROUTE, "5/RT/21번도로 *  "+ "${dist}")
                        Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                    }
                    //차도와 인도가 분리, 정해진 횡단구역으로만 횡단 가능
                    22 -> {
                        safty += (dist * 1)
                        Log.d(SAFEROUTE, "1/RT/22번도로 *  " + "${dist}")
                        Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                    }
                    //차도 인도 분리 X || 보행자 횡단에 제약 X 보행자도로
                    23 -> {
                        safty += (dist * 10)
                        Log.d(SAFEROUTE, "10/RT/23번도로 *  " + "${dist}")
                        Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                    }

                    //차량 통행 불가 보행자 도로
                    24 -> {
                        safty -= (dist * .5)
                        Log.d(SAFEROUTE, "0.5/RT/24번도로 *  " + "${dist}")
                        Log.d(SAFEROUTE, "누적점수"+"${safty}" );
                    }
                    //쾌적 X 도로
                    else -> {
                    }
                }

        }
        return safty
    }
}








