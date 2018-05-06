<%--
  Created by IntelliJ IDEA.
  User: zuoyu
  Date: 5/6/18
  Time: 10:43 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>关系小程序</title>
    <script type="text/javascript" src="jQuery/jquery.min.js"></script>
    <style type="text/css">
      *{
        font-size: 30px;
        font-family: 微软雅黑;
      }
      .square_1 {
        width: 100px;
        height: 100px;
        border: 1px solid black;
        display: table-cell;
        vertical-align: middle;
        text-align: center;
        background-color: #E8E8E8;
      }
      span {
        display: table-cell;
        width: 44px;
      }
      #center {
        border: 1px dashed black;
        background-color: #F8F8F8;
      }
      #window {
        width: 388px;
        height: 50px;
        text-align: center;
        border: 1px solid black;
      }
      #updata{
        height: 50px;
        width: 100px;
        text-align: center;
        border: 1px solid black;
        background-color: #E8E8E8;
      }
      .but {
        width: 388px;
        height: 50px;
        text-align: center;
        line-height: 50px;
        border: 1px solid black;
        background-color: #E8E8E8;
      }
    </style>
  </head>
  <body>
  <center id="center">
    <p />
    <button value="爸爸" class="square_1">爸爸</button>&nbsp;
    <button value="妈妈" class="square_1">妈妈</button>&nbsp;
    <button value="丈夫" class="square_1">丈夫</button>&nbsp;
    <button value="妻子" class="square_1">妻子</button>&nbsp;
    <br/>
    &nbsp;
    <br/>
    <button value="哥哥" class="square_1">哥哥</button>&nbsp;
    <button value="弟弟" class="square_1">弟弟</button>&nbsp;
    <button value="姐姐" class="square_1">姐姐</button>&nbsp;
    <button value="妹妹" class="square_1">妹妹</button>&nbsp;
    <br/>
    &nbsp;
    <br/>
    <button value="儿子" class="square_1">儿子</button>&nbsp;
    <button value="女儿" class="square_1">女儿</button>&nbsp;
    <button value="的" class="square_1" id="de">的</button>&nbsp;
    <button class="square_1" id="rest">重置</button>&nbsp;
    <p />
  </center>
  <p />
  <center>
    <span id="window"></span><span style="border: 1px solid black" id="updata">修改</span>
    <div id="exit" class="but">退出</div>
    <div id="settings" class="but">管理数据</div>
  </center>
  </body>

  <script type="text/javascript">
      $(function() {
          var one = "";
          var string = "";
          var updata = "";
          $(".square_1").hover(function(){
              $(this).css("backgroundColor","#CCFFFF");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $("#de").hover(function(){
              $(this).css("backgroundColor","#5F9EA0");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $("#rest").hover(function(){
              $(this).css("backgroundColor","#90EE90");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $("#updata").hover(function(){
              $(this).css("backgroundColor","#E09E90");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $("#exit").hover(function(){
              $(this).css("backgroundColor","#FF4500");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $("#settings").hover(function(){
              $(this).css("backgroundColor","#E09E90");
          },function(){
              $(this).css("backgroundColor","#E8E8E8");
          });

          $(".square_1").on("click",function(){
              $(this).css("backgroundColor","#FFFFF0");
          });
          $("#rest").on("click",function(){
              one = "";
              $("#window").empty();
          });
          $("#exit").on("click",function(){
              if (confirm("确定退出吗？")) {
                  window.opener=null;
                  window.open('','_self');
                  window.close();
              }
          });
          $("#updata").on("click",function(){
              one = prompt("请输入要修改的值", one);
              $.post(
                  "index.do",
                  {
                      updata:updata,
                      one:one,
                  },
                  function(data) {
                      if (data || data.data) {
                          alert("修改成功！");
                      } else {
                          alert("修改失败！");
                      }
                  }
              );
          });
          $(".square_1:not('#rest')").on("click",function(){
              one = one + $(this).val();
              string = one;
              updata = one;
              $.post(
                  "index.do",
                  {
                      data:one,
                  },
                  function(data) {
                      $.each(data,function(index,obj){
                          one = obj.resultName;
                          if(one == null || one == undefined || one == ""){
                              one = prompt("数据库内没有该数值，请输入应有的数值：", "");
                              $.post(
                                  "index.do",
                                  {
                                      string:string,
                                      one:one,
                                  },
                                  function(data) {
                                      if (data || data.data) {
                                          alert("添加成功！");
                                      } else {
                                          alert("添加失败！");
                                      }
                                  }
                              );
                          }
                      });
                      see(one);
                  },
                  "json"
              );
              see(one);
          });

          $("#settings").on("click",function () {
              window.open("http://47.106.122.229:8080/zuoyu");
          })
      });
      function see(one){
          $("#window").html(one).show();
      }
  </script>
</html>
