package com.cocosh.shmstore.home.model

/**
 * 红包属性对象
 * Created by zhangye on 2018/8/17.
 */
data class BonusAttr(
        var id: String, //属性id
        var parent: String, // 父字段id
        var child: String, // 包含子字段个数
        var dom: String, // 字段类型(用于客户端呈现),eg:`text`-文本,`numeric`-数字,`amount`-金额,`datetime`-日期/时间,`radio`-单选按钮,`checkbox`-复选框,`upload.image`-图片上传,`文件上传`-文件上传,`range`-区间,`district`-区域,`geo`-位置半径(坐标点+N公里半径)...
        var name: String, // 属性名称,eg:红包名称,红包图片
        var keyword: String,
        var limit: ArrayList<Limit>,
        var desc: String,  // 属性说明,eg:全角字符,最大不得超过设置值,仅可输入正整数,系统自动算出,...
        var style: String, //客户端属性展示风格,eg:"readonly"---是否只读,不能编辑,"auto"---是否自动计算,"display"---初始化页面时,是否显示或隐藏,"toggle":"ad_pos"---针对(关键词为ad_pos的)属性切换状态
        var required: String, //是否为必填属性,即:属性值不能为空值
        var hidden: String,  // 是否为隐藏属性,在UI层不展示,客户端ajax提交时上送,eg:广告轮播图的<主图>
        var save: String, //存档入库方式:'0'-不存档,'1'-当前属性(子属性合并,json格式)存档,'2'-子属性分别存档
        var order: String //属性排序序号(值相同,则默认横向布局),升序
) {
    data class Limit(
            var name: String,   // 限定类型名称,eg:可选位置,可选类型,显示格式,...
            var custom: String, // 自定义选取的系统限制词汇,eg:"region"-区域,"industry"-行业,"hobby"-兴趣爱好,"ad"-广告对象(不弹窗),"ad_pos"-广告位置(不弹窗),...
            var rules: String,  // 限制规则,为空表示不限定,json格式,eg:{"<=":20},["Y-m-d H:i:s"],["jpeg","png"],...
            var desc: String // 属性说明,用于客户端展示
    )
}