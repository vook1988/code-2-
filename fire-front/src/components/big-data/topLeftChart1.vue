<!--
 * @Description: 
 * @Version: 2.0
 * @Autor: LMJ
 * @Date: 2021-09-07 15:05:21
 * @LastEditors: LMJ
 * @LastEditTime: 2021-09-17 13:16:18
-->
<template>
  <div style="width: 100%; height: 100%">
    <highcharts :options="chartOptions" :callback="myCallback" style="min-width: 450px; min-height: 380px; "></highcharts>
  </div>
</template>

<script>

import { Chart } from "highcharts-vue";
import { color } from 'highcharts';
export default {
  components: {
    highcharts: Chart
  },
  data() {
    return {
      chartOptions: {
        chart: {
          backgroundColor: 'rgba(255,255,255,0)',
		type: 'pie',
		options3d: {
			enabled: true,
			alpha: 45,
			beta: 0
		}
	},
	title: {
		text: '',
          left: "center",
          top: "1.5%",
          style: {
            color: "#000000",

          },
	},
	tooltip: {
		pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
	},
	plotOptions: {
		pie: {
			allowPointSelect: true,
			cursor: 'pointer',
			depth: 90,
			dataLabels: {
				enabled: true,
				format: '{point.name}'
			}
		}
	},
  credits:{
     enabled: false // 禁用版权信息
},
	series: [{
		type: 'pie',
		name: '占比',
		data: [
			['A.非常有信心',  10.0,],
			['B.比较有信心',       32.0],
			['C.有信心',   15.0],
			{
				name: 'D.当前无信心',
				y: 40.0,
				sliced: true,
				selected: true
			},
			
		]
	}]
      }
    };
  },
  methods: {
    myCallback() {
    //   console.log("this is callback function");
    }
  }
};
</script>

<style scoped lang="scss">
.highcharts-container {
  width:100%;
  height: 100%;
  border: 1px solid #ddd;
  margin: auto;
}

</style>
