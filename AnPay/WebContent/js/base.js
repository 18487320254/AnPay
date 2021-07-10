//js基础库(base.js)
/**
 * 1.为保证开发质量，使用本库请注意先完全读懂源代码
 * 2.为了减少冗余代码提高代码效率，本库对非普通用户参数未作严格验证，请程序员自行注意参数合法性问题
 * 3.库内部分方法涉及调用固定UI元素，请注意页面联系逻辑联系性
 * 4.本库为原生js库，除了To_pdf()方法需要依赖jspdf与html2canvas库外，其它不依赖任何第三方环境支持，只需引入页面即可使用
 * 5.日志写入方法需自行配置接口地址
 */
var school=null;//yart
//基础地址变量
var add_servers='';
var api_servers=['DiancanA','AncodePay'];
var search_word=null;
var select_word=null;

var setclass1=null;
var setclass2=null;
var desk=null;
var ishaveorders=0;

var src_host='https://diancanyun.oss-cn-chengdu.aliyuncs.com/';//静态资源主机
var folder=['pic_product/','pic_system/'];//文件夹

//标准命令字库
var cmd_state={'wait':'待处理','loading':'处理中','ok':'已完成'};
var cmd_d_state=['wait','loading','ok'];

//备用基础变量
var mainbatch=null;
var schedule=0;//进度条变量
var confirm_yes=2;//确认框变量
var jsonData=[];//备用json数据


//控制等单界面
function Show_Wait(isshow){
    getId('wait-iframe').style.display=isshow;
}

//getId(string 目标ID)  （通过id获得元素）
function getId(id){
    return document.getElementById(id);
}
//getId_parent(int 跳跃级数，string 目标ID)  （通过id获得父级页面元素，最高支持跨四级）
function getId_parent(sum,id){
    if (sum==1) {
        return window.parent.document.getElementById(id);//执行父级方法与此雷同
    } else if(sum==2){
        return window.parent.parent.document.getElementById(id);
    } else if(sum==3){
        return window.parent.parent.parent.document.getElementById(id);
    } else if(sum==4){
        return window.parent.parent.parent.parent.document.getElementById(id);
    }
}
//getId_child(int 跳跃级数，string 目标ID)  （通过id获得子级页面元素，最高支持跨四级）
function getId_child(sum,id){
    if (sum==1) {
        return getId('iframe').contentWindow.getId(id);//执行父级方法与此雷同
    } else if(sum==2){
        return getId('iframe').contentWindow.getId('iframe').contentWindow.getId(id);
    } else if(sum==3){
        return getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.getId(id);
    } else if(sum==4){
        return getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.getId(id);
    }
}
//getName(string 目标name)  （通过name获得元素列）
function getName(name){
    return document.getElementsByName(name);
}
//getName_parent(int 跳跃级数，string 目标name)  （通过name获得父级页面元素组，最高支持跨四级）
function getName_parent(sum,name){
    if (sum==1) {
        return window.parent.document.getElementsByName(name);
    } else if(sum==2){
        return window.parent.parent.document.getElementsByName(name);
    } else if(sum==3){
        return window.parent.parent.parent.document.getElementsByName(name);
    } else if(sum==4){
        return window.parent.parent.parent.parent.document.getElementsByName(name);
    }
}
//getValue(string 目标ID，string 属性名)  （通过id获得元素属性值）
function getValue(id,name_shuxing){
    return getId(id)[name_shuxing].replace(/=/g, "#equal#").replace(/'/g, "#apostrophe#").replace(/,/g, "，").replace(/%/g, "#percent#");
}
//getStyle(string 目标ID，string 样式名)  （通过id获得元素某行间样式的值）
function getStyle(id,name_style){
    return getId(id).style[name_style];
}
//getStyle_computed(string 目标ID，string 样式名)  （通过id获得元素某样式计算后的值）
function getStyle_computed(id,name_style){
    var obj=getId(id);
    if (obj.currentStyle) {//IE
        return obj.currentStyle[name_style];
    } else {//chrome、火狐
        return getComputedStyle(obj,false)[name_style];
    }
}

//获得下拉选择的值
function getSelect(id){
    var myselect=getId(id);
    var index=myselect.selectedIndex;
    return myselect.options[index].value;
}
//获得下拉选择的值
function getSelectinnerHTML(id){
    var myselect=getId(id);
    var index=myselect.selectedIndex;
    return myselect.options[index].innerHTML;
}
//选中某个下拉选项
function setSelect(id,newvalue){
    getId(id).value=newvalue;
}

//setAttribute(string 目标ID，string 属性名，string 值)  （操作元素属性）
function upAttribute(id,name_shuxing,newvalue){
    getId(id)[name_shuxing]=newvalue;
}
//setStyle(string 目标ID，string 样式名，string 值)  （操作元素样式）
function upStyle(id,name_style,newvalue){
    getId(id).style[name_style]=newvalue;
}

//go(string 跳转方式，string 目标url)  （支持各种页面跳转）
function go(){
    if (arguments.length>1) {
        if (arguments[1]=='top') {
            top.location.href=arguments[0];
        } else {
            window.open(arguments[0]);
        }
    } else {
        window.location.href=arguments[0];
    }
}


//获取url所携带的参数(点餐专用)
function getPt(name){
  var str1=window.location.href.split('?');
  var str2=str1[1].split('dcyanddcy');
  for (let i = 0; i < str2.length; i++) {
      var str=str2[i].split('=');
      if (str[0]==name) {
          return str[1];
      }
  }
}

//获取url所携带的参数
/*
function getPt(name){
    var str1=window.location.href.split('?');
    var str2=str1[1].split('&');
    for (let i = 0; i < str2.length; i++) {
        var str=str2[i].split('=');
        if (str[0]==name) {
            return str[1];
        }
    }
}*/

//获取顶部iframe的src所携带的参数
function getPti(name){
    var str1=top.document.getElementById('pop_window').src.split('?');
    var str2=str1[1].split('&');
    for (let i = 0; i < str2.length; i++) {
        var str=str2[i].split('=');
        if (str[0]==name) {
            return str[1];
        }
    }
}

//获取当前iframe的src所携带的参数
function getPtnow(){
    var str1='';
    if (arguments.length>1) {
        str1=window.parent.document.getElementById(arguments[1]).src.split('?');
    }else{
        str1=window.parent.document.getElementById('iframe').src.split('?');
    }
    var str2=str1[1].split('&');
    for (let i = 0; i < str2.length; i++) {
        var str=str2[i].split('=');
        if (str[0]==arguments[0]) {
            return str[1];
        }
    }
}

//追加html
function AddHtml(id,myhtml){
    var oldhtml=getId(id).innerHTML;
    upAttribute(id,'innerHTML',oldhtml+myhtml);
}

//show_Alert(string 提示文字)  （标准信息提示框，需要匹配HTML与css库）
function show_Alert(text){
    if (text.length>500) {
        text='字符串过长，无法展示';
    }
    getId('alert_text').innerHTML=text;
    getId('alert').style.display='block';
}
//show_confirm(string 确认内容)  （标准信息确认框，需要匹配HTML与css库）
function show_confirm(text){
    if (text.length>500) {
        text='字符串过长，无法展示';
    }
    window.top.getId('confirm_text').innerHTML=text;
    window.top.getId('confirm').style.display='block';
}
//show_prompt(string 提示文字)  （标准弹出单项输入框，需要匹配HTML与css库）
function show_prompt(text){
    if (text.length>25) {
        text='请输入';
    }
    getId('prompt_text').innerHTML=text;
    getId('prompt').style.display='block';
}
//show_schedule(int 参照百分变量)  （标准进度条，需要匹配HTML与css库）
function show_schedule(text){
    getId('schedule_text').innerHTML=text+'%';
    getId('schedule_text').style.width=text+'%';
    getId('schedule').style.display='block';
}
//show_loading(string 显示属性)  （标准加载动画，需要匹配HTML与css库）
function show_loading(isshow){
    getId('loading').style.display=isshow;
}
//show_loading(string 显示属性)  （标准加载动画，需要匹配HTML与css库）
function show_loading_bot(isshow){
    getId('loading_bottom').style.display=isshow;
}
//show_pop(string 显示属性，string 目标路径)  （标准弹出页面，需要匹配HTML与css库）
function show_pop(isshow,newhref){
    getId('pop').style.display=isshow;
    getId('pop_window').src=newhref;
}

//popLast(int 跳跃级数)  （弹出窗口隐藏并刷新源页面，最高支持跨四级）
function popLast(x){
    top.hide_s('pop',',','none');
    if (x==1) {
        top.getId('iframe').contentWindow.location.reload(true);
    } else if(x==2){
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    } else if (x==3) {
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    }else if (x==4) {
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    }

}

//backLast(int 跳跃级数)  （背景式刷新源页面，最高支持跨四级）
function backLast(x){
    if (x==1) {
        top.getId('iframe').contentWindow.location.reload(true);
    } else if(x==2){
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    } else if (x==3) {
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    }else if (x==4) {
        top.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.getId('iframe').contentWindow.location.reload(true);
    }

}

//Ajax请求
// 注意参数 callback 函数为从服务器端成功取到数据后的回调函数
function Ajax(reptype,callback,API,par) {
    // 使用 jQuery 向服务器端发送 Ajax 请求
    $.ajax({
        url: add_servers+API,          // 对应 Code-10.1 第 17 行, 注意多了 "/wl" 前缀
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
/*
//Ajax(sting 请求方式，string 接口名，sting 请求参数串,出口函数)    (Ajax数据请求，有返回)
function Ajax(method,lastm,url,parameterlist){
	var xmlhttp;
	if (window.XMLHttpRequest){
		//IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
		xmlhttp=new XMLHttpRequest();
	}else{
		//IE6, IE5 浏览器执行代码
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open(method,add_servers+url,true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.withCredentials = true;
	xmlhttp.send(parameterlist);
	xmlhttp.onreadystatechange=function(){
		if (xmlhttp.readyState==4&&xmlhttp.status==200){
            var rs=xmlhttp.responseText;
            //alert(rs);
            lastm(rs);
		}
	}
}
*/

//Ajax_data(sting 请求方式，string 接口名，sting 请求参数串)    (Ajax数据请求，无返回)
function Ajax_noback(method,url,parameterlist){
	var xmlhttp;
	if (window.XMLHttpRequest){
		//IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
		xmlhttp=new XMLHttpRequest();
	}else{
		//IE6, IE5 浏览器执行代码
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.open(method,url,true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.withCredentials = true;
	xmlhttp.send(parameterlist);
}

//Ajax_Upload(this，string 目标文件夹，sting 接口名) (用于单文件上传,由file标签onchange事件触发)
function Ajax_Upload(data,lastm,tofolder,className){
    var x=new FileReader;
    x.readAsDataURL(data.files[0]);
    x.onloadend=function(){//用户选择的文件读取完成时
        //上传文件
        var myfile = data.files[0];
        var fm = new FormData();
        fm.append('tofolder', tofolder);
        fm.append('myfile', myfile);
        var request = new XMLHttpRequest();
        /*xhr.upload.onprogress失效是很多人都踩过的坑，对于XMLHttpRequest Level 2，假设xhr是XMLHttpRequest的实例对象，那么xhr.upload.addEventListener('progress', callback)或者xhr.upload.onprogress = callback; 可以用来监测文件上传进度，xhr.upload是一个XMLHttpRequestUpload对象，即xhr上传对象，xhr.upload有abort, error, load, load, loadend, loadstart, progress, timeout几个事件。
        如果绑定的事件没有被触发，很有可能是事件绑定的代码写错了位置。正确的位置是在xhr.send()以前，将xhr.upload.onprogress = callback; 或者xhr.upload.addEventListener('progress', callback); 放到xhr.send()后面是不会生效的，其他的事件也一样。
        对于xhr.upload.onprogress事件处理函数，它的第一个参数e为上传进度发生变化时触发的事件，e.isTrusted和e.lengthComputable为布尔值，e.isTrusted为true时代表事件为用户触发的可信事件，e.lengthComputable为true时代表上传总字节量可以计算出来，e.loaded是已经加载的字节数，e.total是总的字节数, e.loaded / e.total 就是完成的比例， 需要注意的是，如果e.lengthComputable为false，e.total为0. 所以在计算进度的时候需要先判断e.lengthComputable，否则得到的值是Infinity，也就是无穷大。 */
        request.upload.onprogress=function (ev){
            if(ev.lengthComputable){
                var precent=100 * ev.loaded/ev.total;
                window.top.schedule=precent;
                window.top.show_schedule(precent.toFixed(1));
                if (precent>=100) {
                    window.top.getId('schedule').style.display='none';
                }
                console.log(precent);
            }
        }
        request.open("POST",add_servers+className,true);
        request.withCredentials = true;
        request.send(fm);
        request.onreadystatechange=function(){
            if (request.readyState==4 && request.status==200){
                var rs=request.responseText;
                lastm(rs);
            }
        }
        //window.top.Schedule();
    }
}  

//outlog(string 应用类型，string 自定义文字描述，string 原文) (前端日志输出)
function outlog(apptype,logtype,remarks,text){
    var parameterlist='apptype='+apptype+'&logtype='+logtype+'&remarks='+remarks+'&text='+text;
    var url=add_servers+api_servers[0];
    Ajax_noback('post',url,parameterlist);
}

//文件下载
function Download(src,name) {
    let a = document.createElement('a');
    a.href = src;
    a.download='yimai_'+name;
    a.click();
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
function To_Excel(title,jsonData,filename){
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

//To_pdf(string 目标元素id，int 清晰度dpi,string 背景色，string 纸张类型，string 导出文件名)    （导出pdf文件）
    /*此功能需要依赖jspdf.js与html2canvas.js库
    <script src="js/jspdf.debug.js"></script>
    <script src="js/html2canvas.js"></script>
    dpi取值0--1之间
    背景色建议#fff
    纸张建议为a4
    导出文件名需带.pdf的后缀
    */
function To_pdf(id,dpi,backcolor,paper,filename){
    //获得目标范围
    var downPdf = document.getElementById(id);
    html2canvas(downPdf, {
        onrendered:function(canvas) {
            //返回图片URL，参数：图片格式和清晰度(0-1)
            var pageData = canvas.toDataURL('image/jpeg', dpi);
            //方向默认竖直，尺寸ponits，格式a4【595.28,841.89]
            var pdf = new jsPDF('landscape', 'pt', paper);
            //需要dataUrl格式
            pdf.addImage(pageData, 'JPEG', 0, 0, 841.89, 838.89/canvas.width * canvas.height );
            pdf.save(filename);
        },
        //背景设为白色（默认为黑色）
        background: backcolor 
    })
}


//hide_s(string id链,string 截取标记,string display值)     （批量隐藏或展示元素，idlist建议用英文逗号拼接）
function hide_s(idlist,word,stylevalue){
    var arr=idlist.split(word);
    for(var i=0;i<arr.length;i++){
        getId(arr[i]).style.display=stylevalue;
    }
} 
//change(obj 当前元素this,目标链接,string 当前元素目标类名,string 其它元素类名)   （导航条单选及清理，导航元素必须属于change类）
function change(now,newhref,nowclass,otherclass){
    if(newhref!=''&&newhref!=null){
        getId('iframe').src=newhref;
    }
    var changeobj=getName('change');
    for(var i=0;i<changeobj.length;i++){
        changeobj[i].className=otherclass;
    }
    now.className=nowclass;
}

//规则显示解析（text规则串，type规则类型）
function Analysis(text,type){
    var str='-';
    var fuhao1=null;
    var fuhao2=null;
    if (type=='score') {
        fuhao1='&times;';
        fuhao2='+';
    } else if(type=='threshold'){
        fuhao1='>';
        fuhao2='且';
    } else if(type=='subject'){
        fuhao1='/';
        fuhao2='、';
    } else if(type=='teachers'){
        fuhao1='/';
        fuhao2='、';
    }
    if (text!='-'&&text!=''&&text!=' ') {
        var idlist=text.split(';');
        for (let i = 0; i < idlist.length; i++) {
            var valArr=idlist[i].split(':');
            if (i==0) {
                str=valArr[1]+fuhao1+valArr[2];
            } else {
                str+=fuhao2+valArr[1]+fuhao1+valArr[2];
            }
        }
    }
    return str;
}
