<template>
    <div ref="chartRef" style="width: 100%; height: 100%"></div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  
  export default {
    props: {
      data: {
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
          tooltip: {
            trigger: 'item'
          },
          series: [
            {
              name: '数据',
              type: 'pie',
              radius: '50%',
              data: this.data,
              label: {
                formatter: '{b}: {c} ({d}%)'
              },
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
              }
            }
          ]
        };
        chart.setOption(option);
      }
    }
  };
  </script>