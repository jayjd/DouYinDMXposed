package com.clj.dydmxp.music;

import android.os.Build;
import android.os.Handler;
import android.widget.SeekBar;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MusicYYY {

    // 获取音乐id数组
    public static long geMusicIdArray(String musicName) {

        String jsonStr = null;
        System.out.println(musicName);
        try {
            String str = URLEncoder.encode(musicName, "utf-8");
            // 转换成encode
            URL url = new URL("http://music.163.com/api/search/pc?s=" + str + "&type=1&limit=50&offset=0");
            // 拼接url
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setConnectTimeout(3000);
            httpCon.setDoInput(true);
            httpCon.setRequestMethod("GET");
            // 获取相应码
            int respCode = httpCon.getResponseCode();
            if (respCode == 200) {
                // ByteArrayOutputStream相当于内存输出流
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                // 将输入流转移到内存输出流中
                while ((len = httpCon.getInputStream().read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                // 将内存流转换为字符串
                jsonStr = new String(out.toByteArray(), "utf-8");
//				System.out.println(jsonStr);
                JSONObject js = JSONObject.parseObject(jsonStr);
                String result = js.getString("result");
                if (result == null || result.length() == 0) {
                    System.out.println("错误：" + jsonStr);
                    return 0;
                }
                String songs = JSONObject.parseObject(result).getString("songs");
                JSONArray jsonArray = JSONArray.parseArray(songs);
                // jsonArray 内就是歌曲信息了
                // 之后可以用jsonArray.getJSONObject(0).getString(/*这里放你要获取的字段名如id*/)获取详细信息

                // id = new String[10];
                String gequid = "0";
                for (int i = 0; i < jsonArray.size(); i++) {
                    String gequming = jsonArray.getJSONObject(i).getString("name");
                    String geshou = jsonArray.getJSONObject(i).getJSONArray("artists").getJSONObject(0)
                            .getString("name");
//					String gequ = musicName.replace(" ", "").split("-")[0];
//					String geshou1 = musicName.replace(" ", "").split("-")[1];
                    String gequ = musicName.split(" ")[0];
                    String geshou1 = "";
                    if (musicName.split(" ").length == 2) {
                        geshou1 = musicName.split(" ")[1];
                        if (gequ.contains(gequming) && geshou1.contains(geshou)) {
                            gequid = jsonArray.getJSONObject(i).getString("id");
                            String mp3url = jsonArray.getJSONObject(i).getString("mp3Url");
                            System.out.println("歌曲信息：" + gequming + "。。" + geshou + ".." + gequid);
                            break;
                        }
                    }
                }
                if (!gequid.equals("0")) {
                    return Long.parseLong(gequid);
                } else {
                    System.out.println("未匹配到歌曲");
                    return 0;
                }
            } else {
                System.out.println("网易云错误，错误码为" + respCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }// TODO Auto-generated catch block

        return 0;
    }

    // 获取歌曲链接
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void getCloundMusicMp3(long gequid, Handler handler, SeekBar musicSeekBar) throws IOException {
        // 方法1:
        String MP3url = "http://music.163.com/song/media/outer/url?id=" + gequid;
        System.out.println(MP3url);
        Connection.Response re = (Connection.Response) Jsoup.connect(MP3url).followRedirects(false).execute();

        String mp3url = re.header("location");
        if (mp3url.lastIndexOf("404") != -1) {
            System.out.println(mp3url);
            System.out.println("没有版权");
        } else {
            System.out.println("可以播放:真是地址是" + mp3url);
            MusicYYYMedia musicYYYMedia = new MusicYYYMedia(handler, musicSeekBar);
            musicYYYMedia.initMedia();
            musicYYYMedia.setMediaPlayer(mp3url);
        }
//        // 方法2:
//        // 私钥，随机16位字符串（自己可改）
//        String secKey = "cd859f54539b24d7";
//        String text = "{\"ids\": \"[" + gequid
//                + "]\", \"level\": \"standard\", \"encodeType\": \"aac\"}";
//        String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";
//        String nonce = "0CoJUm6Qyw8W8jud";
//        String pubKey = "010001";
//        // 2次AES加密，得到params
//        try {
//            String params = EncryptTools.encrypt(EncryptTools.encrypt(text, nonce), secKey);
//            StringBuffer stringBuffer = new StringBuffer(secKey);
//            // 逆置私钥
//            secKey = stringBuffer.reverse().toString();
//            String hex = EncryptTools.byte2HexString(secKey.getBytes());
//            BigInteger bigInteger1 = new BigInteger(hex, 16);
//            BigInteger bigInteger2 = new BigInteger(pubKey, 16);
//            BigInteger bigInteger3 = new BigInteger(modulus, 16);
//            // RSA加密计算
//            BigInteger bigInteger4 = bigInteger1.pow(bigInteger2.intValue()).remainder(bigInteger3);
//            String encSecKey = EncryptTools.byte2HexString(bigInteger4.toByteArray());
//            // 字符填充
//            encSecKey = EncryptTools.zfill(encSecKey, 256);
//            // 评论获取
//            Map<String, String> map = new HashMap<>();
////            map.put("MUSIC_U", token);
//            map.put("__remember_me", "true");
////            map.put("__csrf", uuid);
//            Document document = Jsoup
//                    .connect("https://music.163.com/weapi/song/enhance/player/url/v1")
//                    .cookie("appver", "1.5.0.75771").header("Referer", "https://music.163.com").data("params", params)
//                    .data("encSecKey", encSecKey).cookies(map).ignoreContentType(true).post();
////		System.out.println("歌曲信息:" + document.text());
//            JSONObject js = (JSONObject) JSONObject.parse(document.text());
//            if (js.getString("code").equals("200")) {
//                // 获取歌曲信息
//                JSONObject songInfo = js.getJSONArray("data").getJSONObject(0);
//                if (songInfo.getString("code").equals("200")) {
//                    String songType = songInfo.getString("type");
//                    String songUrl = songInfo.getString("url");
//                    long songSize = songInfo.getLong("size");
//                    System.out.println("歌曲格式:" + songType + "\n歌曲链接：" + songUrl);
//                    if (songType.equals("FLAC")) {
//                        System.out.println("歌曲地址：" + songUrl);
//                    } else {
//                        System.out.println("歌曲地址：" + songUrl);
//                    }
//                } else {
//                    System.out.println("歌曲链接获取失败，错误码：" + songInfo.getString("code"));
//                }
//
//            } else {
//                System.out.println("歌曲链接获取失败，错误码：" + js.getString("code"));
//            }
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

    }
}
