
//全局URL
//var server_host='http://supersystem.anguangkeji.com/Super_system/';
var server_host='';
var confirm_yes=2;
//Ajax请求
// 注意参数 callback 函数为从服务器端成功取到数据后的回调函数
function An_ajax(callback,API,par,reptype) {
    document.getElementById('loading').style.display = 'block';
    // 使用 jQuery 向服务器端发送 Ajax 请求
    $.ajax({
        url: server_host+API,          // 对应 Code-10.1 第 17 行, 注意多了 "/wl" 前缀
        type: reptype,                 // 声明以 Post 方式发送请求
        dataType: "text",
        data:par,             // 告诉 jQuery, 服务器端返回的数据是 JSON 格式
        beforeSend: function () {     // 发送请求前的回调函数
            //$("#progress").show();    // 发送请求前显示进度提示(左下角一个绕圈圈的动画)
        },
        success: function (rs) {    // 请求成功时的回调函数
            document.getElementById('loading').style.display = 'none';
            if(rs=='n_do'){
                alert('操作失败！'); 
            } else if(rs=='n_login') {
                An_go('login.html','top');   
            } else if(rs=='n_psd') {
                alert('账密错误或账户被禁用！');   
            } else if(rs=='n_reged') {
                alert('手机号已经被占用！');   
            } else if(rs=='n_code') {
                alert('验证码错误或已过期！');   
            } else if(rs=='n_user') {
                alert('未获取到相关用户信息！');   
            } else {
                if (callback!='null') {
                    callback(rs);// 回调 callback 函数, 并将服务器端返回的数据传入
                }
            }
        },
        error: function () {  // 请求失败的回调函数
            document.getElementById('loading').style.display = 'none';
            alert('请求失败！');     
        },
        complete: function () {       // 请求完成时的回调函数
            //$("#progress").hide();    // 隐藏进度提示
        }
    });
}
function An_getVal(id){
    return An_enCode($("#"+id).val());
}

function An_enCode(text){
    return text.replace(/=/g, "#Ancode-equal#").replace(/\?/g, "#Ancode-questionmark#").replace(/&/g, "#Ancode-and#").replace(/,/g, "#Ancode-comma#").replace(/%/g, "#Ancode-percent#").replace(/\+/g, "#Ancode-add#").replace(/\//g, "#Ancode-or#").replace(/\\/g, "#Ancode-cbor#").replace(/'/g, "#Ancode-dan#").replace(/"/g, "#Ancode-shuang#");
}
function An_unCode(text){
    return text.replace(/#Ancode-equal#/g, "=").replace(/#Ancode-questionmark#/g, "?").replace(/#Ancode-and#/g, "&").replace(/#Ancode-comma#/g, ",").replace(/#Ancode-percent#/g, "%").replace(/#Ancode-add#/g, "+").replace(/#Ancode-or#/g, "/").replace(/#Ancode-cbor#/g, "\\").replace(/#Ancode-dan#/g, "'").replace(/#Ancode-shuang#/g, "\"");
}

//go(string 跳转方式，string 目标url)  （支持各种页面跳转）
function An_go(){
    if (arguments.length>1) {
        if (arguments[1]=='top') {
            top.location.href=arguments[0];
        }else if(arguments[1]=='topop'){
            top.open(arguments[0]);
        } else {
            window.open(arguments[0]);
        }
    } else {
        window.location.href=arguments[0];
    }
}

function An_getPt(parname){
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == parname){
            return pair[1];
        }
    }
    return(false);
}

//得到当前时间字符串，格式为：YYYY-MM-DD HH:MM:SS    
function An_getTime(type)  { 
    var now = new Date();  
    var year = now.getFullYear();       //年  
    var month = now.getMonth() + 1;     //月  
    var day = now.getDate(); 
    var hh = now.getHours();            //时  
    var mm = now.getMinutes();          //分  
    var ss=now.getSeconds(); 
    var clock = ""; 
    if (type=='time') {
        clock = year + "-";  
        if(month < 10) clock += "0";         
        clock += month + "-";  
        if(day < 10) clock += "0";   
        clock += day + " ";  
        if(hh < 10) clock += "0";  
        clock += hh + ":";  
        if (mm < 10) clock += '0';   
        clock += mm+ ":"; 
        if (ss < 10) clock += '0';   
        clock += ss;  
    } else if(type=='time_d') {
        clock = year + "-";  
        if(month < 10) clock += "0";         
        clock += month + "-";  
        if(day < 10) clock += "0";   
        clock += day;  
    } else if(type=='rm') {
        clock = year;  
        if(month < 10) clock += "0";         
        clock += month;  
        if(day < 10) clock += "0";   
        clock += day;  
        if(hh < 10) clock += "0";  
        clock += hh;  
        if (mm < 10) clock += '0';   
        clock += mm; 
        if (ss < 10) clock += '0';   
        clock += ss; 
    } else if(type=='rm_d') {
        clock = year;  
        if(month < 10) clock += "0";         
        clock += month;  
        if(day < 10) clock += "0";   
        clock += day;  
    }
    return(clock);
}

function An_Download(src,name) {
    let a = document.createElement('a');
    a.href = src;
    a.download=name;
    a.target="_blank";
    a.click();
}
//生成从minNum到maxNum的随机数
function An_random(minNum,maxNum){ 
    switch(arguments.length){ 
        case 1: 
            return parseInt(Math.random()*minNum+1,10); 
        break; 
        case 2: 
            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); 
        break; 
        default: 
            return 0; 
        break; 
    } 
} 

//To_Excel(string 列标题, String json,string 文件名) (json导出excel表格，列标题用英文逗号隔开)
/*要导出的json数据格式示例
var jsonData = [
      {
        name:'路人甲',
        phone:'123456789',
        email:'000@123456.com'
      },
      {
        name:'炮灰乙',
        phone:'123456789',
        email:'000@123456.com'
      },
      {
        name:'土匪丙',
        phone:'123456789',
        email:'000@123456.com'
      },
      {
        name:'流氓丁',
        phone:'123456789',
        email:'000@123456.com'
      },
    ]
 */
function An_ToExcel(title,jsonData,filename){
    //列标题，逗号隔开，每一个逗号就是隔开一个单元格
    let str = title+`\n`;
    //增加\t为了不让表格显示科学计数法或者其他格式
    for(let i = 0 ; i < jsonData.length ; i++ ){
        for(let item in jsonData[i]){
            str+=`${jsonData[i][item] + '\t'},`;     
        }
        str+='\n';
    }
    //encodeURIComponent解决中文乱码
    let uri = 'data:text/csv;charset=utf-8,\ufeff' + encodeURIComponent(str);
    //通过创建a标签实现
    var link = document.createElement("a");
    link.href = uri;
    //对下载的文件命名
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}
//change(obj 当前元素this,目标链接,string 当前元素目标类名,string 其它元素类名)   （导航条单选及清理，导航元素必须属于change类）
function An_Change(now,newhref,nowclass,otherclass){
    if(newhref!=''&&newhref!=null){
        getId('iframe').src=newhref+'?timerm='+An_getTime('rm');
    }
    var changeobj=getName('change');
    for(var i=0;i<changeobj.length;i++){
        changeobj[i].className=otherclass;
    }
    now.className=nowclass;
}

//getId(string 目标ID)  （通过id获得元素）
function getId(id){
    return document.getElementById(id);
}
//getName(string 目标name)  （通过name获得元素列）
function getName(name){
    return document.getElementsByName(name);
}
//***********存在模态框依赖 */
function An_alert(text){
    $("#alert_text").text(text);
    $('#myModal-alert').modal('toggle');
}
function An_confirm(text,type,callback){
    if (type=='show') {
        $("#confirm_text").text(text);
        $("#confirm").show();
        var deltime=setInterval(function(){
                    if (confirm_yes==1) {
                        confirm_yes=2;
                        $("#confirm").hide();
                        $("#confirm_text").text('');
                        clearInterval(deltime);
                        callback();
                    }else if (confirm_yes==0) {
                        confirm_yes=2;
                        $("#confirm").hide();
                        $("#confirm_text").text('');
                        clearInterval(deltime);
                    }
                }, 100);
    } else {
        confirm_yes=2;
        $("#confirm").hide();
        $("#confirm_text").text(text);
    }
}

//****************************************************************** */
//****************************************************************** */
//****************************************************************** */
//****************************************************************** */
//****************************************************************** */
//****************************************************************** */
var islogin=0;
var search_word='';
var m_mylist='</li><li><a onclick="$(\'#myModal-uppsd\').modal(\'toggle\')"> <i class="fa fa-unlock-alt fa-fw"></i> 修改密码</a> </li><li><a onclick="$(\'#myModal-upphone\').modal(\'toggle\')"> <i class="fa fa-mobile fa-fw"></i> 手机换绑</a> </li><div class="menu-divider"></div>';
var m_tea_left='<li> <a href="teachers_index.html?tid=data-tid"> <img src="data-headimg" alt=""><span> data-name </span>data-istop</a></li>';
var m_istop='<span class="dot-notiv"></span>';
var m_buyed_class='<div><a href="single-video.html?main_id=data-cid" class="video-post video-post-list"><div class="video-post-thumbnail"><span class="video-post-time">data-price</span><span class="play-btn-trigger"></span><img src="data-pic" alt=""></div><div class="video-post-content"><h3> data-title</h3><img src="data-headimg" alt=""><span class="video-post-user">data-name</span><span class="video-post-views">data-lable </span></div></a></div>';
var m_buyed_orders='<div><a href="single-video.html?main_id=data-cid" class="video-post video-post-list"><div class="video-post-thumbnail"><span class="video-post-time">data-state</span><span class="play-btn-trigger"></span><img src="data-pic" alt=""></div><div class="video-post-content"><h3> data-title</h3><span class="video-post-user">data-order-number</span><span class="video-post-views">data-time data-price</span></div></a></div>';
var m_search_class='<li> <a href="single-video.html?main_id=data-cid"> 【课程】data-title .. </a> </li>';
var m_search_teacher='<li> <a href="teachers_index.html?tid=data-tid"> 【老师】data-name / data-lable .. </a> </li>';
$(document).ready(function(){
    var par='type=user_info';
    //An_ajax(cb_user_info,"GetUserData",par,"post");
    par='type=teacher_hot_left';
    //An_ajax(cb_teacher_hot_left,"GetTeacher",par,"post");
});
function cb_user_info(rs){
    if (rs=='n_login') {
        $("#login").text('登录 / 注册');
        $("#login").attr("onclick", "$('#myModal-login-code').modal('toggle')");
        $("#video_btn").css("display", "none");
        $("#orders_btn").css("display", "none");
    } else {
        //info
        islogin=1;
        $("#mylist").prepend(m_mylist);
        $("#video_btn").css("display", "block");
        $("#orders_btn").css("display", "block");
        var obj=rs.split('#Ancode#');
        $("#myinfo_name").text(obj[0]);
        //$("#user_lable").text(obj[1]);
        $("#myinfo_id").text(obj[2]);
        //class list
        var par='type=buyed_class';
        An_ajax(cb_buyed_class,"GetUserData",par,"post");
        //order list
        par='type=buyed_orders';
        An_ajax(cb_buyed_orders,"GetUserData",par,"post");
    }
}
function cb_teacher_hot_left(rs){
    var obj=jQuery.parseJSON(rs);
    var myhtml='';
    for (let i = 0; i < obj.length; i++) {
        if (i<3) {
            myhtml+=m_tea_left.replace(/data-tid/g,obj[i].id)
            .replace(/data-headimg/g,An_unCode(obj[i].headimg))
            .replace(/data-name/g,obj[i].name)
            .replace(/data-istop/g,m_istop);
        } else {
            myhtml+=m_tea_left.replace(/data-tid/g,obj[i].id)
            .replace(/data-headimg/g,An_unCode(obj[i].headimg))
            .replace(/data-name/g,obj[i].name)
            .replace(/data-istop/g,"");
        }
    }
    $("#list_tea_left").html(myhtml);
}
function cb_buyed_class(rs){
    var obj=jQuery.parseJSON(rs);
    var myhtml='';
    for (let i = 0; i < obj.length; i++) {
        myhtml+=m_buyed_class.replace(/data-cid/g,obj[i].id)
        .replace(/data-price/g,'￥'+obj[i].price)
        .replace(/data-title/g,obj[i].title)
        .replace(/data-pic/g,An_unCode(obj[i].pic))
        .replace(/data-headimg/g,An_unCode(obj[i].headimg))
        .replace(/data-name/g,obj[i].name)
        .replace(/data-lable/g,obj[i].lable);
    }
    $("#list_buyed_class").html(myhtml);
}
function cb_buyed_orders(rs){   
    var obj=jQuery.parseJSON(rs);
    var myhtml='';
    for (let i = 0; i < obj.length; i++) {
        var state='待支付';
        switch (obj[i].state) {
            case 'w':
                state='待支付';
                break;
            case 'y':
                state='已购买';
                break;
            case 'c':
                state='订单关闭';
                break;
            default:
                state='订单异常';
                break;
        }
        myhtml+=m_buyed_orders.replace(/data-cid/g,obj[i].id)
        .replace(/data-price/g,'￥'+obj[i].price)
        .replace(/data-title/g,obj[i].title)
        .replace(/data-pic/g,An_unCode(obj[i].pic))
        .replace(/data-order-number/g,obj[i].order_number)
        .replace(/data-time/g,obj[i].time)
        .replace(/data-state/g,state);
    }
    $("#list_buyed_orders").html(myhtml);
}
function Exit(){
    var par='type=exit_user';
    An_ajax(function(rs){
        window.location.reload();
    },"LoginUser",par,"post");
}
$('#search_word').on('input propertychange', function() {//监听文本框
    var search_word1=$('#search_word').val();
    if (search_word1!=null&&search_word1!=search_word&&search_word1.length>0) {
        search_word=search_word1;
        var par='type=search_all&word='+search_word1;
        An_ajax(cb_search_all,"GetClass",par,"post");
    }
});
function cb_search_all(rs){
    $("#search_rs_list").html('<li class="list-title"> 相关结果 </li>');
    var obj0=rs.split('#Ancode#');
    var obj1=jQuery.parseJSON(obj0[0]);
    var obj2=jQuery.parseJSON(obj0[1]);
    var leng1=obj1.length;
    var leng2=obj2.length;
    if ((obj1.length+obj2.length)>16) {
        leng1=parseInt((16*parseFloat((obj1.length/(obj1.length+obj2.length)))));
        leng2=16-leng1;
    }
    var myhtml1='';
    for (let i = 0; i < leng1; i++) {
        myhtml1+=m_search_class.replace(/data-cid/g,obj1[i].id)
        .replace(/data-title/g,obj1[i].title);
    }
    $("#search_rs_list").append(myhtml1);
    var myhtml2='';
    for (let i = 0; i < leng2; i++) {
        myhtml2+=m_search_teacher.replace(/data-tid/g,obj2[i].id)
        .replace(/data-name/g,obj2[i].name)
        .replace(/data-lable/g,obj2[i].lable);
    }
    $("#search_rs_list").append(myhtml2);
}
$("#reg_getcode").click(function (e) { 
    var phone=$("#reg_phone").val();
    if (phone.length==11) {
        $("#reg_getcode").text('已发送');
        $("#reg_getcode").attr('disabled',true);
        setTimeout(function(){
            $("#reg_getcode").text('获取验证码');
            $("#reg_getcode").attr('disabled',false);
        }, 60*1000);
        var par='type=reg_get_code&phone='+phone;
        An_ajax(function(rs){},"LoginUser",par,"post");
    } else {
        alert('手机号格式错误！');   
    }
});
$("#login_code_getcode").click(function (e) { 
    var phone=$("#login_code_phone").val();
    if (phone.length==11) {
        $("#login_code_getcode").text('已发送');
        $("#login_code_getcode").attr('disabled',true);
        setTimeout(function(){
            $("#login_code_getcode").text('获取验证码');
            $("#login_code_getcode").attr('disabled',false);
        }, 60*1000);
        var par='type=login_get_code&uid='+phone;
        An_ajax(function(rs){},"LoginUser",par,"post");
    } else {
        alert('手机号格式错误！');   
    }
});
$("#upphone_getcode").click(function (e) { 
    var phone=$("#upphone_phone").val();
    var psd=$("#upphone_psd").val();
    if (phone.length==11) {
        $("#upphone_getcode").text('已发送');
        $("#upphone_getcode").attr('disabled',true);
        setTimeout(function(){
            $("#upphone_getcode").text('获取验证码');
            $("#upphone_getcode").attr('disabled',false);
        }, 60*1000);
        var par='type=upphone_get_code&newphone='+phone+'&psd='+psd;
        An_ajax(function(rs){},"LoginUser",par,"post");
    } else {
        alert('手机号格式错误！');   
    }
});
$("#uppsd_getcode").click(function (e) { 
    $("#uppsd_getcode").text('已发送');
    $("#uppsd_getcode").attr('disabled',true);
    setTimeout(function(){
        $("#uppsd_getcode").text('获取验证码');
        $("#uppsd_getcode").attr('disabled',false);
    }, 60*1000);
    var par='type=uppsd_get_code';
    An_ajax(function(rs){},"LoginUser",par,"post");
});


$("#reg_sub").click(function (e) { 
    if($("#reg_agree").is(":checked")){//选中 
        var psd1=$("#reg_psd1").val();
        var psd2=$("#reg_psd2").val();
        var code=$("#reg_code").val();
        var name=$("#reg_name").val();
        if (psd1.length>=6) {
            if (psd1==psd2) {
                var par='type=reg_user&psd='+psd1+'&code='+code+'&lable=暂无个性标签&name='+name;
                An_ajax(function(rs){
                    if (rs=='yes') { 
                        alert('注册成功！'); 
                        $('#myModal-login-reg').modal('toggle')   
                    } else {
                        alert('操作失败！');   
                    }
                },"LoginUser",par,"post");
            } else {
                alert('两次输入的密码不一致！');  
            }
        } else {
            alert('密码长度至少为6位！');  
        }
    }else{
        alert('请勾选同意注册协议！'); 
    }
});
$("#login_code_sub").click(function (e) { 
    var code=$("#login_code_code").val();
    var par='type=login_code&code='+code;
    An_ajax(function(rs){
        if (rs=='yes') { 
            window.location.reload();  
        } else {
            alert('登录失败！');   
        }
    },"LoginUser",par,"post");
});
$("#login_psd_sub").click(function (e) { 
    var uid=$("#login_psd_uid").val();
    var psd=$("#login_psd_psd").val();
    var par='type=login_psd&uid='+uid+'&psd='+psd;
    An_ajax(function(rs){
        if (rs=='yes') { 
            window.location.reload();  
        } else {
            alert('登录失败！');   
        }
    },"LoginUser",par,"post");
});
$("#upphone_sub").click(function (e) { 
    var code=$("#upphone_code").val();
    var par='type=upphone_user&code='+code;
    An_ajax(function(rs){
        if (rs=='yes') { 
            window.location.reload();  
        } else {
            alert('登录失败！');   
        }
    },"LoginUser",par,"post");
});
$("#uppsd_sub").click(function (e) { 
    var psd1=$("#uppsd_psd1").val();
    var psd2=$("#uppsd_psd2").val();
    var code=$("#uppsd_code").val();
    if (psd1.length>=6) {
        if (psd1==psd2) {
            var par='type=uppsd_user&psd='+psd1+'&code='+code;
            An_ajax(function(rs){
                if (rs=='yes') { 
                    window.location.reload(); 
                } else {
                    alert('操作失败！');   
                }
            },"LoginUser",par,"post");
        } else {
            alert('两次输入的密码不一致！');  
        }
    } else {
        alert('密码长度至少为6位！');  
    }
});