var option="";
$(function(){
	tree();
	set();
	/*timer=setTimeout("location.reload()",5000);*/
})

function set(){
	$.ajax( {  
		type : "POST",  
		url : "../control/getcontorl.do",
			dataType : "json",
			success : function(data) {
				for(var i =0;i<data.length;i++){
					var name = data[i].ip;
					var pid = data[i].pid-1;
					var color = "";
					if(data[i].available==1){
						color="red";
					}else{
						color="green";
					}
					option.series[0].data[0].children[pid].children.push({
				        name: name,
				        id:data[i].id,
				        symbol: 'circle',
				        symbolSize: 20,
				        itemStyle: {
				            normal: {
				                color: color,
				                label: {
				                    show: true,
				                    position: 'right'
				                },
				                
				            },
				            emphasis: {
				                label: {
				                    show: false
				                },
				                borderWidth: 0
				            }
				        }
					});
				}
				 var myChart = echarts.init(document.getElementById('main'));
				 var ecConfig = echarts.config;  
				 myChart.on(ecConfig.EVENT.CLICK, eConsole);  
				 myChart.setOption(option); 
			}
		});
}

function tree(){
    var myChart = echarts.init(document.getElementById('main')); 
    	option = {
				    title : {
				        text: '网站监控'
				    },
				    calculable : false,
				
				    series : [
				        {
				        	clickable : true, 
				            name:'树图',
				            type:'tree',
				            orient: 'horizontal',  
				            rootLocation: {x: 100, y:'60%'}, 
				            nodePadding: 20,
				            symbol: 'circle',
				            symbolSize: 40,
				            itemStyle: {
				                normal: {
				                    label: {
				                        show: true,
				                        position: 'inside',
				                        textStyle: {
				                            color: '#000',
				                            fontSize: 15,
				                            fontWeight:  'bolder'
				                        }
				                    },
				                    lineStyle: {
				                        color: '#000',
				                        width: 1,
				                        type: 'broken' 
				                    }
				                },
				                emphasis: {
				                    label: {
				                        show: false
				                    },
				                	color:"blue"
				                }
				            },
				            data: [
				                {
				                    name: '万方',
				                    value: 6,
				                    symbol: 'circle',
						            symbolSize: 60,
				                    itemStyle: {
				                        normal: {
				                            label: {
				                                show: true,
				                                "textStyle": {
		                                            "color": "#000"
		                                        }
				                            }
				                        }
				                    },
				                    children: [
				                        {
				                        	name: '学术圈',
				                        	pid:1,
				                        	symbol: 'circle',
				 				            symbolSize: 50,
				                            itemStyle: {
				                                normal: {
				                                    label: {
				                                        show: true,
				                                        "textStyle": {
				                                            "color": "#000"
				                                        }
				                                    }
				                                }
				                            },
				                            children: [
				                                
				                            ]
				                        },
				                        {
				                            name: '工作台',
				                            symbol: 'circle',
				                            pid:2,
								            symbolSize:  50,
				                            itemStyle: {
				                                normal: {
				                                    label: {
				                                        show: true,
				                                        "textStyle": {
				                                            "color": "#000"
				                                        }
				                                    }
				                                    
				                                }
				                            },
				                            children: [
						                                
							               ]
				                        },
				                        {
				                            name: '知识发现',
				                            symbol: 'circle',
								            symbolSize:  50,
								            pid:3,
				                            itemStyle: {
				                                normal: {
				                                    label: {
				                                        show: true,
				                                        "textStyle": {
				                                            "color": "#000"
				                                        }
				                                    }
				                                    
				                                }
				                            },
				                            children: [
						                                
							                            ]
				                        },
				                        {
				                            name: '数据处理',
				                            symbol: 'circle',
								            symbolSize:  50,
								            pid:4,
				                            itemStyle: {
				                                normal: {
				                                    label: {
				                                        show: true,
				                                        "textStyle": {
				                                            "color": "#000"
				                                        }
				                                    }
				                                    
				                                }
				                            },
				                            children: [
						                                
							                            ]
				                        }
				                    ]
				                }
				            ]
				        }
				    ]
				};
}
function eConsole(param) {    
    if (typeof param.seriesIndex == 'undefined') {    
        return;    
    }    
    if (param.type == 'click') { 
    	if(param.data.id!=null){
    		updatecontrol(param.data.id)
    	}else if(param.data.pid!==null){
    		addcontrol(param.data.pid);
    	}else{
    		return;
    	}
    }    
}

function updatecontrol(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['500px', '300px'],
	    title: '监测修改',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../control/updatecontrol.do?id="+id,
	}); 
}

function addcontrol(id){
	layer.open({
	    type: 2, //page层 1div，2页面
	    area: ['500px', '300px'],
	    title: '监测增加',
	    moveType: 1, //拖拽风格，0是默认，1是传统拖动
	    content: "../control/addcontrol.do?pid="+id,
	}); 
}

