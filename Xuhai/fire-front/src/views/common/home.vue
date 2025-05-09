<template>
  <div id="main1">
    <dv-loading v-if="false">Loading...</dv-loading>
    <div class="topTitleBox">
      <div class="dateTimeBox">
        <div class="dateTimeBox-after"></div>
        <span class="dateText">
          <dv-decoration-6 :color="['#000000', '#4c4c4c']" style="height: 40px">

          </dv-decoration-6></span>
      </div>
      <dv-decoration-10 :color="['#000000', '#fff']" style="width: 33.33%; height: 10px; margin-top: 20px" />
      <div class="leftDecorate">
        <dv-decoration-3 :color="['#000000', '#fff']" style="width: 89%; height: 30px" />
      </div>
      <dv-decoration-8 :color="['#000000', '#000000']" style="width: 10%; height: 50px; margin-top: 25px" />
      <div class="title">
        <dv-decoration-5 :dur="2" :color="['#000000', '#000000']" style="width: 100%; height: 30px" /><span
          style="color:rgba(213, 101, 31)">智能消火栓平台</span>
        <dv-decoration-6 class="title-bototm" :reverse="true" :color="['#000000', '#4c4c4c']"
          style="width: 250px; height: 10px" />
      </div>
      <dv-decoration-10 :color="['#000000', '#fff']" style="
          width: 33.33%;
          height: 10px;
          margin-top: 15px;
          transform: rotateY(180deg);
        " />
    </div>

    <!-- 内容 -->
    <div class="mainbox">
      <ul class="clearfix nav1" style="width: 100%;">
        <li style="width: 50%">
          <dv-border-box-3>          
              <div class="boxnav" style="height: 100%;">
                <PieChart :data="pieData" />
              </div>
          </dv-border-box-3>
        </li>
        <li style="width: 50%">
          <dv-border-box-1>
            <!-- <div class="tit">消防栓</div> -->
            <div class="boxnav" style="height: 450px;">
              <LineChart :xData="lineXData" :yData="lineYData" />
            </div>
          </dv-border-box-1>
        </li>
        <li style="width: 100%">
          <dv-border-box-8>
            <div class="boxnav" style="height: 100%;">
              <BarChart :xData="barXData" :yData="barYData" />

            </div>
          </dv-border-box-8>
          <!-- <div style="margin-bottom: 20px;">设备异常</div>

          <div class="box">
            <div class="boxnav" style="height: 300px;">
              <ScrollList :listData="listData" />
            </div>
          </div> -->
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import PieChart from '../modules/statistics/location.vue';
import BarChart from '../modules/statistics/equipment.vue';
import LineChart from '../../components/big-data/LineChart.vue';
import ScrollList from '../../components/big-data/ScrollList.vue';

export default {
  data() {
    return {
      currentTime: '',
      timer: null,
      pieData: [
        { value: 335, name: '直接访问' },
        { value: 310, name: '邮件营销' },
        { value: 234, name: '联盟广告' },
        { value: 135, name: '视频广告' },
        { value: 1548, name: '搜索引擎' }
      ],
      barXData: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
      barYData: [120, 200, 150, 80, 70, 110, 130],
      lineXData: ['1月', '2月', '3月', '4月', '5月', '6月', '7月'],
      lineYData: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6],
      listData: [
        '列表项1',
        '列表项2',
        '列表项3',
        '列表项4',
        '列表项5',
        '列表项6',
        '列表项7',
        '列表项8',
        '列表项9',
        '列表项10'
      ]
    };
  },
  mounted() {

  },
  components: {
    PieChart,
    BarChart,
    LineChart,
    ScrollList
  },
  beforeDestroy() {
    clearTimeout(this.timer);
  },
  methods: {

  }
};
</script>

<style scoped>
@import 'css/style.css';
</style>

<style lang='scss' scoped>
#main1 {
  width: 100%;
  height: 100%;
  background-size: 100% 100%;
  overflow: hidden;

  ::v-deep .loading-tip {
    font-size: 23px !important;
    color: rgb(0, 0, 0);
    font-weight: bold;
  }

  .topTitleBox {
    width: 100%;
    height: 80px;
    display: flex;
    position: relative;

    .leftDecorate {
      position: absolute;
      left: 7%;
      top: 32px;
    }

    .dateTimeBox {
      position: absolute;
      right: 7%;
      top: 27px;
      color: #000000;
      font-size: 20px;
      width: 20.33%;
      height: 50px;
      text-align: center;
      line-height: 50px;
      cursor: pointer;
      -webkit-transform: skewX(-45deg);
      transform: skewX(-45deg);

      .dateText {
        display: inline-block;
        transform: skewX(45deg);
      }

      .dateTimeBox-after {
        position: absolute;
        right: -25px;
        top: 0;
        height: 50px;
        width: 50px;
        -webkit-transform: skewX(45deg);
        transform: skewX(45deg);
      }
    }

    .title {
      width: 500px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      font-size: 25px;
      color: white;
    }
  }

  // 媒体查询
  @media (max-width: 1200px) {
    .topTitleBox {
      width: 80%;
    }
  }

  .mainbox {
    padding: 20px;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .box {
      margin-bottom: 20px;
      background-color: rgba(255, 255, 255, 0.1);
      border-radius: 10px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
      overflow: hidden;

      .tit {
        padding: 10px;
        background-color: rgba(0, 0, 0, 0.3);
        color: white;
        font-weight: bold;
      }

      .boxnav {
        padding: 10px;
      }
    }
  }
}
</style>