package com.coding.girl.bean

/**
 * @author: Coding.He
 * @date: 2020/6/16
 * @emil: 229101253@qq.com
 * @des:
 */
class GirlPicBean {
    /**
     * counts : 1
     * data : [{"_id":"5e52aa7f8ad0cb82d1976166","author":"鸢媛","category":"Girl","createdAt":"2020-03-08 08:00:00","desc":"人生不过是午后到黄昏的距离，茶凉言尽，月上柳梢。 \u200b\u200b\u200b\u200b","images":["http://gank.io/images/d449165e9f434a60afafa47bd4167d57"],"likeCounts":0,"publishedAt":"2020-03-08 08:00:00","stars":1,"title":"第18期","type":"Girl","url":"http://gank.io/images/d449165e9f434a60afafa47bd4167d57","views":247}]
     * status : 100
     */
    var counts = 0
    var status = 0
    var data: List<DataBean> = arrayListOf()

    class DataBean {
        /**
         * _id : 5e52aa7f8ad0cb82d1976166
         * author : 鸢媛
         * category : Girl
         * createdAt : 2020-03-08 08:00:00
         * desc : 人生不过是午后到黄昏的距离，茶凉言尽，月上柳梢。 ​​​​
         * images : ["http://gank.io/images/d449165e9f434a60afafa47bd4167d57"]
         * likeCounts : 0
         * publishedAt : 2020-03-08 08:00:00
         * stars : 1
         * title : 第18期
         * type : Girl
         * url : http://gank.io/images/d449165e9f434a60afafa47bd4167d57
         * views : 247
         */
        var _id: String = ""
        var author: String = ""
        var category: String = ""
        var createdAt: String = ""
        var desc: String = ""
        var likeCounts = 0
        var publishedAt: String = ""
        var stars = 0
        var title: String = ""
        var type: String = ""
        var url: String = ""
        var views = 0
        var images: List<String> = arrayListOf()
    }
}