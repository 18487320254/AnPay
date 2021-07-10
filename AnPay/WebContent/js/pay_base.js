
//全局URL
//var server_host='http://supersystem.anguangkeji.com/Super_system/';
var server_host='';
var confirm_yes=2;
//Ajax请求
// 注意参数 callback 函数为从服务器端成功取到数据后的回调函数
function An_ajax(callback,API,par,reptype) {
    // 使用 jQuery 向服务器端发送 Ajax 请求
    $.ajax({
        url: server_host+API,          // 对应 Code-10.1 第 17 行, 注意多了 "/wl" 前缀
        type: reptype,                 // 声明以 Post 方式发送请求
        dataType: "text",
        data:par,             // 告诉 jQuery, 服务器端返回的数据是 JSON 格式
        beforeSend: function () {     // 发送请求前的回调函数
            //$("#progress").show();    // 发送请求前显示进度提示(左下角一个绕圈圈的动画)
        },
        success: function (rs) {    // 请求成功时的回调函数    // 若服务器端回应数据中状态码 code >= 0, 说明服务器端一切正常
            if(rs=='n_do'){
                alert('操作失败！'); 
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
        error: function () {          // 请求失败的回调函数
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
/*
function An_alert(text){
    $("#alert_text").text(text);
    $('#myModal-alert').modal('toggle');
}
*/
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

function getPt(parname){
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
function getTime(type)  { 
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
    } else if(type=='rm_n') { 
        if(hh < 10) clock = "0";  
        clock += hh;  
        if (mm < 10) clock += '0';   
        clock += mm; 
        if (ss < 10) clock += '0';   
        clock += ss;
    } 
    return(clock);
}

function An_uncode(text){
    return text.replace(/#Ancode-equal#/g, "=").replace(/#Ancode-questionmark#/g, "?").replace(/#Ancode-and#/g, "&").replace(/#Ancode-comma#/g, ",").replace(/#Ancode-percent#/g, "%").replace(/#Ancode-add#/g, "+");
}

function Download(src,name) {
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
