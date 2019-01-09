function openWindow(){
	$.ajax({
		  type: "post",
		  url: "/seaGrid/area/getLastResult.do",
		  data:{
			 productCode:"uv10",
			 searchDate:searchDate,
			 timeAge:'08',
			 northLatitude:northLatitude,
			 southLatitude:southLatitude,
			 westLongitude:westLongitude,
			 eastLongitude:eastLongitude
		  },
		  dataType:"json",
		  success:function(responseData){
			
			  $("#chartUv10").highcharts({
					  chart: {
				            zoomType: 'xy',//放大缩小范围
				            height: 250
				        },
					  title: {
					        text: 'EC模式10米大风一致性检验'
					    },
					    subtitle: {
					        text: ''
					    },
					    yAxis: [{
					    	 opposite: false,
					    	 labels: {
					                format: '{value}',
					                style: {
					                    color: Highcharts.getOptions().colors[0]
					                }
					            },
					        title: {
					            text: '',
					            style: {
				                    color: Highcharts.getOptions().colors[1]
				                }
					        }
					    }],
					    tooltip: {
				            shared: true
				        },
					    legend: {
					    	 align: 'center',
				            verticalAlign: 'bottom',
				            layout: 'horizontal',
				            x: 0,
				            y: -0
		
					    },
					    credits: {//版权信息
					    	enabled: false
					    }, 
					    series: [{
					        name: '10米风',
					        type: 'spline',
					        data:responseData.yDatas,
					        tooltip: {
				                valueSuffix: ''
				            }
					    }],
					    responsive: {
					        rules: [{
					            condition: {
					                maxWidth: 500
					            },
					            chartOptions: {
					                legend: {
					                    layout: 'horizontal',
					                    align: 'center',
					                    verticalAlign: 'bottom'
					                }
					            }
					        }]
					    }
				  });
			  $("#chartNcepUv10").highcharts({
				  chart: {
			            zoomType: 'xy',//放大缩小范围
			            height: 250
			        },
				  title: {
				        text: 'NCEP模式10米大风一致性检验'
				    },
				    subtitle: {
				        text: ''
				    },
				    yAxis: [{
				    	 opposite: false,
				    	 labels: {
				                format: '{value}',
				                style: {
				                    color: Highcharts.getOptions().colors[0]
				                }
				            },
				        title: {
				            text: '',
				            style: {
			                    color: Highcharts.getOptions().colors[1]
			                }
				        }
				    }],
				    tooltip: {
			            shared: true
			        },
				    legend: {
				    	 align: 'center',
			            verticalAlign: 'bottom',
			            layout: 'horizontal',
			            x: 0,
			            y: -0
	
				    },
				    credits: {//版权信息
				    	enabled: false
				    }, 
				    series: [{
				        name: '10米风',
				        type: 'spline',
				        data:responseData.ncepYdata,
				        tooltip: {
			                valueSuffix: ''
			            }
				    }],
				    responsive: {
				        rules: [{
				            condition: {
				                maxWidth: 500
				            },
				            chartOptions: {
				                legend: {
				                    layout: 'horizontal',
				                    align: 'center',
				                    verticalAlign: 'bottom'
				                }
				            }
				        }]
				    }
			  });
			  //EC
			  var downLoadChart = $("#chartUv10").highcharts();
			  var times = new Array();
			  var data_length  = new Array();
			  for(var i=0;i<responseData.xLabs.length;i++){
				  times.push(responseData.xLabs[i])
			  }
			  downLoadChart.xAxis[0].setCategories(times);
			  //Ncep
			  var NcepChart = $("#chartNcepUv10").highcharts();
			  NcepChart.xAxis[0].setCategories(times);
		  },
		  error:function(){
			  alert("error");
		  }
		});
}