<template>
  <div ref="chartRef" style="width: 100%; height: 450px"></div>
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
              // 添加标题
              title: {
                  text: '  近7个月消防栓异常情况'
              },
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
                      type: 'line',
                      smooth: true,
                      itemStyle: {
                          color: '#f5222d'
                      }
                  },
                  {
                      // 添加柱状图系列
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