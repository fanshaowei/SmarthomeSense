<html>
<head>
    <meta charset="UTF-8">
    <title>Demo</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<h2>Hello World!</h2>

<script src="http://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script src="http://cdn.bootcss.com/handlebars.js/1.3.0/handlebars.min.js"></script>

<script>
    var str = {"jobInfo":{
		"jobName":"情景关联",
		"jobType":"senceRelated",
		"jobState":"0",
		"trigger":["0/20 * * * * ?","0/20 * * * * ?"],
		"triggerType":"cron",
		"cronTriggerType":"workDay",
		"user":"fansw",
		"token":"1111111122222222",
		"sourceScene":{"sceneId":"1","sceneName":"333"},
		"doScene":{"sceneId":"2","sceneName":"3333"}
	}}; 

    $(function() {
  /**    
  $.ajax({
            type: 'get',
            url: 'http://anjubao.com:8080/quartzserver/appjob/addNewJobInfo?jsonparam={"jobInfo":"111"}',		   
            dataType:"json",							
			contentType:"application/json;charset=utf-8",
			data:"json=" + JSON.stringify(str),
            success: function(data) {
                alert(data);
            }
        });
 **/
		
		var str = {
		      "username":"13580387033",
			  "req_token":"13580387033d2a09297ece8486ba27e4466909c32f81464098014173",
			  "terminal_code":"01061190000013B9",			  
			  "equipment_list":{"EQUIPMENTLIST":[
					{"equipment_code":"0182119FFFFFFD5F","name_device":"无线门磁","num_channel":"1","name_channel":"chn"}  
				]
			  }
			};
		
		$.ajax({
            type: 'POST',
            url: 'http://127.0.0.1:9802/SmarthomeSense/addDevice',		
			data: JSON.stringify(str),
            dataType:"json",							
			contentType:"application/json;charset=utf-8",
			//data:"json=" + JSON.stringify(str),
            success: function(data) {
                alert(data);
            }
        });
    });
</script>
</body>
</html>
