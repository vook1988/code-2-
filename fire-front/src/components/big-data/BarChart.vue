<template>
    <div ref="chartRef" style="width: 100%; height: 100%"></div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  
  export default {
    props: {
      xData: {
        type: Array,
        default: () => []
      },
      yData: {
        type: Array,
        default: () => []
      }
    },
    mounted() {
      this.initChart();
    },
    methods: {
      initChart() {
        const chart = echarts.init(this.$refs.chartRef);
        const option = {
          xAxis: {
            type: 'category',
            data: this.xData
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              data: this.yData,
              type: 'bar',
              itemStyle: {
                color: '#1890ff'
              }
            }
          ]
        };
        chart.setOption(option);
      }
    }
  };
  </script>