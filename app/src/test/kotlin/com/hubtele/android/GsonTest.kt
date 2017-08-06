package com.hubtele.android

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.hubtele.android.helper.DateFormatter
import com.hubtele.android.model.ProgramInfo
import com.hubtele.android.model.ProgramTable
import org.junit.Test

import org.junit.Assert.*

import java.lang.reflect.Type
import java.util.*

/**
 * Created by nakama on 2015/10/25.
 */
class GsonTest {

    @Test
    fun normalGson() {
        val gson = GsonBuilder().setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'").create()

        val programTableType = object : TypeToken<ProgramTable>() {
        }.type
        val programTable = gson.fromJson<ProgramTable>(programTableStr, programTableType)
        println("gson to json:" + gson.toJson(programTable))
        assertTrue("contains 8 station", programTable.size == 8)
        assertTrue("01 station name is nhksougou", programTable["01"]?.name == "1.NHK総合")
        assertTrue("01 program id is 01201510251508", programTable["01"]?.program?.id == "01201510251508")
        assertTrue("01 program start", programTable["01"]?.program?.startAt?.time ==
                DateFormatter.fromIsoFormat("2015-10-25T06:08:00.000Z").time)
        assertTrue("01 program end", programTable["01"]?.program?.endAt?.time ==
                DateFormatter.fromIsoFormat("2015-10-25T07:00:00.000Z").time)
    }
    //\"entryCount\":353,\"start_at\":\"2015-10-25T06:08:00.000Z\",\"end_at\":\"2015-10-25T07:00:00.000Z\"

    private val programTableStr = "{\"01\":{\"name\":\"1.NHK総合\",\"program\":{\"_id\":\"562c472db9d577fd776c913b\",\"station\":\"01\",\"id\":\"01201510251508\",\"title\":\"競馬「第76回菊花賞」（発走　3：40）　～京都競馬場から中継～　[SS][字]\",\"summary\":\"当日情報満載のNHK競馬中継。3歳馬クラシック最終戦を京都競馬場から生中継。［延伸のとき以降の番組に変更あり］\",\"genre\":[\"101110\",\"101000\"],\"entryCount\":353,\"start_at\":\"2015-10-25T06:08:00.000Z\",\"end_at\":\"2015-10-25T07:00:00.000Z\"},\"entryCount\":0},\"02\":{\"name\":\"2.NHKEテレ1\",\"program\":{\"_id\":\"562c472db9d577fd776c9145\",\"station\":\"02\",\"id\":\"02201510251500\",\"title\":\"第17回全日本学生柔道体重別団体優勝大会～尼崎市・ベイコム総合体育館から中継～\",\"summary\":\"大学日本一の座をかけ、体重別に7人ずつが出場する団体戦。大学によって得意、不得意の階級がある中、1試合ごとの“かけひき”が勝負の行方を左右する。\",\"genre\":[\"101105\",\"101000\"],\"entryCount\":0,\"start_at\":\"2015-10-25T06:00:00.000Z\",\"end_at\":\"2015-10-25T07:00:00.000Z\"},\"entryCount\":0},\"04\":{\"name\":\"4.日テレ\",\"program\":{\"_id\":\"562c472db9d577fd776c914d\",\"station\":\"04\",\"id\":\"04201510251500\",\"title\":\"ブリヂストンオープン2015　最終日～袖ヶ浦カンツリークラブ・袖ヶ浦コース[字][デ]\",\"summary\":\"ドラマチックな16番からのあがり3ホール。名門・袖ヶ浦を制し、賞金王に近づくのは誰か。プロの技が問われる特徴的なコース、強風の袖ヶ浦、混戦必至!\",\"genre\":[\"101103\",\"101000\"],\"entryCount\":2,\"start_at\":\"2015-10-25T06:00:00.000Z\",\"end_at\":\"2015-10-25T07:25:00.000Z\"},\"entryCount\":0},\"05\":{\"name\":\"5.テレビ朝日\",\"program\":{\"_id\":\"562c472db9d577fd776c9157\",\"station\":\"05\",\"id\":\"05201510251525\",\"title\":\"路線バスで寄り道の旅[字]\",\"summary\":\"日頃秒読みスケジュールに疲れ気味の徳さん御一行。のんび～り旅を味わうために選んだのは“路線バス今回は徳さん一人で横浜絶品 秋のグルメ&行楽尽くしの旅!!\",\"genre\":[\"105105\",\"105000\"],\"entryCount\":1,\"start_at\":\"2015-10-25T06:25:00.000Z\",\"end_at\":\"2015-10-25T07:30:00.000Z\"},\"entryCount\":0},\"06\":{\"name\":\"6.TBS\",\"program\":{\"_id\":\"562c472db9d577fd776c915d\",\"station\":\"06\",\"id\":\"06201510251530\",\"title\":\"NOBUTA　GROUP　マスターズGCレディース　最終日[字]\",\"summary\":\"優勝賞金2520万円！賞金女王独走中のイ・ボミ、今季5勝のテレサ・ルー、昨年のこの大会覇者大山志保、日本女子オープン2位の菊地絵里香など、トッププロが目白押し！\",\"genre\":[\"101103\",\"101000\"],\"entryCount\":14,\"start_at\":\"2015-10-25T06:30:00.000Z\",\"end_at\":\"2015-10-25T08:00:00.000Z\"},\"entryCount\":0},\"07\":{\"name\":\"7.テレビ東京\",\"program\":{\"_id\":\"562c472db9d577fd776c9161\",\"station\":\"07\",\"id\":\"07201510251400\",\"title\":\"日曜イベントアワー「信州山岳刑事　道原伝吉3」[再][字][解]\",\"summary\":\"安曇野北警察署の道原伝吉(松平健)が、巨大な密室と化した北アルプス連続殺人を解く!不倫デート中の死と滑落死!被害者のそばにはなぜかリンドウの花が…その意味は!?\",\"genre\":[\"103100\",\"103000\"],\"entryCount\":1,\"start_at\":\"2015-10-25T05:00:00.000Z\",\"end_at\":\"2015-10-25T06:55:00.000Z\"},\"entryCount\":0},\"08\":{\"name\":\"8.フジテレビ\",\"program\":{\"_id\":\"562c472db9d577fd776c916c\",\"station\":\"08\",\"id\":\"08201510251500\",\"title\":\"みんなのKEIBA\",\"summary\":\"3冠最終戦「菊花賞・GI」2冠ドゥラメンテ不在で混戦!前哨戦Vリアファルvs北島三郎愛馬キタサンブラックvs皐月2着リアルスティール▽大島麻衣連勝は?\",\"genre\":[\"101110\",\"101000\"],\"entryCount\":14,\"start_at\":\"2015-10-25T06:00:00.000Z\",\"end_at\":\"2015-10-25T07:00:00.000Z\"},\"entryCount\":0},\"09\":{\"name\":\"9.TOKYO MX1\",\"program\":{\"_id\":\"562c472db9d577fd776c9177\",\"station\":\"09\",\"id\":\"09201510251530\",\"title\":\"MXショッピング\",\"summary\":\"リポコラージュラメラエッセンス　【化粧品】\",\"genre\":[\"102104\",\"102000\"],\"entryCount\":0,\"start_at\":\"2015-10-25T06:30:00.000Z\",\"end_at\":\"2015-10-25T06:59:00.000Z\"},\"entryCount\":0}}"
}
