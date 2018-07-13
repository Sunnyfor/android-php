package com.cocosh.shmstore.enterpriseCertification.ui.model

/**
 * Created by lmg on 2018/3/27.
 */
class LicenseBean {
    /**
     * log_id : 9092573184062593850
     * words_result_num : 6
     * words_result : {"社会信用代码":{"location":{"width":162,"top":495,"height":23,"left":639},"words":"91Il016WA05615XE"},"单位名称":{"location":{"width":242,"top":542,"height":24,"left":320},"words":"首媒科技(北京)有限公司"},"法人":{"location":{"width":59,"top":641,"height":22,"left":317},"words":"刘厚南"},"证件编号":{"location":{"width":0,"top":0,"height":0,"left":0},"words":"无"},"地址":{"location":{"width":383,"top":608,"height":26,"left":318},"words":"北京市怀柔区桥梓镇兴桥大街1号南楼203室"},"有效期":{"location":{"width":39,"top":741,"height":28,"left":485},"words":"长期"}}
     */

    private var log_id: Long? = 0
    private var words_result_num: Int? = 0
    private var words_result: WordsResultBean? = null

    fun getLog_id(): Long? {
        return log_id
    }

    fun setLog_id(log_id: Long) {
        this.log_id = log_id
    }

    fun getWords_result_num(): Int? {
        return words_result_num
    }

    fun setWords_result_num(words_result_num: Int) {
        this.words_result_num = words_result_num
    }

    fun getWords_result(): WordsResultBean? {
        return words_result
    }

    fun setWords_result(words_result: WordsResultBean) {
        this.words_result = words_result
    }

    class WordsResultBean {
        /**
         * 社会信用代码 : {"location":{"width":162,"top":495,"height":23,"left":639},"words":"91Il016WA05615XE"}
         * 单位名称 : {"location":{"width":242,"top":542,"height":24,"left":320},"words":"首媒科技(北京)有限公司"}
         * 法人 : {"location":{"width":59,"top":641,"height":22,"left":317},"words":"刘厚南"}
         * 证件编号 : {"location":{"width":0,"top":0,"height":0,"left":0},"words":"无"}
         * 地址 : {"location":{"width":383,"top":608,"height":26,"left":318},"words":"北京市怀柔区桥梓镇兴桥大街1号南楼203室"}
         * 有效期 : {"location":{"width":39,"top":741,"height":28,"left":485},"words":"长期"}
         */

        var 社会信用代码: 社会信用代码Bean? = null
        var 单位名称: 单位名称Bean? = null
        var 法人: 法人Bean? = null
        var 证件编号: 证件编号Bean? = null
        var 地址: 地址Bean? = null
        var 有效期: 有效期Bean? = null
        var 成立日期: 成立日期Bean? = null

        class 社会信用代码Bean {
            /**
             * location : {"width":162,"top":495,"height":23,"left":639}
             * words : 91Il016WA05615XE
             */

            var location: LocationBean? = null
            var words: String? = null

            class LocationBean {
                /**
                 * width : 162
                 * top : 495
                 * height : 23
                 * left : 639
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 单位名称Bean {
            /**
             * location : {"width":242,"top":542,"height":24,"left":320}
             * words : 首媒科技(北京)有限公司
             */

            var location: LocationBeanX? = null
            var words: String? = null

            class LocationBeanX {
                /**
                 * width : 242
                 * top : 542
                 * height : 24
                 * left : 320
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 法人Bean {
            /**
             * location : {"width":59,"top":641,"height":22,"left":317}
             * words : 刘厚南
             */

            var location: LocationBeanXX? = null
            var words: String? = null

            class LocationBeanXX {
                /**
                 * width : 59
                 * top : 641
                 * height : 22
                 * left : 317
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 证件编号Bean {
            /**
             * location : {"width":0,"top":0,"height":0,"left":0}
             * words : 无
             */

            var location: LocationBeanXXX? = null
            var words: String? = null

            class LocationBeanXXX {
                /**
                 * width : 0
                 * top : 0
                 * height : 0
                 * left : 0
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 地址Bean {
            /**
             * location : {"width":383,"top":608,"height":26,"left":318}
             * words : 北京市怀柔区桥梓镇兴桥大街1号南楼203室
             */

            var location: LocationBeanXXXX? = null
            var words: String? = null

            class LocationBeanXXXX {
                /**
                 * width : 383
                 * top : 608
                 * height : 26
                 * left : 318
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 有效期Bean {
            /**
             * location : {"width":39,"top":741,"height":28,"left":485}
             * words : 长期
             */

            var location: LocationBeanXXXXX? = null
            var words: String? = null

            class LocationBeanXXXXX {
                /**
                 * width : 39
                 * top : 741
                 * height : 28
                 * left : 485
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }

        class 成立日期Bean {
            /**
             * location : {"width":39,"top":741,"height":28,"left":485}
             * words : 长期
             */

            var location: LocationBeanXXXXX? = null
            var words: String? = null

            class LocationBeanXXXXX {
                /**
                 * width : 39
                 * top : 741
                 * height : 28
                 * left : 485
                 */

                var width: Int? = 0
                var top: Int? = 0
                var height: Int? = 0
                var left: Int? = 0
            }
        }
    }
}